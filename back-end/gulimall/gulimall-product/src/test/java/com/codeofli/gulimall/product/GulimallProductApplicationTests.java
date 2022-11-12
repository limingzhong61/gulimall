package com.codeofli.gulimall.product;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeofli.gulimall.product.entity.BrandEntity;
import com.codeofli.gulimall.product.service.BrandService;
import com.codeofli.gulimall.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;


    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void testRedisson() {
        System.out.println(redissonClient);
    }
    @Test
    public void testStringRedisTemplate(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("hello","world_"+ UUID.randomUUID().toString());
        String hello = ops.get("hello");
        System.out.println(hello);
    }

    @Test
    public void testFindPath() {
        Long[] catelogPath = categoryService.findCatelogPath(225L);
        log.info("完整路径：{}", Arrays.asList(catelogPath));
    }

    @Test
    void contextLoads() {
        //BrandEntity brandEntity = new BrandEntity();
        //brandEntity.setDescript("hello");
        //brandEntity.setName("华为");
        //brandService.save(brandEntity);
        //System.out.println("保存成功");
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 1L));
        list.forEach((item) -> {
            System.out.println(item);
        });
    }


}
