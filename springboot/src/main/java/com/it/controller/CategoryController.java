package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Category;
import com.it.service.CategoryService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：〈车辆分类管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 车辆分类管理首页页面跳转
     * （将数据库中所有的菜单信息查询出来封装好传给前台，前台通过
     * layui-tree插件现实菜单数据）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/category/index");
        return mv;
    }

    /**
     * 获取车辆分类接口
     *
     * @param
     * @return
     */
    @GetMapping("/getList.do")
    @ResponseBody
    public Map<String, Object> getList() {
        //获取分类集合
        List<Category> categoryList = categoryService.getList(new Category());
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Category category : categoryList) {
            Map<String, Object> sysMap = new HashMap<>();
            sysMap.put("name", category.getName());
            sysMap.put("id", category.getId());
            sysMap.put("parentId", category.getParentId());
            sysMap.put("orderNum", category.getPriority());
            sysMap.put("rank", category.getRank());
            sysMap.put("checkArr", "0");
            List<Map<String, Object>> mapList = categoryService.getCategoryByParentId(category.getId());
            sysMap.put("children", mapList);
            //此判断是将非一级分类的信息不封装到结果集中，避免前台菜单树展示出二级分类（注：二级分类在一级分类下拉中展示，
            // 取消此判断会让二级分类和一级分类并列展示出来）
            if (category.getParentId().equals("0")) {
                resultList.add(sysMap);
            }

        }
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("data", resultList);
        hashMap.put("code", 0);
        return hashMap;
    }

    /**
     * 分类管理界面管理界面列表
     *
     * @param category
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("category.do")
    public TableResultResponse reloadTable(int page, int limit, Category category) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Category> pageInfo = categoryService.selectPage(category, page, limit);
        for (Category record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("parentId", record.getParentId());
            resultMap.put("name", record.getName());
            resultMap.put("rank", record.getRank());
            resultMap.put("priority", record.getPriority());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增分类页面弹窗
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(String parentId, ModelAndView mv) {
        mv.addObject("parentId", parentId);
        mv.setViewName("/category/addPage");
        return mv;
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping("/category.do")
    @ResponseBody
    public ResultResponse insert(Category category) {
        //查询父分类个数
        List<Map<String, Object>> list = categoryService.getCategoryByParentId("0");
        if ("0".equals(category.getParentId())) {
            if (list.size() > 8) {
                return Result.resuleError("父分类最大添加数为9,请合理安排分类条目!");
            }
        }
        boolean result = categoryService.insert(category);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑分类页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Category category = categoryService.getOne(id);
        mv.addObject("category", category);
        mv.setViewName("/category/editPage");
        return mv;
    }

    /**
     * 下级分类页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/listPage.do")
    public ModelAndView listPage(String id, ModelAndView mv) {
        Category category = categoryService.getOne(id);
        mv.addObject("category", category);
        mv.setViewName("/category/listPage");
        return mv;
    }

    /**
     * 编辑分类
     *
     * @param category
     * @return
     */
    @PutMapping("/category.do")
    @ResponseBody
    public ResultResponse edit(Category category) {
        boolean result = categoryService.editById(category);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping("/category.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = categoryService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}