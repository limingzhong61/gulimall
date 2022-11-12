package com.codeofli.gulimall.product.web;

import com.codeofli.common.utils.R;
import com.codeofli.gulimall.product.entity.CategoryEntity;
import com.codeofli.gulimall.product.feign.SeckillFeignService;
import com.codeofli.gulimall.product.service.CategoryService;
import com.codeofli.gulimall.product.vo.Catalog2Vo;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SeckillFeignService seckillFeignService;

    @GetMapping({"/", "index.html"})
    public String getIndex(Model model) {
        //获取所有的一级分类
        List<CategoryEntity> catagories = categoryService.getLevel1Catagories();
        model.addAttribute("catagories", catagories);
        return "index";
    }

    @ResponseBody
    @RequestMapping("index/catalog.json")
    public Map<String, List<Catalog2Vo>> getCatlogJson() {
        Map<String, List<Catalog2Vo>> map = categoryService.getCatalogJson();
        return map;
    }

    @GetMapping("/read")
    @ResponseBody
    public String read() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("ReadWrite-Lock");
        RLock rLock = lock.readLock();
        String s = "";
        try {
            rLock.lock();
            System.out.println("读锁加锁" + Thread.currentThread().getId());
            Thread.sleep(5000);
            s = redisTemplate.opsForValue().get("lock-value");
        } finally {
            rLock.unlock();
            return "读取完成:" + s;
        }
    }

    @GetMapping("/write")
    @ResponseBody
    public String write() {
        RReadWriteLock lock = redissonClient.getReadWriteLock("ReadWrite-Lock");
        RLock wLock = lock.writeLock();
        String s = UUID.randomUUID().toString();
        try {
            wLock.lock();
            System.out.println("写锁加锁" + Thread.currentThread().getId());
            Thread.sleep(10000);
            redisTemplate.opsForValue().set("lock-value", s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            wLock.unlock();
            return "写入完成:" + s;
        }
    }

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        // 1、获取一把锁，只要锁的名字一样，就是同一把锁
        RLock lock = redissonClient.getLock("my-lock");
        // 2、加锁
        //lock.lock(); //阻塞式等待。默认加的锁都是30s时间。
        //  1)、锁的自动续期，如果业务超长，运行期间自动给锁续上新的30s。不用担心业务时间长，锁自动过期被删除
        // 2）、加锁的业务只要运行完成，就不会给当前锁续期，即使不手动解锁，锁默认在30s以后自动删除。

        lock.lock(10, TimeUnit.SECONDS); //10秒自动解锁自动解锁时间一定要大于业务的执行时间。
        // 问题:lock.lock(10,TimeUnit.SECONDS);在锁时间到了以后，不会自动续期。

        try {
            System.out.println("加锁成功，执行业务..." + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (Exception e) {
        } finally {
            // 3、解锁将设解锁代码没有运行，redisson会不会出现死锁
            System.out.println("释放锁..." + Thread.currentThread().getId());
            lock.unlock();
        }
        return "hello";
    }

    @GetMapping("/park")
    @ResponseBody
    public String park() {
        RSemaphore park = redissonClient.getSemaphore("park");
        try {
            park.acquire(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "停进2";
    }

    @GetMapping("/go")
    @ResponseBody
    public String go() {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release(2);
        return "开走2";
    }

    @GetMapping("/setLatch")
    @ResponseBody
    public String setLatch() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("CountDownLatch");
        try {
            latch.trySetCount(5);
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "门栓被放开";
    }

    @GetMapping("/offLatch")
    @ResponseBody
    public String offLatch() {
        RCountDownLatch latch = redissonClient.getCountDownLatch("CountDownLatch");
        latch.countDown();
        return "门栓被放开1";
    }

    @ResponseBody
    @GetMapping("/getSeckillSkuInfo/{skuId}")
    public R getSeckillSkuInfo(@PathVariable("skuId") Long skuId) {
        return seckillFeignService.getSeckillSkuInfo(skuId);
    }

}
