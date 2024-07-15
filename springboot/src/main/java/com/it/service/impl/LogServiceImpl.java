package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Log;
import com.it.mapper.LogMapper;
import com.it.service.LogService;
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
public class LogServiceImpl implements LogService {
    @Resource
    private LogMapper LogMapper;
    @Resource
    private ItdragonUtils itdragonUtils;

    @Override
    public Page<Log> selectPage(Log Log, int page, int limit) {
        EntityWrapper<Log> searchInfo = new EntityWrapper<>();
        Page<Log> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(Log.getUserName())) {
            searchInfo.eq("userName", Log.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(Log.getOperation())) {
            searchInfo.like("operation", Log.getOperation());
        }
        if (ItdragonUtils.stringIsNotBlack(Log.getTime())) {
            searchInfo.between("time", Log.getTime().split(" - ")[0], Log.getTime().split(" - ")[1]);
        }
        searchInfo.orderBy("time desc");
        List<Log> resultList = LogMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public List<Log> selectList(String operation) {
        EntityWrapper<Log> searchInfo = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(operation)) {
            searchInfo.like("operation", operation);
        }
        return LogMapper.selectList(searchInfo);
    }

    @Override
    public boolean insert(String operation,String Ip) {
        Log log = new Log();
        log.setOperation(operation);
        log.setTime(DateUtil.getNowDateSS());
        log.setIp(Ip);
        log.setUserName(itdragonUtils.getSessionUser().getUserName());
        Integer insert = LogMapper.insert(log);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String ids) {
        String[] idList = ids.split(",");
        int result = 0;
        for (String s : idList) {
            result = LogMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }

    }
}