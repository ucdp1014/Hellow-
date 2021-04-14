package com.guigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guigu.service.SetmealService;
import com.health.constant.MessageConstant;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    //查询所有套餐
    @RequestMapping("/getSetmeal.do")
    public Result getSetmeal(){
        try {
            List<Setmeal> all = setmealService.findAll();
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,all);
        }catch (Exception e){
        e.printStackTrace();
        return  new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }


    //根据套餐id查询套餐详情
    @RequestMapping("/findById.do")
    public Result findById(Integer id){
        try {
            Setmeal byId = setmealService.findById(id);
            return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return  new Result(false,MessageConstant.GET_SETMEAL_LIST_FAIL);
        }
    }
}
