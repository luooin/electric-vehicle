package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.ShopCart;
import com.it.mapper.ShopCartMapper;
import com.it.service.ShopCartService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Service
public class ShopCartServiceImpl implements ShopCartService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private ShopCartMapper shopCartMapper;

    @Override
    public Page<ShopCart> selectPage(ShopCart entity, int page, int limit) {
        EntityWrapper<ShopCart> searchInfo = new EntityWrapper<>();
        Page<ShopCart> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserId())) {
            searchInfo.eq("userId", entity.getUserId());
        }
        List<ShopCart> resultList = shopCartMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(ShopCart entity) {
        entity.setUserId(itdragonUtils.getSessionUserId());
        Integer insert = shopCartMapper.insert(entity);
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
            result = shopCartMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ShopCart> getList(ShopCart entity) {
        EntityWrapper<ShopCart> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getUserId())) {
            wrapper.eq("userId", entity.getUserId());
        }
        List<ShopCart> resultList = shopCartMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<ShopCart> getListByUserId(String userId) {
        EntityWrapper<ShopCart> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(userId)) {
            wrapper.eq("userId", userId);
        }
        List<ShopCart> resultList = shopCartMapper.selectList(wrapper);
        return resultList;

    }


    @Override
    public ShopCart getOne(String id) {
        return shopCartMapper.selectById(id);
    }


}