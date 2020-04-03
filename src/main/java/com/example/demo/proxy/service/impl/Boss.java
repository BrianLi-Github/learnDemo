package com.example.demo.proxy.service.impl;

import com.example.demo.proxy.service.IBoss;

/**
 * 实现卖食物的逻辑
 */
public class Boss implements IBoss {
    @Override
    public int food(String size) {
        if (size == null || size.trim().equals("")) {
            System.out.println("掌柜的，给我上一碗面，默认大小。。。");
            return 100;
        }
        System.out.println("掌柜的，给我上一碗面，所选型号： " + size);
        return 50;
    }
}
