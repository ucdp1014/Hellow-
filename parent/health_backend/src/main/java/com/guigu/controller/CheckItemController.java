package com.guigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guigu.service.CheckItemService;
import com.health.constant.MessageConstant;
import com.health.entity.CheckItemDeleteException;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckItem;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 检查项
 */


@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

        @Reference
         private CheckItemService checkItemService;

    //新增
    @RequestMapping("/add.do")
    public Result add(@RequestBody CheckItem checkItem){
        try {
            checkItemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.ADD_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);
    }
    //查询所有(分页查询)
    @RequestMapping("findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            if (queryPageBean.getQueryString()==null){
                queryPageBean.setQueryString("");

            }
        System.out.println(checkItemService);
        System.out.println(queryPageBean);
        return checkItemService.findPage(queryPageBean);
    }
    //删除检查项
    @RequestMapping("/delete.do")
    public Result deltetByid(Integer id){
        try {
            checkItemService.deltetByid(id);
        }catch (Exception e){
            e.printStackTrace();
            if (e instanceof CheckItemDeleteException){
                return new Result(false,"已经有检查组关联此检查项，请先输出相应的检查组");
            }
            return new Result(false, MessageConstant.DELETE_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    @RequestMapping("/edit.do")
    public Result edit(@RequestBody CheckItem checkItem){
        System.out.println(checkItem);
        try {
            checkItemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }

    //不带分页的查询所有
    @RequestMapping("/findAll.do")
    public Result findAll(){
        List<CheckItem> data=null;
        try {
            data= checkItemService.findAll();
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKITEM_FAIL);
        }
        return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,data);
    }
}
