package com.example.demo.proxy.service;

/**
 * 业务接口： 卖食物
 */
public interface IBoss {

    /**
     * 根据食物的size 获取价格
     * @param size
     * @return
     */
    int food(String size);
}
