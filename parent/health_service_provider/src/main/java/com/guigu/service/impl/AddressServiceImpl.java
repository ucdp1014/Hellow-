package com.guigu.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.guigu.service.AddressService;
import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Address;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("addressService")
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    private com.guigu.dao.AddressDao AddressDao;


    /**
     * 分页查询所有机构信息功能
     * @param queryPageBean
     * @return com.itheima.entity.PageResult
     * @author JANME
     * @date 2020-09-22 19:03
     */
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        //获取当前页的页码
        Integer currentPage = queryPageBean.getCurrentPage();
        //获取每页显示条目数
        Integer pageSize = queryPageBean.getPageSize();
        //获取查询条件
        String queryString = queryPageBean.getQueryString();
        //对查询条件进行 模糊查询
        if (queryString != null && queryString.length() > 0) {
            queryString = "%" + queryString + "%";

            //使用分页插件进行分页
            PageHelper.startPage(currentPage, pageSize);
            Page<Address> addressPage = AddressDao.findByCondition(queryString);

            //获取集合
            List<Address> addressList = addressPage.getResult();
            //获取数据总条目数
            long total = addressPage.getTotal();
            return new PageResult(total, addressList);
        }

        //使用分页插件进行分页
        PageHelper.startPage(currentPage, pageSize);
        Page<Address> addressPage = AddressDao.findPage();

        //获取集合
        List<Address> addressList = addressPage.getResult();
        //获取数据总条目数
        long total = addressPage.getTotal();
        return new PageResult(total, addressList);
    }

    /**新增体检机构
     * @param address
     * @return void
     * @author JANME
     * @date 2020-09-22 19:03
     */
    @Override
    public void add(Address address) {
        AddressDao.add(address);
    }

    @Override
    public long findByAddressId(Integer id) {
        return AddressDao.findByAddressId(id);
    }

    @Override
    public void edit(Address address) {
        AddressDao.edit(address);
    }

    @Override
    public void delete(Integer id) {
        AddressDao.delete(id);
    }

    @Override
    public Address findUpdate(Integer id) {
        return AddressDao.findUpdate(id);
    }

    @Override
    public List<Address> findAll() {
        return AddressDao.findAll();
    }

    @Override
    public Address findById(Integer addressId) {
        return AddressDao.findById(addressId);
    }

    //通过用户id查询关联表获得对应的体检机构
    @Override
    public Address getByMemberId(Integer id) {
        return AddressDao.getByMemberId(id);
    }
}
