package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Evaluate;
import com.it.entity.Logistics;
import com.it.entity.Order;
import com.it.entity.Refund;
import com.it.mapper.EvaluateMapper;
import com.it.mapper.LogisticsMapper;
import com.it.mapper.OrderMapper;
import com.it.mapper.RefundMapper;
import com.it.service.OrderService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private OrderMapper OrderMapper;
    @Resource
    private LogisticsMapper logisticsMapper;
    @Resource
    private EvaluateMapper evaluateMapper;
    @Resource
    private RefundMapper refundMapper;

    @Override
    public Page<Order> selectPage(Order entity, int page, int limit) {
        EntityWrapper<Order> searchInfo = new EntityWrapper<>();
        Page<Order> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserId())) {
            searchInfo.eq("userId", entity.getUserId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUuId())) {
            searchInfo.like("uuId", entity.getUuId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getSellerId())) {
            searchInfo.like("sellerId", entity.getSellerId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getConsigneeInfo())) {
            searchInfo.like("consigneeInfo", entity.getConsigneeInfo());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getState())) {
            searchInfo.like("state", entity.getState());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getProductName())) {
            searchInfo.like("productName", entity.getProductName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getIsDelete())) {
            searchInfo.like("isDelete", entity.getIsDelete());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getIsDeleteSeller())) {
            searchInfo.like("isDeleteSeller", entity.getIsDeleteSeller());
        }
        searchInfo.orderBy("time desc");
        List<Order> resultList = OrderMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public Page<Refund> selectPage(Refund entity, int page, int limit) {
        EntityWrapper<Refund> searchInfo = new EntityWrapper<>();
        Page<Refund> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getOrderId())) {
            searchInfo.eq("orderId", entity.getOrderId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getSellerId())) {
            searchInfo.eq("sellerId", entity.getSellerId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getState())) {
            searchInfo.like("state", entity.getState());
        }
        searchInfo.orderBy("time desc");
        List<Refund> resultList = refundMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;

    }

    @Override
    public Page<Evaluate> selectPage(Evaluate entity, int page, int limit) {
        EntityWrapper<Evaluate> searchInfo = new EntityWrapper<>();
        Page<Evaluate> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getProductId())) {
            searchInfo.eq("productId", entity.getProductId());
        }
        searchInfo.orderBy("time desc");
        List<Evaluate> resultList = evaluateMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;

    }

    @Override
    public boolean insert(Order entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setState("0");
        entity.setIsDelete("0");
        entity.setIsDeleteSeller("0");
        entity.setAliNo("-");
        entity.setSaleUser(itdragonUtils.getSessionUserName());
        entity.setUuId(itdragonUtils.getOrderIdByUUId());
        entity.setUserId(itdragonUtils.getSessionUserId());
        Integer insert = OrderMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(Refund entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setState("0");
        Integer insert = refundMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;

    }

    @Override
    public boolean edit(Order entity) {
        Integer insert = OrderMapper.updateById(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editByAliNo(String aliNo) {
        Order order = new Order();
        order.setState("1");
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.eq("aliNo", aliNo);
        Integer update = OrderMapper.update(order, wrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean edit(Refund entity) {
        Integer insert = refundMapper.updateById(entity);
        if (insert > 0) {
            return true;
        }
        return false;

    }

    @Override
    public boolean edit(Evaluate entity) {
        Integer insert = evaluateMapper.updateById(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Order getOne(String id) {
        Order order = OrderMapper.selectById(id);
        if (order != null) {
            return order;
        }
        return new Order();
    }

    @Override
    public Evaluate getEvaluate(String id) {
        Evaluate evaluate = evaluateMapper.selectById(id);
        if (evaluate != null) {
            return evaluate;
        }
        return new Evaluate();
    }

    @Override
    public boolean editState(String orderId, String state) {
        Order order = new Order();
        order.setId(orderId);
        order.setState(state);
        Integer update = OrderMapper.updateById(order);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(Logistics entity) {
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = logisticsMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(Evaluate entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setUserImg(itdragonUtils.getSessionUserImg());
        Integer insert = evaluateMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Logistics> getListByOrderId(String orderId) {
        EntityWrapper<Logistics> wrapper = new EntityWrapper<>();
        wrapper.eq("orderId", orderId);
        wrapper.orderBy("time desc");
        List<Logistics> logisticsList = logisticsMapper.selectList(wrapper);
        return logisticsList;
    }

    @Override
    public List<Order> getOrderList() {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.orderBy("time desc");
        List<Order> orderList = OrderMapper.selectList(wrapper);
        return orderList;

    }


    @Override
    public List<Order> getOrderList(String year, String state) {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.like("time ", year);
        if (ItdragonUtils.stringIsNotBlack(state)) {
            wrapper.eq("state ", state);
        }
        List<Order> orderList = OrderMapper.selectList(wrapper);
        return orderList;
    }

    @Override
    public boolean deleteOrder(String id) {
        Integer delete = OrderMapper.deleteById(id);
        if (delete > 0) {
            //删除成功后删除对应的订单的物流信息
            EntityWrapper<Logistics> wrapper = new EntityWrapper<>();
            wrapper.eq("orderId", id);
            logisticsMapper.delete(wrapper);
            return true;
        }
        return false;

    }

    @Override
    public boolean deleteRefund(String id) {
        Integer delete = refundMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer orderNum(String state) {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        wrapper.eq("state", state);
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        return OrderMapper.selectList(wrapper).size();
    }

    @Override
    public Float getOrderPrice() {
        EntityWrapper<Order> wrapper = new EntityWrapper<>();
        List<Order> orderList = OrderMapper.selectList(wrapper);
        Float zum = Float.valueOf(0);
        for (Order order : orderList) {
            zum += order.getUnitPrice();
        }
        return zum;
    }
}