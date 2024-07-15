package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Slideshow;

import java.util.List;

public interface SlideshowService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Slideshow> selectPage(Slideshow entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Slideshow entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Slideshow entity);

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
    List<Slideshow> getList(Slideshow entity);
    /**
     * 通过类型获取集合
     *
     * @param type
     * @return
     */
    List<Slideshow> getListByType(String type);

    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Slideshow getOne(String id);

}
