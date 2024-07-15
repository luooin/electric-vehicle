package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.*;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：〈车辆管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private BrowseService browseService;
    @Autowired
    private UserService userService;

    /**
     * 车辆管理首页页面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        //查询所有父id为0的分类信息(0即为一级分类)
        List<Map<String, Object>> mapList = categoryService.getCategoryByParentId("0");
        mv.addObject("categoryList", mapList);
        mv.setViewName("/product/index");
        return mv;
    }

    /**
     * 促销推荐
     *
     * @param mv
     * @return
     */
    @RequestMapping("/promotion.do")
    public ModelAndView promotion(ModelAndView mv) {
        mv.setViewName("/product/promotion");
        return mv;
    }

    /**
     * 车辆管理界面列表异步加载接口
     * 前端通过layui-table插件实现异步加载渲染数据
     *
     * @param product
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("product.do")
    public TableResultResponse reloadTable(int page, int limit, Product product) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Product> pageInfo = productService.selectPage(product, page, limit);
        for (Product record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            Category category = categoryService.getOne(record.getParentCategoryId());
            resultMap.put("categoryId", category.getName() == null ? "-" : category.getName());
            resultMap.put("uuId", record.getUuId());
            resultMap.put("img", record.getImg());
            resultMap.put("specification", record.getSpecification());
            //价格格式化:保留后两位小数
            DecimalFormat decimalFormat = new DecimalFormat(".00");
            resultMap.put("disPrice", decimalFormat.format(record.getDisPrice()));
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            resultMap.put("state", record.getState());
            resultMap.put("content", record.getContent());
            resultMap.put("collectNum", record.getCollectNum());
            resultMap.put("inventory", record.getInventory());
            User user = userService.selectByPrimaryKey(record.getSellerId());
            resultMap.put("sellerName", user != null ? user.getUserName() : " - ");
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }


    /**
     * 验证商品是否还有库存
     *
     * @param productId
     * @return
     */
    @PostMapping("/haveStock.do")
    @ResponseBody
    public ResultResponse haveStock(String productId) {
        User select = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        Product product = productService.getOne(productId);
        if ("是".equals(product.getRank())) {
            if ("否".equals(select.getRank())) {
                return Result.resuleError("此车辆仅会员用户可租借!");
            }
        }

        return Result.resuleSuccess();
    }

    /**
     * 添加推荐
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPromotion.do")
    public ModelAndView addPromotion(ModelAndView mv) {
        mv.setViewName("/product/addPromotion");
        return mv;
    }

    /**
     * 新增车辆页面
     * 查询出所有的车辆分类,并且封装传回前台
     *
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        //查询所有父id为0的分类信息(0即为一级分类)
        List<Map<String, Object>> mapList = categoryService.getCategoryByParentId("0");
        mv.addObject("categoryList", mapList);

        mv.setViewName("/product/addPage");
        return mv;
    }

    /**
     * 新增车辆
     *
     * @param product
     * @return
     */
    @PostMapping("/product.do")
    @ResponseBody
    public ResultResponse insert(Product product) {
        Category category = categoryService.getOne(product.getParentCategoryId());
        product.setRank(category.getRank());
        boolean result = productService.insert(product);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        userService.changeSendNum();
        return Result.resuleSuccess();
    }

    /**
     * 编辑车辆页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Product product = productService.getOne(id);
        mv.addObject("product", product);
        //查询所有父id为0的分类信息(0即为一级分类)
        List<Map<String, Object>> mapList = categoryService.getCategoryByParentId("0");
        mv.addObject("categoryList", mapList);
        //查询当前车辆分类下的所有子分类
        List<Map<String, Object>> sonList = categoryService.getCategoryByParentId(product.getParentCategoryId());
        mv.addObject("sonCategoryList", sonList);

        mv.setViewName("/product/editPage");
        return mv;
    }

    /**
     * 编辑车辆页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPageII.do")
    public ModelAndView editPageII(String id, ModelAndView mv) {
        Product product = productService.getOne(id);
        mv.addObject("product", product);
        //查询所有父id为0的分类信息(0即为一级分类)
        List<Map<String, Object>> mapList = categoryService.getCategoryByParentId("0");
        mv.addObject("categoryList", mapList);
        //查询当前车辆分类下的所有子分类
        List<Map<String, Object>> sonList = categoryService.getCategoryByParentId(product.getParentCategoryId());
        mv.addObject("sonCategoryList", sonList);
        mv.setViewName("/product/editPageII");
        return mv;
    }

    /**
     * 编辑车辆
     *
     * @param product
     * @return
     */
    @PutMapping("/product.do")
    @ResponseBody
    public ResultResponse edit(Product product) {
        boolean result = productService.editById(product);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除车辆
     *
     * @param id
     * @return
     */
    @DeleteMapping("/product.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = productService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除浏览记录
     *
     * @param id
     * @return
     */
    @DeleteMapping("/browseRecord.do")
    @ResponseBody
    public ResultResponse browseRecord(String id) {
        boolean result = browseService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 获取车辆数据总数
     *
     * @param parentId
     * @return
     */
    @PostMapping("/getCount.do")
    @ResponseBody
    public ResultResponse getCount(String parentId, String categoryId) {
        Product product = new Product();
        product.setParentCategoryId(parentId);
        product.setCategoryId(categoryId);
        List<Product> productList = productService.getList(product);
        if (productList.isEmpty()) {
            return Result.resuleSuccess(0);
        }
        return Result.resuleSuccess(productList.size());
    }

    /**
     * 车辆收藏
     *
     * @param productId
     * @return
     */
    @PostMapping("/collect.do")
    @ResponseBody
    public ResultResponse collect(String productId) {
        if (!itdragonUtils.isGogin()) {
            return Result.resuleError("请先登录!");
        }
        //首先判断该用户是否收藏
        boolean collect = productService.isCollect(productId);
        if (collect) {
            boolean deleteCollectById = productService.deleteCollectById(productId);
            if (deleteCollectById) {
                return Result.resuleSuccess(null, "取消收藏成功!");
            }
            return Result.resuleError("操作失败");
        } else {
            Product one = productService.getOne(productId);
            Collect collect1 = new Collect();
            collect1.setProductId(productId);
            collect1.setProductName(one.getName());
            collect1.setProductImg(one.getImg());
            collect1.setProductPrice(one.getDisPrice());
            boolean insert = productService.insert(collect1);
            if (insert) {
                //修改车辆收藏数
                productService.editCollectById(productId, 1);
                return Result.resuleSuccess(null, "收藏成功!");
            }
            return Result.resuleError("操作失败");

        }
    }

    /**
     * 评价列表页面弹窗
     *
     * @param mv
     * @return
     */
    @RequestMapping("/evaluate.do")
    public ModelAndView evaluate(ModelAndView mv, String productId) {
        mv.addObject("productId", productId);
        mv.setViewName("/product/evaluate");
        return mv;
    }

    /**
     * 评价列表页面弹窗
     *
     * @param mv
     * @return
     */
    @RequestMapping("/evaluatePro.do")
    public ModelAndView evaluatePro(ModelAndView mv, String productId) {
        mv.addObject("productId", productId);
        mv.setViewName("/product/evaluatePro");
        return mv;
    }

    /**
     * 车辆评价管理界面列表异步加载接口
     * 前端通过layui-table插件实现异步加载渲染数据
     *
     * @param evaluate
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("evaluateTable.do")
    public TableResultResponse reloadTableEvaluate(int page, int limit, Evaluate evaluate) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Evaluate> pageInfo = orderService.selectPage(evaluate, page, limit);
        for (Evaluate record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userName", record.getUserName());
            resultMap.put("imgs", record.getImgs());
            resultMap.put("grade", record.getGrade());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            resultMap.put("content", record.getContent());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 评价列表页面弹窗
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editEvaluatePage.do")
    public ModelAndView editEvaluatePage(ModelAndView mv, String id) {
        Evaluate evaluate = orderService.getEvaluate(id);
        mv.addObject("evaluate", evaluate);
        mv.setViewName("/product/editEvaluatePage");
        return mv;
    }

    /**
     * 编辑评价
     *
     * @param evaluate
     * @return
     */
    @PutMapping("/evaluate.do")
    @ResponseBody
    public ResultResponse edit(Evaluate evaluate) {
        boolean result = orderService.edit(evaluate);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }
}