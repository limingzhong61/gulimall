package com.codeofli.gulimall.member;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@SpringBootTest
class GulimallMemberApplicationTests {

    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //$2a$10$lOnKZ7qFThhbsQypuOnaleXUfBhASCqgLPe1saCd/iDuhwk0qlRIa
        //$2a$10$vv6wk1gMONSK31bhEYjlWuNWPW5aCemd87P3oLou4lQkEFRXuTtAa
        String encode = passwordEncoder.encode("123456");
        boolean matches = passwordEncoder.matches("123456", "$2a$10$vv6wk1gMONSK31bhEYjlWuNWPW5aCemd87P3oLou4lQkEFRXuTtAa");
        System.out.println(encode + "=>" + matches);
    }

}
