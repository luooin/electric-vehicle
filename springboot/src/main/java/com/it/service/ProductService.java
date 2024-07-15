package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Collect;
import com.it.entity.Evaluate;
import com.it.entity.Product;

import java.util.List;

public interface ProductService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Product> selectPage(Product entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Product entity);

    /**
     * 新增收藏
     * @param entity
     * @return
     */
    boolean insert(Collect entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Product entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);
    /**
     * 删除收藏
     *
     * @param ids
     * @return
     */
    boolean deleteCollectById(String ids);
    /**
     * 获取集合
     *
     * @param entity
     * @return
     */
    List<Product> getList(Product entity);

    /**
     * 获取收藏列表
     * @return
     */
    List<Collect> getList();

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Product getOne(String id);
    /**
     * 查询车辆下的评价信息
     * @param productId
     * @return
     */
    List<Evaluate> getListByProductId(String productId);

    /**
     * 销量修改
     * @param id
     * @param num
     * @return
     */
    boolean editSaleById(String id,Integer num);
    boolean editInById(String id,Integer num);
    /**
     * 收藏数修改
     * @param id
     * @param num
     * @return
     */
    boolean editCollectById(String id,Integer num);
    /**
     * 验证当前用户是否收藏该车辆
     *
     * @param productId
     * @return
     */
    boolean isCollect(String productId);
}
