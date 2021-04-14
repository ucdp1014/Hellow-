package com.guigu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.guigu.dao.CheckItemDao;
import com.guigu.service.CheckItemService;
import com.health.entity.CheckItemDeleteException;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.CheckItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;



@Service("checkItemService")
@Transactional
public class CheckItemServiceImpl  implements CheckItemService {
    //注入dao对象
    @Autowired
    private CheckItemDao checkItemDao;

    @Override
    //新增
    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }


    //分页查询
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        //使用分页插件分页查询
        PageHelper.startPage(currentPage,pageSize);
        //调用持久层查询所有
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        //获取总共查询到的条目数
        long total = page.getTotal();
        //获取查询结果
        List<CheckItem> rows = page.getResult();
        return new PageResult(total,rows);
    }



    //根据id删除检查项
    @Override
        public void deltetByid(Integer id) throws CheckItemDeleteException {
        //判断检查项是否关联检查组
        //先查询有无关联关系
        long count=checkItemDao.findCountCheckItemID(id);
        //如何有关联关系不进行删除操作
        System.out.println("count"+count);
        if (count>0){
            throw new CheckItemDeleteException();
        }
        //如果没有关联关系直接执行删除操作
        checkItemDao.deltetByid(id);
    }

    @Override
    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
