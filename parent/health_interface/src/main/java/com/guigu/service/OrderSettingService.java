package com.guigu.service;

import com.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    public  void  add(List<OrderSetting> list);


    public  List<Map> getOrderSettingByMonth(String data);

    public  void editOrderSettingByDate(OrderSetting orderSetting);
}
