package com.example.demo.netty.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 业务信息类，主要包含业务处理所需要的信息
 */
@Data
public class MessageObj implements Serializable {

    private String objId;

    private String msg;

    private Date actionTime;
}
