package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Leave;
import com.it.entity.Reply;
import com.it.mapper.LeaveMapper;
import com.it.mapper.ReplyMapper;
import com.it.service.LeaveService;
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
public class LeaveServiceImpl implements LeaveService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private LeaveMapper leaveMapper;
    @Resource
    private ReplyMapper replyMapper;


    @Override
    public Page<Leave> selectPage(Leave entity, int page, int limit) {
        EntityWrapper<Leave> searchInfo = new EntityWrapper<>();
        Page<Leave> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.like("time", entity.getTime());
        }
        List<Leave> resultList = leaveMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public Page<Reply> selectPage(Reply entity, int page, int limit) {
        EntityWrapper<Reply> searchInfo = new EntityWrapper<>();
        Page<Reply> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getUserName())) {
            searchInfo.eq("userName", entity.getUserName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getLeaveId())) {
            searchInfo.eq("leaveId", entity.getLeaveId());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getTime())) {
            searchInfo.like("time", entity.getTime());
        }
        List<Reply> resultList = replyMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;

    }

    @Override
    public boolean insert(Leave entity) {
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setUserImg(itdragonUtils.getSessionUserImg());
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = leaveMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        Integer delete = leaveMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Leave> getList(String userName) {
        EntityWrapper<Leave> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(userName)) {
            wrapper.eq("userName", userName);
        }
        wrapper.orderBy("time desc");
        List<Leave> resultList = leaveMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<Reply> getListReply(String userName) {
        EntityWrapper<Reply> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(userName)) {
            wrapper.eq("userName", userName);
        }
        wrapper.orderBy("time desc");
        List<Reply> resultList = replyMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public boolean insert(Reply entity) {
        entity.setUserName(itdragonUtils.getSessionUserName());
        entity.setUserImg(itdragonUtils.getSessionUserImg());
        entity.setTime(DateUtil.getNowDateSS());
        Integer insert = replyMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Reply> selectListReply(String leaveId) {
        EntityWrapper<Reply> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(leaveId)) {
            wrapper.eq("leaveId", leaveId);
        }
        wrapper.orderBy("time desc");
        List<Reply> resultList = replyMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public boolean delReplyById(String id) {
        Integer delete = replyMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }
}