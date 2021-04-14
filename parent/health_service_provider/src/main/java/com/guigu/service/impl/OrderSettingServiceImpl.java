package com.guigu.service.impl;

import com.guigu.dao.OrderSettingDao;
import com.guigu.service.OrderSettingService;
import com.health.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 预约设置服务
 */
@Service("orderSettingService")
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {


    //导入dao资源
    @Autowired
    private OrderSettingDao orderSettingDao;
    //批量导入预约设置数据
    @Override
    public void add(List<OrderSetting> orderSettings) {
        if (orderSettings != null && orderSettings.size() > 0) {
            //判断数据库中是否已经存在当天的日期
            for (OrderSetting orderSetting : orderSettings) {
                long count = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (count > 0) {
                    //已经进行了预约设计-执行更新操作
                    orderSettingDao.undate(orderSetting);
                } else {
                    //还没进行预约设计-执行新增操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    @Override
    public List<Map> getOrderSettingByMonth(String date) {
        //每月第一天
        String begain=date+"-1";
        //每月最后一天（假设每月都有31号）
        String end=date+"-31";
        Map<String,String> map=new HashMap<>();
        map.put("begain",begain);
        map.put("end",end);
        List<OrderSetting> orderSettings = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result=new ArrayList<>();
        if (orderSettings!=null&&orderSettings.size()>0){
            for (OrderSetting orderSetting : orderSettings) {
                Map<String,Object> data=new HashMap<>();
                data.put("date",orderSetting.getOrderDate().getDate());
                data.put("number",orderSetting.getNumber());
                data.put("reservations",orderSetting.getReservations());
                result.add(data);
            }
        }

        return result;
    }

    @Override
    public void editOrderSettingByDate(OrderSetting orderSetting) {
        //根据日期查询是否已经预约设置
        Date orderDate = orderSetting.getOrderDate();
        //根据日期查询是否已经进行预约设置
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count>0){
            orderSettingDao.editReservationsByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }


}
