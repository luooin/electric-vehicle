package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Address;

import java.util.List;

public interface AddressService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Address> selectPage(Address entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Address entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Address entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);

    /**
     * 获取集合
     *
     * @param entity
     * @return
     */
    List<Address> getList(Address entity);

    /**
     * 获取当前用户的所有收货地址
     * @param
     * @return
     */
    List<Address> getList();

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Address getOne(String id);

}
