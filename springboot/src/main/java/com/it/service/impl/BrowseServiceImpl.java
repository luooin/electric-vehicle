package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.it.entity.BrowseRecord;
import com.it.mapper.BrowseRecordMapper;
import com.it.service.BrowseService;
import com.it.util.DateUtil;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BrowseServiceImpl implements BrowseService {
    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private BrowseRecordMapper browseMapper;


    @Override
    public boolean insert(String userId, String productId) {
        //首先查询该用户是否对该车辆已有浏览记录
        EntityWrapper<BrowseRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("itemId", productId);
        List<BrowseRecord> browseRecords = browseMapper.selectList(wrapper);
        if (browseRecords.isEmpty()) {
            //如果没有浏览记录
            BrowseRecord browseRecord = new BrowseRecord();
            browseRecord.setRating(1);
            browseRecord.setUserId(userId);
            browseRecord.setItemId(productId);
            browseRecord.setTime(DateUtil.getNowDateSS());
            Integer insert = browseMapper.insert(browseRecord);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            BrowseRecord browseRecord = browseRecords.get(0);
            browseRecord.setRating(browseRecord.getRating() + 1);
            browseRecord.setTime(DateUtil.getNowDateSS());
            Integer insert = browseMapper.updateById(browseRecord);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public List<BrowseRecord> getList() {
        List<BrowseRecord> browseRecords = browseMapper.selectList(null);
        return browseRecords;
    }

    @Override
    public List<BrowseRecord> getListMy() {
        EntityWrapper<BrowseRecord> wrapper = new EntityWrapper<>();
        wrapper.eq("userId",itdragonUtils.getSessionUserId());
        List<BrowseRecord> browseRecords = browseMapper.selectList(wrapper);
        return browseRecords;
    }

    @Override
    public boolean deleteById(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = browseMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }
}
