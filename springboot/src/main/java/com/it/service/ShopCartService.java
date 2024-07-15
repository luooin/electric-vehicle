package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.ShopCart;

import java.util.List;

public interface ShopCartService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<ShopCart> selectPage(ShopCart entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(ShopCart entity);


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
    List<ShopCart> getList(ShopCart entity);

    /**
     * 通过userId获取集合
     * @param userId
     * @return
     */
    List<ShopCart> getListByUserId(String userId);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    ShopCart getOne(String id);

}
