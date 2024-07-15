package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Notice;
import com.it.mapper.NoticeMapper;
import com.it.service.NoticeService;
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
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private NoticeMapper NoticeMapper;

    @Override
    public Page<Notice> selectPage(Notice entity, int page, int limit) {
        EntityWrapper<Notice> searchInfo = new EntityWrapper<>();
        Page<Notice> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getTitle())) {
            searchInfo.eq("title", entity.getTitle());
        }
        searchInfo.orderBy("time desc");
        List<Notice> resultList = NoticeMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Notice entity) {
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = NoticeMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Notice entity) {
        Integer insert = NoticeMapper.updateById(entity);
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
            result = NoticeMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Notice> getList(Notice entity) {
        EntityWrapper<Notice> wrapper = new EntityWrapper<>();

        List<Notice> resultList = NoticeMapper.selectList(wrapper);
        return resultList;
    }


    @Override
    public Notice getOne(String id) {
        Notice Notice = NoticeMapper.selectById(id);
        if (Notice != null) {
            return Notice;
        } else {
            return new Notice();
        }
    }
}