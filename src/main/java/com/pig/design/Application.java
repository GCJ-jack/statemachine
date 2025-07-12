package com.pig.design;

import cn.hutool.extra.spring.EnableSpringUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.pig.design.mapper")
@SpringBootApplication
@EnableSpringUtil
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
