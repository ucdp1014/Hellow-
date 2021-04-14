package com.guigu.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupService {
    public  void add(CheckGroup checkGroup,Integer[] checkitemIds);

    public PageResult pageQuery(QueryPageBean queryPageBean);

    public  CheckGroup findById(Integer id);

    public List<Integer> findCheckitemIdsByCheckgroupId(Integer id);

    public  void edit(CheckGroup checkGroup,Integer[] checkitemIds);

    public  void delete(Integer id);

    List<CheckGroup> findAll();
}
