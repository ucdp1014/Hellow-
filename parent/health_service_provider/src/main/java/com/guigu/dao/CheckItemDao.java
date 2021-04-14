package com.guigu.dao;

import com.github.pagehelper.Page;
import com.health.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CheckItemDao {

    public  void add(CheckItem checkItem);

    public Page<CheckItem> selectByCondition (String queryString) ;

    public  long  findCountCheckItemID(Integer id);

    public void deltetByid(Integer id);

    public void  edit(CheckItem checkItem);

    public List<CheckItem> findAll();
}
