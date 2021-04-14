package com.guigu.service;

import com.health.entity.PageResult;
import com.health.entity.QueryPageBean;
import com.health.pojo.Address;

import java.util.List;

public interface AddressService {
    PageResult findPage(QueryPageBean queryPageBean);

    void add(Address address);

    long findByAddressId(Integer id);

    void edit(Address address);

    void delete(Integer id);

    Address findUpdate(Integer id);

    List<Address> findAll();

    Address findById(Integer addressId);

    Address getByMemberId(Integer id);
}

