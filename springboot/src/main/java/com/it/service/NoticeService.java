package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Notice;

import java.util.List;

public interface NoticeService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Notice> selectPage(Notice entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Notice entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Notice entity);

    /**
     * 删除
     *
     * @param ids
     * @return
     */

    boolean deleteById(String ids);

    /**
     * 获取集合
     *
     * @param entity
     * @return
     */
    List<Notice> getList(Notice entity);


    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Notice getOne(String id);

}
