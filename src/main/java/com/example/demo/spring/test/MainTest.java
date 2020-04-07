package com.example.demo.spring.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainTest {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring/spring-demo.xml");
    }
}
