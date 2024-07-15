package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Address;
import com.it.mapper.AddressMapper;
import com.it.service.AddressService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 *
 */
@Service
public class AddressServiceImpl implements AddressService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private AddressMapper AddressMapper;

    @Override
    public Page<Address> selectPage(Address entity, int page, int limit) {
        EntityWrapper<Address> searchInfo = new EntityWrapper<>();
        Page<Address> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserId())) {
            searchInfo.eq("userId", entity.getUserId());
        }
        List<Address> resultList = AddressMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Address entity) {
        if (!ItdragonUtils.stringIsNotBlack(entity.getIsDefault())) {
            entity.setIsDefault("0");
        } else {
            //将该用户的默认地址改为非默认地址
            EntityWrapper<Address> wrapper = new EntityWrapper<>();
            wrapper.eq("userId", itdragonUtils.getSessionUserId());
            wrapper.eq("isDefault", "1");
            Address address1 = new Address();
            address1.setIsDefault("0");
            AddressMapper.update(address1, wrapper);
        }
        entity.setUserId(itdragonUtils.getSessionUserId());
        Integer insert = AddressMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Address entity) {
        entity.setIsDefault("1");
        //将该用户的默认地址改为非默认地址
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        wrapper.eq("isDefault", "1");
        Address address1 = new Address();
        address1.setIsDefault("0");
        AddressMapper.update(address1, wrapper);
        Integer insert = AddressMapper.updateById(entity);
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
            result = AddressMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Address> getList(Address entity) {
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getUserId())) {
            wrapper.eq("userId", entity.getUserId());
        }
        List<Address> resultList = AddressMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<Address> getList() {
        EntityWrapper<Address> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", itdragonUtils.getSessionUserId());
        List<Address> resultList = AddressMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Address getOne(String id) {
        Address Address = AddressMapper.selectById(id);
        if (Address != null) {
            return Address;
        } else {
            return new Address();
        }
    }
}