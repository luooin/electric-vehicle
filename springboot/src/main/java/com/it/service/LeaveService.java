package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Leave;
import com.it.entity.Reply;

import java.util.List;

public interface LeaveService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Leave> selectPage(Leave entity, int page, int limit);
    Page<Reply> selectPage(Reply entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Leave entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);

    /**
     * 获取留言集合
     *
     * @param
     * @return
     */
    List<Leave> getList(String userName);
    List<Reply> getListReply(String userName);
    /**
     * 新增回复
     *
     * @param entity
     * @return
     */
    boolean insert(Reply entity);

    /**
     * 得到留言回复集合
     *
     * @return
     */
    List<Reply> selectListReply(String leaveId);

    /**
     * 删除回复
     */
    boolean delReplyById(String id);

}
