package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Evaluate;

import java.util.List;

public interface EvaluateService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Evaluate> selectPage(Evaluate entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Evaluate entity);


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
    List<Evaluate> getList(Evaluate entity);

    Integer num(String itemId);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Evaluate getOne(String id);

}
