package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Notice;
import com.it.service.NoticeService;
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
 * 描述：〈文章管理相关接口/控制器〉
 */
@Controller
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService NoticeService;
    @Autowired
    private ItdragonUtils itdragonUtils;

    /**
     * 文章管理首页页面跳转
     * （将数据库中所有的菜单信息查询出来封装好传给前台，前台通过
     * layui-tree插件现实菜单数据）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/notice/index");
        return mv;
    }

    /**
     * 异步加载文章信息列表
     *
     * @param Notice
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("notice.do")
    public TableResultResponse reloadTable(int page, int limit, Notice Notice) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Notice> pageInfo = NoticeService.selectPage(Notice, page, limit);
        for (Notice record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("title", record.getTitle());
            resultMap.put("content", record.getContent());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增文章页面弹窗
     *
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("/notice/addPage");
        return mv;
    }

    /**
     * 新增文章
     *
     * @param Notice
     * @return
     */
    @PostMapping("/notice.do")
    @ResponseBody
    public ResultResponse insert(Notice Notice) {
        boolean result = NoticeService.insert(Notice);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑文章页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Notice notice = NoticeService.getOne(id);
        mv.addObject("notice", notice);
        mv.setViewName("/notice/editPage");
        return mv;
    }

    /**
     * 编辑文章
     *
     * @param Notice
     * @return
     */
    @PutMapping("/notice.do")
    @ResponseBody
    public ResultResponse edit(Notice Notice) {
        boolean result = NoticeService.editById(Notice);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/notice.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = NoticeService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}