package com.codeofli.gulimall.seckill.service.impl;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.codeofli.common.to.mq.SeckillOrderTo;
import com.codeofli.common.utils.R;
import com.codeofli.common.vo.MemberResponseVo;
import com.codeofli.gulimall.seckill.feign.CouponFeignService;
import com.codeofli.gulimall.seckill.feign.ProductFeignService;
import com.codeofli.gulimall.seckill.interceptor.LoginUserInterceptor;
import com.codeofli.gulimall.seckill.service.SeckillService;
import com.codeofli.gulimall.seckill.to.SeckillSkuRedisTo;
import com.codeofli.gulimall.seckill.vo.SeckillSessionWithSkusVo;
import com.codeofli.gulimall.seckill.vo.SkuInfoVo;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Description:
 * @Created: with IntelliJ IDEA.
 * @author: 夏沫止水
 * @createTime: 2020-07-09 19:29
 **/

@Slf4j
@Service
public class SeckillServiceImpl implements SeckillService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private CouponFeignService couponFeignService;

    @Autowired
    private ProductFeignService productFeignService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final String SESSION__CACHE_PREFIX = "seckill:sessions:";

    private final String SECKILL_CHARE_PREFIX = "seckill:skus";

    private final String SKU_STOCK_SEMAPHORE = "seckill:stock:";    //+商品随机码

    @Override
    public void uploadSeckillSkuLatest3Days() {

        //1、扫描最近三天的商品需要参加秒杀的活动
        R lates3DaySession = couponFeignService.getLates3DaySession();
        if (lates3DaySession.getCode() == 0) {
            //上架商品
            List<SeckillSessionWithSkusVo> sessionData = lates3DaySession.getData("data", new TypeReference<List<SeckillSessionWithSkusVo>>() {
            });
            //缓存到Redis
            //1、缓存活动信息
            saveSessionInfos(sessionData);

            //2、缓存活动的关联商品信息
            saveSessionSkuInfo(sessionData);
        }

    }


    /**
     * 缓存秒杀活动信息
     *
     * @param sessions
     */
    private void saveSessionInfos(List<SeckillSessionWithSkusVo> sessions) {

        sessions.stream().forEach(session -> {

            //获取当前活动的开始和结束时间的时间戳
            long startTime = session.getStartTime().getTime();
            long endTime = session.getEndTime().getTime();

            //存入到Redis中的key
            String key = SESSION__CACHE_PREFIX + startTime + "_" + endTime;

            //判断Redis中是否有该信息，如果没有才进行添加
            Boolean hasKey = redisTemplate.hasKey(key);
            //缓存活动信息
            if (!hasKey) {
                //获取到活动中所有商品的skuId
                List<String> skuIds = session.getRelationSkus().stream()
                        .map(item -> item.getPromotionSessionId() + "-" + item.getSkuId().toString()).collect(Collectors.toList());
                redisTemplate.opsForList().leftPushAll(key, skuIds);
            }
        });

    }

    /**
     * 缓存秒杀活动所关联的商品信息
     *
     * @param sessions
     */
    private void saveSessionSkuInfo(List<SeckillSessionWithSkusVo> sessions) {

        sessions.stream().forEach(session -> {
            //准备hash操作，绑定hash
            BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
            session.getRelationSkus().stream().forEach(seckillSkuVo -> {
                //生成随机码
                String token = UUID.randomUUID().toString().replace("-", "");
                //用**场次id+商品id**作为分布式的key来保证只上架一次的一个场次的某一个商品信息
                String redisKey = seckillSkuVo.getPromotionSessionId().toString() + "-" + seckillSkuVo.getSkuId().toString();
                if (!operations.hasKey(redisKey)) {

                    //缓存我们商品信息
                    SeckillSkuRedisTo redisTo = new SeckillSkuRedisTo();
                    Long skuId = seckillSkuVo.getSkuId();
                    //1、先查询sku的基本信息，调用远程服务
                    R info = productFeignService.getSkuInfo(skuId);
                    if (info.getCode() == 0) {
                        SkuInfoVo skuInfo = info.getData("skuInfo", new TypeReference<SkuInfoVo>() {
                        });
                        redisTo.setSkuInfo(skuInfo);
                    }

                    //2、sku的秒杀信息
                    BeanUtils.copyProperties(seckillSkuVo, redisTo);

                    //3、设置当前商品的秒杀时间信息
                    redisTo.setStartTime(session.getStartTime().getTime());
                    redisTo.setEndTime(session.getEndTime().getTime());

                    //4、设置商品的随机码（防止恶意攻击）
                    redisTo.setRandomCode(token);

                    //序列化json格式存入Redis中
                    String seckillValue = JSON.toJSONString(redisTo);
                    operations.put(seckillSkuVo.getPromotionSessionId().toString() + "-" + seckillSkuVo.getSkuId().toString(), seckillValue);

                    //如果当前这个场次的商品库存信息已经上架就不需要上架
                    //5、使用库存作为分布式Redisson信号量（限流）
                    // 使用库存作为分布式信号量
                    RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + token);
                    // 商品可以秒杀的数量作为信号量
                    semaphore.trySetPermits(seckillSkuVo.getSeckillCount());
                }
            });
        });
    }





    /**
     * 获取到当前可以参加秒杀商品的信息
     * @return
     */
    @SentinelResource(value = "getCurrentSeckillSkusResource",blockHandler = "blockHandler")
    @Override
    public List<SeckillSkuRedisTo> getCurrentSeckillSkus() {

        try (Entry entry = SphU.entry("seckillSkus")) {
            //1、确定当前时间属于哪个秒杀场次
            long currentTime = System.currentTimeMillis();

            //从Redis中查询到所有key以seckill:sessions开头的所有数据
            Set<String> keys = redisTemplate.keys(SESSION__CACHE_PREFIX + "*");
            for (String key : keys) {
                //seckill:sessions:1594396764000_1594453242000
                String replace = key.replace(SESSION__CACHE_PREFIX, "");
                String[] s = replace.split("_");
                //获取存入Redis商品的开始时间
                long startTime = Long.parseLong(s[0]);
                //获取存入Redis商品的结束时间
                long endTime = Long.parseLong(s[1]);

                //判断是否是当前秒杀场次
                if (currentTime >= startTime && currentTime <= endTime) {
                    //2、获取这个秒杀场次需要的所有商品信息
                    // 大家直接lrange 0 -1 就可以取出所有了
                    List<String> range = redisTemplate.opsForList().range(key, -100, 100);
                    BoundHashOperations<String, String, String> hasOps = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
                    assert range != null;
                    List<String> listValue = hasOps.multiGet(range);
                    if (listValue != null && listValue.size() >= 0) {

                        List<SeckillSkuRedisTo> collect = listValue.stream().map(item -> {
                            String items = (String) item;
                            SeckillSkuRedisTo redisTo = JSON.parseObject(items, SeckillSkuRedisTo.class);
                            // redisTo.setRandomCode(null);当前秒杀开始需要随机码
                            return redisTo;
                        }).collect(Collectors.toList());
                        return collect;
                    }
                    break;
                }
            }
        } catch (BlockException e) {
            log.error("资源被限流{}",e.getMessage());
        }

        return null;
    }

    public List<SeckillSkuRedisTo> blockHandler(BlockException e) {

        log.error("getCurrentSeckillSkusResource被限流了,{}",e.getMessage());
        return null;
    }

    /**
     * 根据skuId查询商品是否参加秒杀活动
     *
     * @param skuId
     * @return
     */
    @Override
    public SeckillSkuRedisTo getSkuSeckilInfo(Long skuId) {

        //1、找到所有需要秒杀的商品的key信息---seckill:skus
        BoundHashOperations<String, String, String> hashOps = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);

        //拿到所有的key
        Set<String> keys = hashOps.keys();
        if (keys != null && keys.size() > 0) {
            //4-45 正则表达式进行匹配
            String reg = "\\d-" + skuId;
            for (String key : keys) {
                //如果匹配上了
                if (Pattern.matches(reg, key)) {
                    //从Redis中取出数据来
                    String redisValue = hashOps.get(key);
                    //进行序列化
                    SeckillSkuRedisTo redisTo = JSON.parseObject(redisValue, SeckillSkuRedisTo.class);

                    //随机码
                    Long currentTime = System.currentTimeMillis();
                    Long startTime = redisTo.getStartTime();
                    Long endTime = redisTo.getEndTime();
                    //如果当前时间大于等于秒杀活动开始时间并且要小于活动结束时间
                    if (currentTime >= startTime && currentTime <= endTime) {
                        return redisTo;
                    }
                    redisTo.setRandomCode(null);
                    return redisTo;
                }
            }
        }
        return null;
    }


    /**
     * 当前商品进行秒杀（秒杀开始）
     *  //TODO-limingzhong 秒杀细化 2023/6/10 16:33
     * @param killId
     * @param key
     * @param num
     * @return
     */
    @Override
    public String kill(String killId, String key, Integer num) throws InterruptedException {
        long killStartTime = System.currentTimeMillis();
        BoundHashOperations<String, String, String> ops = redisTemplate.boundHashOps(SECKILL_CHARE_PREFIX);
        String json = ops.get(killId);
        String orderSn = null;
        if (!StringUtils.isEmpty(json)) {
            SeckillSkuRedisTo redisTo = JSON.parseObject(json, SeckillSkuRedisTo.class);
            //1. 验证时效
            long current = System.currentTimeMillis();
            if (current >= redisTo.getStartTime() && current <= redisTo.getEndTime()) {
                //2. 验证商品和商品随机码是否对应
                String redisKey = redisTo.getPromotionSessionId() + "-" + redisTo.getSkuId();
                if (redisKey.equals(killId) && redisTo.getRandomCode().equals(key)) {
                    //3. 验证当前用户是否购买过
                    MemberResponseVo memberResponseVo = LoginUserInterceptor.loginUser.get();
                    long ttl = redisTo.getEndTime() - System.currentTimeMillis();
                    //3.1 通过在redis中使用 用户id-skuId 来占位看是否买过
                    Boolean occupy = redisTemplate.opsForValue().setIfAbsent(memberResponseVo.getId() + "-" + redisTo.getSkuId(), num.toString(), ttl, TimeUnit.MILLISECONDS);
                    //3.2 占位成功，说明该用户未秒杀过该商品，则继续
                    if (occupy) {
                        //4. 校验库存和购买量是否符合要求
                        if (num <= redisTo.getSeckillLimit()) {
                            //4.1 尝试获取库存信号量
                            RSemaphore semaphore = redissonClient.getSemaphore(SKU_STOCK_SEMAPHORE + redisTo.getRandomCode());
                            boolean acquire = semaphore.tryAcquire(num, 100, TimeUnit.MILLISECONDS);
                            //4.2 获取库存成功
                            if (acquire) {
                                //5. 发送消息创建订单
                                //5.1 创建订单号
                                orderSn = IdWorker.getTimeId();
                                //5.2 创建秒杀订单to
                                SeckillOrderTo orderTo = new SeckillOrderTo();
                                orderTo.setMemberId(memberResponseVo.getId());
                                orderTo.setNum(num);
                                orderTo.setOrderSn(orderSn);
                                orderTo.setPromotionSessionId(redisTo.getPromotionSessionId());
                                orderTo.setSeckillPrice(redisTo.getSeckillPrice());
                                orderTo.setSkuId(redisTo.getSkuId());
                                //5.3 发送创建订单的消息
                                rabbitTemplate.convertAndSend("order-event-exchange", "order.seckill.order", orderTo);
                                log.info("秒杀耗时：{}ms", System.currentTimeMillis() - killStartTime);
                            }
                        }
                    }
                }
            }
            return orderSn;
        }
        return null;
    }

}
