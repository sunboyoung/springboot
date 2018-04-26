package com.zms.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Author:zms
 *
 * @Description:Spring boot入口
 * @SpringBootApplication注解相当于以下三个注解
 * @Configuration 代表当前类一个配置类
 * @EnableAutoConfiguration 核心功能 自动配置 根据当前项目引入的jar自动配置
 * @ComponentScan 如果不显示调用 默认扫描boot配置包目录下的注解
 * 				   如果显示调用 不加basePackage默认扫描所有包
 * @Date:2018/4/26 14:47
 *
 */
@SpringBootApplication
@MapperScan("com.zms.mapper")
@ComponentScan("com.zms")
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
}
