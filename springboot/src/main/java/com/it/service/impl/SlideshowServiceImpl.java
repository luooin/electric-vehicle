package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Slideshow;
import com.it.mapper.SlideshowMapper;
import com.it.service.SlideshowService;
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
public class SlideshowServiceImpl implements SlideshowService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private SlideshowMapper SlideshowMapper;

    @Override
    public Page<Slideshow> selectPage(Slideshow entity, int page, int limit) {
        EntityWrapper<Slideshow> searchInfo = new EntityWrapper<>();
        Page<Slideshow> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getProductId())) {
            searchInfo.eq("productId", entity.getProductId());
        }
        searchInfo.orderBy("time desc");
        List<Slideshow> resultList = SlideshowMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Slideshow entity) {
        entity.setTime(DateUtil.getNowDateSS());
        entity.setUserName(itdragonUtils.getSessionUserName());
        Integer insert = SlideshowMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Slideshow entity) {
        Integer insert = SlideshowMapper.updateById(entity);
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
            result = SlideshowMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Slideshow> getList(Slideshow entity) {
        EntityWrapper<Slideshow> wrapper = new EntityWrapper<>();

        wrapper.orderBy("time desc");
        List<Slideshow> resultList = SlideshowMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<Slideshow> getListByType(String type) {
        EntityWrapper<Slideshow> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(type)) {
            wrapper.eq("type", type);
        }
        wrapper.eq("state", 1);
        wrapper.orderBy("time desc");
        List<Slideshow> resultList = SlideshowMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public Slideshow getOne(String id) {
        return SlideshowMapper.selectById(id);
    }


}