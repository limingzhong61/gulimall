package com.codeofli.gulimall.product;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.codeofli.gulimall.product.entity.BrandEntity;
import com.codeofli.gulimall.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
class GulimallProductApplicationTests {

    @Autowired
    BrandService brandService;





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
