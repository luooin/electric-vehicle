package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Evaluate;
import com.it.entity.Logistics;
import com.it.entity.Order;
import com.it.entity.Refund;

import java.util.List;

public interface OrderService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Order> selectPage(Order entity, int page, int limit);

    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Refund> selectPage(Refund entity, int page, int limit);

    Page<Evaluate> selectPage(Evaluate entity, int page, int limit);

    /**
     * 新增订单
     *
     * @param entity
     * @return
     */
    boolean insert(Order entity);

    /**
     * 新增归还申请
     *
     * @param entity
     * @return
     */
    boolean insert(Refund entity);

    /**
     * 编辑订单
     *
     * @param entity
     * @return
     */
    boolean edit(Order entity);

    boolean editByAliNo(String aliNo);

    /**
     * 编辑归还申请
     *
     * @param entity
     * @return
     */
    boolean edit(Refund entity);

    /**
     * 编辑评价信息
     *
     * @param entity
     * @return
     */
    boolean edit(Evaluate entity);

    /**
     * 获取单个对象
     *
     * @param id
     * @return
     */
    Order getOne(String id);

    /**
     * 获取评价的单个对象
     *
     * @param id
     * @return
     */
    Evaluate getEvaluate(String id);

    /**
     * 修改订单状态
     *
     * @param orderId
     * @param state
     * @return
     */
    boolean editState(String orderId, String state);

    /**
     * 新增物流信息
     *
     * @param entity
     * @return
     */
    boolean insert(Logistics entity);

    /**
     * 新增评价
     *
     * @param entity
     * @return
     */
    boolean insert(Evaluate entity);

    /**
     * 查询订单下的物流信息
     *
     * @param orderId
     * @return
     */
    List<Logistics> getListByOrderId(String orderId);

    /**
     * 获取订单集合
     *
     * @return
     */
    List<Order> getOrderList();

    List<Order> getOrderList(String year, String state);

    /**
     * 删除订单
     *
     * @param id
     * @return
     */
    boolean deleteOrder(String id);

    /**
     * 删除归还申请
     *
     * @param id
     * @return
     */
    boolean deleteRefund(String id);

    /**
     * 获取各个订单的数目
     *
     * @param state
     * @return
     */
    Integer orderNum(String state);

    Float getOrderPrice();


}
