package com.example.demo.spring.service.impl;

import com.example.demo.spring.annotation.RpcService;
import com.example.demo.spring.service.BusinessesService;

@RpcService("businessesService")
public class BusinessesServiceImpl implements BusinessesService {
    @Override
    public String doSomething(String param) {
        System.out.println("........Do something........ param: " + param);
        return "param: " + param + " result";
    }
}
