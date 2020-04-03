package com.example.demo.proxy.test;

import com.example.demo.proxy.main.ProxyBoss;
import com.example.demo.proxy.service.IBoss;
import com.example.demo.proxy.service.impl.Boss;

/**
 * 什么是动态代理？ 简单写一个模板接口，剩下的个性化工作，给动态代理来完成
 */
public class SaleAction {

    public static void main(String[] args) {
        sale();
        System.out.println("-------------------------\n");
        saleProxy();
    }

    public static void sale() {
        IBoss boss = new Boss();
        System.out.println("纯手工面馆!");
        int foodPrice = boss.food("xxl");
        System.out.println("杂酱面一份， 价格: " + foodPrice);
    }

    /**
     * 使用代理，在这个代理中，只代理了Boss的food方法
     * 定制业务，可以改变原来接口的参数，返回值等
     */
    public static void saleProxy() {
        IBoss boss = ProxyBoss.getProxy(10, Boss.class);
        System.out.println("纯手工面馆， 代理经营!");
        int foodPrice = boss.food("xxl");
        System.out.println("杂酱面一份， 折后价格: " + foodPrice);
    }
}
