package com;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class MyTestt {

    @Test
    public void test() throws IOException {
        new ClassPathXmlApplicationContext("classpath:spring-service.xml");
        System.in.read();
    }
}
