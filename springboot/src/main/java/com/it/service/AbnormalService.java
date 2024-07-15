package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Abnormal;

import java.util.List;

public interface AbnormalService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Abnormal> selectPage(Abnormal entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Abnormal entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Abnormal entity);

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
    List<Abnormal> getList(Abnormal entity);


    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Abnormal getOne(String id);

}
