package com.example.demo.netty.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * 服务器返回的信息结构
 */
@Data
public class ResponseModal implements Serializable {

    private String status;

    private String message;
}
