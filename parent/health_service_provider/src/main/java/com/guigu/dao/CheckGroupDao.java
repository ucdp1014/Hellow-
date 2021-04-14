package com.guigu.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckGroup;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface CheckGroupDao {
    public  void add(CheckGroup checkGroup);

    public  void setCheckGroupIdAndCheckItemId(Map map);

    public Page<CheckGroup> selectByCondition(String queryPage);

    public  CheckGroup findById(Integer id);

    public List<Integer> findCheckitemIdsByCheckgroupId(Integer id);

    public  void edit(CheckGroup checkGroup);

    public   Integer clear(Integer id);

    public  void delete (Integer id);

    List<CheckGroup> findAll();
}
