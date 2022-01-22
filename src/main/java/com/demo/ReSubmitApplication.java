package com.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * 防重复提交
 */
@SpringBootApplication
public class ReSubmitApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReSubmitApplication.class, args);
	}

}
