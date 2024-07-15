package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Collect;
import com.it.entity.Evaluate;
import com.it.entity.Product;
import com.it.mapper.CollectMapper;
import com.it.mapper.EvaluateMapper;
import com.it.mapper.ProductMapper;
import com.it.service.ProductService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Service
public class ProductServiceImpl implements ProductService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private EvaluateMapper evaluateMapper;
    @Resource
    private CollectMapper collectMapper;

    @Override
    public Page<Product> selectPage(Product entity, int page, int limit) {
        EntityWrapper<Product> searchInfo = new EntityWrapper<>();
        Page<Product> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.like("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getSellerId())) {
            searchInfo.like("sellerId", entity.getSellerId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getCategoryId())) {
            searchInfo.eq("categoryId", entity.getCategoryId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getPromotion())) {
            searchInfo.eq("promotion", entity.getPromotion());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getParentCategoryId())) {
            searchInfo.eq("parentCategoryId", entity.getParentCategoryId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getUuId())) {
            searchInfo.like("uuId", entity.getUuId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getState())) {
            searchInfo.eq("state", entity.getState());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.like("time", entity.getTime());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getOrderFiled())) {
            searchInfo.orderBy(entity.getOrderFiled() + " desc");
        } else {
            searchInfo.orderBy("time desc");
        }
        List<Product> resultList = productMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Product entity) {
        //添加车辆录入时间
        entity.setTime(DateUtil.getNowDateSS());
        //添加车辆收藏数
        entity.setCollectNum(0);
        //促销推荐初始状态
        entity.setPromotion("0");
        //车辆初始状态
        entity.setState("0");
        //添加车辆唯一编码
        entity.setUuId(itdragonUtils.getUUId());
        entity.setSellerId(itdragonUtils.getSessionUserId());
        Integer insert = productMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean insert(Collect entity) {
        entity.setUserId(itdragonUtils.getSessionUserId());
        Integer insert = collectMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Product entity) {
        Integer insert = productMapper.updateById(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = productMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean deleteCollectById(String id) {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        wrapper.eq("productId", id);
        Integer delete = collectMapper.delete(wrapper);
        if (delete > 0) {
            return true;
        }
        return false;

    }

    @Override
    public List<Product> getList(Product entity) {
        EntityWrapper<Product> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            wrapper.like("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getCategoryId())) {
            wrapper.eq("categoryId", entity.getCategoryId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getState())) {
            wrapper.eq("state", entity.getState());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getSellerId())) {
            wrapper.eq("sellerId", entity.getSellerId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getParentCategoryId())) {
            wrapper.eq("parentCategoryId", entity.getParentCategoryId());
        }
        List<Product> resultList = productMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<Collect> getList() {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        List<Collect> resultList = collectMapper.selectList(wrapper);
        return resultList;

    }


    @Override
    public Product getOne(String id) {
        Product product = productMapper.selectById(id);
        if (product != null) {
            return product;
        }
        return new Product();
    }

    @Override
    public List<Evaluate> getListByProductId(String productId) {
        EntityWrapper<Evaluate> wrapper = new EntityWrapper<>();
        wrapper.eq("productId", productId);
        wrapper.orderBy("time desc");
        List<Evaluate> evaluateList = evaluateMapper.selectList(wrapper);
        return evaluateList;
    }

    @Override
    public boolean editSaleById(String id, Integer num) {
        Product product = productMapper.selectById(id);
        product.setInventory(product.getInventory() - num);
        Integer update = productMapper.updateById(product);
        if (update > 0) {
            return true;
        }
        return false;

    }

    @Override
    public boolean editInById(String id, Integer num) {
        Product product = productMapper.selectById(id);
        Integer update = productMapper.updateById(product);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editCollectById(String id, Integer num) {
        Product product = productMapper.selectById(id);
        product.setCollectNum(product.getCollectNum() + num);
        Integer update = productMapper.updateById(product);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isCollect(String productId) {
        EntityWrapper<Collect> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        wrapper.eq("productId", productId);
        List<Collect> resultList = collectMapper.selectList(wrapper);
        if (resultList.isEmpty()) {
            return false;
        }
        return true;
    }


}