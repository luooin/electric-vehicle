package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Evaluate;
import com.it.entity.Message;
import com.it.service.CategoryService;
import com.it.service.MessageService;
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
 * 描述：〈评论管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/evaluate")
public class EvaluateController {
    @Autowired
    private com.it.service.EvaluateService EvaluateService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;


    /**
     * 评论管理首页页面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/evaluate/index");
        return mv;
    }

    /**
     * 异步加载评论信息列表
     *
     * @param Evaluate
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("evaluate.do")
    public TableResultResponse reloadTable(int page, int limit, Evaluate Evaluate) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<com.it.entity.Evaluate> pageInfo = EvaluateService.selectPage(Evaluate, page, limit);
        for (com.it.entity.Evaluate record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userName", record.getUserName());
            resultMap.put("content", record.getContent());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }



    /**
     * 删除评论
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/evaluate.do")
    @ResponseBody
    public ResultResponse delete(String ids) {
        boolean result = EvaluateService.deleteById(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}