package com.codeofli.gulimall.product.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyRedissonConfig {
    /**
     * 所有对Redisson的使用都是通过Redissonclient对象操作
     * @return
     */
    @Bean
    public RedissonClient redissonClient(){
        //1、创建配置
        // Redis url should start with redis:// or rediss://
        Config config = new Config();
        config.useSingleServer().setAddress("redis://192.168.242.5:6379");
        //2、根据config创建出RedissonClient示例
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
