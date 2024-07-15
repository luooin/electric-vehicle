package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Abnormal;
import com.it.mapper.AbnormalMapper;
import com.it.service.AbnormalService;
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
public class AbnormalServiceImpl implements AbnormalService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private AbnormalMapper AbnormalMapper;

    @Override
    public Page<Abnormal> selectPage(Abnormal entity, int page, int limit) {
        EntityWrapper<Abnormal> searchInfo = new EntityWrapper<>();
        Page<Abnormal> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getState())) {
            searchInfo.eq("state", entity.getState());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getSellName())) {
            searchInfo.eq("sellName", entity.getSellName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getType())) {
            searchInfo.eq("type", entity.getType());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.like("time", entity.getTime());
        }
        searchInfo.orderBy("time desc");
        List<Abnormal> resultList = AbnormalMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Abnormal entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setState("0");
        Integer insert = AbnormalMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Abnormal entity) {
        Integer insert = AbnormalMapper.updateById(entity);
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
            result = AbnormalMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Abnormal> getList(Abnormal entity) {
        EntityWrapper<Abnormal> wrapper = new EntityWrapper<>();

        List<Abnormal> resultList = AbnormalMapper.selectList(wrapper);
        return resultList;
    }


    @Override
    public Abnormal getOne(String id) {
        Abnormal Abnormal = AbnormalMapper.selectById(id);
        if (Abnormal != null) {
            return Abnormal;
        } else {
            return new Abnormal();
        }
    }
}