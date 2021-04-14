package com.guigu.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.guigu.dao.CheckGroupDao;
import com.guigu.service.CheckGroupService;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 检查组服务
 */

@Service("checkGroupService")
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    //注入dao
    @Autowired
    private CheckGroupDao checkGroupDao;
    //新增检查组，同时需要让检查项关联
    @Override
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组，操作t_checkgroup  表
            checkGroupDao.add(checkGroup);
        //设置检查组跟检查项 多对多的关联关系
        Integer checkGroupId = checkGroup.getId();
        if (checkitemIds!=null&&checkitemIds.length>0)
        for ( Integer checkitemId:checkitemIds
             ) {
            Map<String,Integer> map=new HashMap<>();
            map.put("checkGroupId",checkGroupId);
            map.put("checkitemId",checkitemId);
            checkGroupDao.setCheckGroupIdAndCheckItemId(map);
        }

    }


    //分页查询
    @Override
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        String queryString = queryPageBean.getQueryString();
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        //mybatis插件分页
        PageHelper.startPage(currentPage,pageSize);
      Page<CheckGroup> page= checkGroupDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //根据id查询检查项
    @Override
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }


    //根据检查组查询检查项ID
    @Override
    public List<Integer> findCheckitemIdsByCheckgroupId(Integer id) {
        return checkGroupDao.findCheckitemIdsByCheckgroupId(id);
    }


    //编辑检查组同时关联检查项
    @Override
    public void edit(CheckGroup checkGroup, Integer []checkitemIds) {
        //修改检查组，操作检查组表
        checkGroupDao.edit(checkGroup);
        //清理检查组关联检查项的表
        Integer checkGroupId = checkGroupDao.clear(checkGroup.getId());
        //重新建立当前检查组 和检查项的关联关系
        if (checkitemIds!=null && checkitemIds.length>0)
            for ( Integer checkitemId:checkitemIds
            ) {
                Map<String,Integer> map=new HashMap<>();
                map.put("checkGroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupIdAndCheckItemId(map);
            }
    }

    @Override
    public void delete(Integer id) {
        checkGroupDao.delete(id);
    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }
}
