package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.AbnormalService;
import com.it.service.OrderService;
import com.it.service.ProductService;
import com.it.service.UserService;
import com.it.util.ItdragonUtils;
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
 * 描述：〈异常管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/abnormal")
public class AbnormalController {
    @Autowired
    private AbnormalService AbnormalService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private OrderService orderService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    /**
     * 异常管理首页页面跳转
     * （将数据库中所有的菜单信息查询出来封装好传给前台，前台通过
     * layui-tree插件现实菜单数据）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/reportIndex.do")
    public ModelAndView reportIndex(ModelAndView mv) {
        mv.setViewName("/abnormal/reportIndex");
        return mv;
    }

    @RequestMapping("/backIndex.do")
    public ModelAndView backIndex(ModelAndView mv) {
        mv.setViewName("/abnormal/backIndex");
        return mv;
    }

    /**
     * 异步加载异常信息列表
     *
     * @param Abnormal
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("abnormal.do")
    public TableResultResponse reloadTable(int page, int limit, Abnormal Abnormal) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Abnormal> pageInfo = AbnormalService.selectPage(Abnormal, page, limit);
        for (Abnormal record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("type", record.getType());
            resultMap.put("state", record.getState());
            resultMap.put("content", record.getContent());
            resultMap.put("userName", record.getUserName());
            resultMap.put("sellName", record.getSellName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增异常页面弹窗
     *
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("/abnormal/addPage");
        return mv;
    }

    /**
     * 新增异常
     *
     * @param Abnormal
     * @return
     */
    @PostMapping("/abnormal.do")
    @ResponseBody
    public ResultResponse insert(Abnormal Abnormal) {
        boolean result = AbnormalService.insert(Abnormal);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 归还异常
     *
     * @param Abnormal
     * @return
     */
    @PostMapping("/back.do")
    @ResponseBody
    public ResultResponse back(Abnormal Abnormal, String orderId, String refundId) {
        Order order = orderService.getOne(orderId);
        Abnormal.setType("back");
        Abnormal.setSellName(order.getSaleUser());
        Abnormal.setName("归还异常,订单号:" + order.getUuId() + "    (" + order.getProductName() + ")");
        boolean result = AbnormalService.insert(Abnormal);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        orderService.editState(orderId, "6");
        Refund refund = new Refund();
        refund.setId(refundId);
        refund.setState("2");
        orderService.edit(refund);
        return Result.resuleSuccess();
    }

    /**
     * 归还异常页面弹窗
     *
     * @return
     */
    @RequestMapping("/backPage.do")
    public ModelAndView backPage(ModelAndView mv, String orderId, String refundId) {
        mv.addObject("orderId", orderId);
        mv.addObject("refundId", refundId);
        mv.setViewName("/abnormal/backPage");
        return mv;
    }

    /**
     * 商品举报异常页面弹窗
     *
     * @return
     */
    @RequestMapping("/reportPage.do")
    public ModelAndView reportPage(ModelAndView mv, String productId) {
        mv.addObject("productId", productId);
        mv.setViewName("/abnormal/reportPage");
        return mv;
    }

    /**
     * 归还异常
     *
     * @param Abnormal
     * @return
     */
    @PostMapping("/report.do")
    @ResponseBody
    public ResultResponse report(Abnormal Abnormal, String productId) {
        Product product = productService.getOne(productId);
        Abnormal.setType("report");
        User select = userService.selectByPrimaryKey(product.getSellerId());
        Abnormal.setSellName(select.getUserName());
        Abnormal.setName("违规举报异常,车辆:" + product.getName());
        boolean result = AbnormalService.insert(Abnormal);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑异常页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Abnormal abnormal = AbnormalService.getOne(id);
        mv.addObject("abnormal", abnormal);
        mv.setViewName("/abnormal/editPage");
        return mv;
    }

    /**
     * 编辑异常
     *
     * @param Abnormal
     * @return
     */
    @PutMapping("/abnormal.do")
    @ResponseBody
    public ResultResponse edit(Abnormal Abnormal) {
        boolean result = AbnormalService.editById(Abnormal);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除异常
     *
     * @param id
     * @return
     */
    @DeleteMapping("/abnormal.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = AbnormalService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}