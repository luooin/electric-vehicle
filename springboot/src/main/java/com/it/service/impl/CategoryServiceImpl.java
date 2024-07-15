package com.it.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Category;
import com.it.mapper.CategoryMapper;
import com.it.service.CategoryService;
import com.it.util.ItdragonUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private ItdragonUtils itdragonUtils;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Page<Category> selectPage(Category entity, int page, int limit) {
        EntityWrapper<Category> searchInfo = new EntityWrapper<>();
        Page<Category> pageInfo = new Page<>(page, limit);
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            searchInfo.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getParentId())) {
            searchInfo.eq("parentId", entity.getParentId());
        } else {
            searchInfo.eq("parentId", "0");
        }
        searchInfo.orderBy("priority asc");
        List<Category> resultList = categoryMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Category entity) {
        Integer insert = categoryMapper.insert(entity);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(Category entity) {
        Integer insert = categoryMapper.updateById(entity);
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
            result = categoryMapper.deleteById(s);
        }
        if (result > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Category> getList(Category entity) {
        EntityWrapper<Category> wrapper = new EntityWrapper<>();
        if (ItdragonUtils.stringIsNotBlack(entity.getName())) {
            wrapper.eq("name", entity.getName());
        }
        if (ItdragonUtils.stringIsNotBlack(entity.getParentId())) {
            wrapper.eq("parentId", entity.getParentId());
        }
        List<Category> resultList = categoryMapper.selectList(wrapper);
        return resultList;
    }

    @Override
    public List<Map<String, Object>> getCategoryByParentId(String id) {
        EntityWrapper<Category> searchInfo = new EntityWrapper<>();
        searchInfo.eq("parentId", id);
        searchInfo.orderBy("priority asc");
        List<Category> categoryList = categoryMapper.selectList(searchInfo);
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Category category : categoryList) {
            List<Map<String, Object>> childrenResultList = new ArrayList<>();
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("id", category.getId());
            resultMap.put("name", category.getName());
            resultMap.put("rank", category.getRank());
            resultMap.put("orderNum", category.getPriority());
            resultMap.put("checkArr", "0");
            EntityWrapper<Category> childrenSearchInfo = new EntityWrapper<>();
            childrenSearchInfo.eq("parentId", category.getId());
            List<Category> childrenCategoryList = categoryMapper.selectList(childrenSearchInfo);
            for (Category category1 : childrenCategoryList) {
                Map<String, Object> childrenResultMap = new HashMap<>();
                childrenResultMap.put("id", category1.getId());
                childrenResultMap.put("name", category1.getName());
                childrenResultMap.put("rank", category.getRank());
                childrenResultMap.put("orderNum", category.getPriority());
                childrenResultMap.put("checkArr", "0");
                childrenResultList.add(childrenResultMap);
            }
            resultMap.put("children", childrenResultList);
            resultList.add(resultMap);
        }
        return resultList;
    }


    @Override
    public Category getOne(String id) {
        Category category = categoryMapper.selectById(id);
        if (category != null) {
            return category;
        } else {
            return new Category();
        }
    }
}