package com.guigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guigu.service.SetmealService;
import com.health.constant.MessageConstant;
import com.health.constant.RedisConstant;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.entity.Result;
import com.health.pojo.Setmeal;
import com.health.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * 体检套餐
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {


    @Reference
    private SetmealService setmealService;


    //使用jeedis操作Redis服务
    @Autowired
    private JedisPool jedisPool;
    //新增
    @RequestMapping("/upload.do")
    public Result upload(@RequestParam("imgFile")MultipartFile imgFile){

        //获取后缀
        String originalFilename = imgFile.getOriginalFilename();
        int index = originalFilename.lastIndexOf(".");
        String entention = originalFilename.substring(index - 1);//.jpg
        System.out.println(entention);
        //获取随机文件名
        String fileName= UUID.randomUUID().toString()+entention;
        System.out.println(fileName);
        try {
            //上传七牛云
            QiniuUtils.upload2Qiniu(imgFile.getBytes(),fileName);
            //将图片上传到七牛云的同时将图片名称存入Redis大集合中
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,fileName);
        } catch (IOException e) {
            e.printStackTrace();
            //上传失败
            return  new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,fileName);
    }

    @RequestMapping("/add.do")
    public Result add(@RequestBody Setmeal setmeal,Integer[] CheckGroupIds){

                try{
                    setmealService.add(setmeal,CheckGroupIds);

                }catch (Exception e){
                    e.printStackTrace();
                    return new Result(false,MessageConstant.ADD_SETMEAL_FAIL);
                }
                     return new Result(true,MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findPage.do")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {


        return setmealService.findPage(queryPageBean);
    }

    //根据id查询检查组
    @RequestMapping("/findIdInMiddleTable.do")
    public Result findIdInMiddleTable(String setMealId){
        try {
            List<String> checkgroupIds=setmealService.findIdInMiddleTable(setMealId);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }


    //修改套餐
    @RequestMapping("/edit.do")
    public Result edit(@RequestBody Setmeal setmeal, Integer[] checkgroupIds ,String tempImgId){
        try {
            setmealService.edit(setmeal,checkgroupIds,tempImgId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    //删除套餐
    @RequestMapping("/delete.do")
    public Result delete(Integer setMealId,String imgId){
        try {
            setmealService.delete(setMealId,imgId);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false, MessageConstant.DELETE_SETMEAL_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_SETMEAL_SUCCESS);
    }

    @RequestMapping("/findAllSetmeal.do")
    public Result findAllSetmeal() {
        try {
            List<Setmeal> list = setmealService.findAllSetmeal();
            return new Result(true,MessageConstant.QUERY_SETMEALLIST_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_SETMEALLIST_FAIL);
        }
    }
}
