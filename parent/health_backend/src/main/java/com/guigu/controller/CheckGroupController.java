package com.guigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.guigu.service.CheckGroupService;
import com.health.constant.MessageConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.CheckGroup;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckGroupController {

    @Reference
    private CheckGroupService checkGroupService;

    //新增
    @RequestMapping("/add.do")
    public Result add (@RequestBody CheckGroup checkGroup , Integer[] checkitemsIds){
        System.out.println(checkGroupService);
        try{
            System.out.println(checkGroup);
            System.out.println(checkitemsIds);
            checkGroupService.add(checkGroup,checkitemsIds);

        }catch (Exception e){
        e.printStackTrace();
        //新增失败
        return new Result(false, MessageConstant.ADD_CHECKGROUP_FAIL);
        }
        //新增成功
        return  new Result(true, MessageConstant.ADD_CHECKGROUP_SUCCESS);
    }


//    分页查询
    @RequestMapping("findPage.do")
    private PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        System.out.println(queryPageBean);
        return  checkGroupService.pageQuery(queryPageBean);
    }



    //根据ID查询
    @RequestMapping("/findById.do")
    public Result findById (@RequestBody Integer id){
        System.out.println(checkGroupService);
        try{
       CheckGroup checkGroup =checkGroupService.findById(id);
            return  new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();

            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    //根据检查组查询多个检查项
    @RequestMapping("findCheckitemIdsByCheckgroupId.do")
    public  Result findCheckitemIdsByCheckgroupId(@RequestBody Integer id) {
        try {
            List<Integer> checkItemIds = checkGroupService.findCheckitemIdsByCheckgroupId(id);
            return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS, checkItemIds);
        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false, MessageConstant.QUERY_CHECKITEM_SUCCESS);
        }
    }



    //编辑检查组
    @RequestMapping("edit.do")
    public  Result edit(@RequestBody CheckGroup checkGroup,Integer[] checkItemIds) {
        try {
           checkGroupService.edit(checkGroup,checkItemIds);

        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }
        return new Result(true, MessageConstant.EDIT_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("delete.do")
    public  Result delete(@RequestBody Integer id) {
        try {
            checkGroupService.delete(id);

        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
        }
        return new Result(true, MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }



    @RequestMapping("findAll.do")
    public  Result findAll() {
        List<CheckGroup> list=null;
        try {
        list = checkGroupService.findAll();

        } catch (Exception e) {
            e.printStackTrace();

            return new Result(false, MessageConstant.QUERY_CHECKGROUP_SUCCESS);
        }
        return new Result(  true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
    }
}
