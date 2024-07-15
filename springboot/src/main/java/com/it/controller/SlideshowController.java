package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Slideshow;
import com.it.service.SlideshowService;
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
 * 〈实列接口〉<br>
 *
 * @author
 */
@Controller
@RequestMapping("/slideshow")
public class SlideshowController {
    @Autowired
    private SlideshowService slideshowService;
    @Autowired
    private ItdragonUtils itdragonUtils;

    /**
     * 管理界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("slideshow/index");
        return mv;
    }

    /**
     * 管理界面列表数据异步加载接口
     *
     * @param entity
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("slideshow.do")
    public TableResultResponse reloadTable(Slideshow entity, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Slideshow> pageInfo = slideshowService.selectPage(entity, page, limit);
        for (Slideshow record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("url", record.getUrl());
            resultMap.put("productId", record.getProductId());
            resultMap.put("userName", record.getUserName());
            resultMap.put("state", record.getState());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("slideshow/addPage");
        return mv;
    }

    /**
     * 新增数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PostMapping("/slideshow.do")
    public ResultResponse insert(Slideshow entity) {
        boolean result = slideshowService.insert(entity);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(ModelAndView mv, String id) {
        Slideshow slideshow = slideshowService.getOne(id);
        mv.addObject("slideshow", slideshow);
        mv.setViewName("slideshow/editPage");
        return mv;
    }

    /**
     * 编辑数据接口
     *
     * @param entity
     * @return
     */
    @ResponseBody
    @PutMapping("/slideshow.do")
    public ResultResponse edit(Slideshow entity) {
        boolean result = slideshowService.editById(entity);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除数据接口
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/slideshow.do")
    public ResultResponse delete(String ids) {
        boolean result = slideshowService.deleteById(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}