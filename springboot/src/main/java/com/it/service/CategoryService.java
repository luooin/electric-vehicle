package com.it.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    /**
     * 分页查询
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    Page<Category> selectPage(Category entity, int page, int limit);

    /**
     * 新增
     *
     * @param entity
     * @return
     */
    boolean insert(Category entity);

    /**
     * 编辑
     *
     * @param entity
     * @return
     */
    boolean editById(Category entity);

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
    List<Category> getList(Category entity);

    /**
     * 根据父id获取分类集合
     * @param id
     * @return
     */

    List<Map<String, Object>> getCategoryByParentId(String id);
    /**
     * 通过id查询单个对象
     *
     * @param id
     * @return
     */
    Category getOne(String id);

}
