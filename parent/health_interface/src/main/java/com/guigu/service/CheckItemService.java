package com.guigu.service;

import com.health.entity.CheckItemDeleteException;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;

import java.util.List;

//服务接口
public interface CheckItemService {

    public  void add(CheckItem checkItem);

    public PageResult findPage(QueryPageBean queryPageBean);

    public  void  deltetByid(Integer id) throws Exception;

    public void  edit(CheckItem checkItem);

    public List<CheckItem> findAll();

}
