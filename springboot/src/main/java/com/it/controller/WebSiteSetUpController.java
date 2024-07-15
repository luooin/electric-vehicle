package com.it.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.PackUpInfo;
import com.it.entity.User;
import com.it.entity.WbeParameter;
import com.it.service.UserService;
import com.it.service.WbeParameterService;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.util.TableResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version:
 * @Description: 网站设置相关接口
 */
@RequestMapping("/wbeSet")
@Controller
public class WebSiteSetUpController {
    private static final transient Logger log = LoggerFactory.getLogger(WebSiteSetUpController.class);
    private static final String SERVICE_USER = "a48ea1ba5fe1423896928772c8b78755";//初始用户头像
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private UserService userService;

    /**
     * 网站参数设置页面跳转接口
     *
     * @param mv
     * @return
     */
    @GetMapping("/wbeParameter.do")
    public ModelAndView wbeParameter(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        //查询客服用户
        List<User> userList = userService.selectListInRole(SERVICE_USER);
        mv.addObject("userList", userList);
        mv.setViewName("/systemSet/wbeSet");
        return mv;
    }

    /**
     * 数据库备份信息列表
     *
     * @param packUpInfo
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("packUpInfo.do")
    public TableResultResponse enlistmentTables(PackUpInfo packUpInfo, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<PackUpInfo> pageInfo = wbeParameterService.selectPage(packUpInfo, page, limit);
        for (PackUpInfo record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userName", record.getUserName());
            resultMap.put("name", record.getName());
            resultMap.put("url", record.getUrl());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 修改网站参数的接口
     *
     * @param wbeParameter
     * @return
     */
    @ResponseBody
    @PutMapping("/wbeParameter.do")
    public ResultResponse editWbeParameter(WbeParameter wbeParameter) {
        boolean result = wbeParameterService.editById(wbeParameter);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 数据库备份操作
     *
     * @param packUpInfo
     * @return
     */
    @ResponseBody
    @PostMapping("/packUpInfo.do")
    public ResultResponse insertPack(PackUpInfo packUpInfo) {
        String filePath = "D:\\User\\";
        String dbName = "itembank_database";
        long time = new java.util.Date().getTime();
        try {
            Process process = Runtime.getRuntime().exec(
                    "cmd  /c  mysqldump -u root -p123 " + dbName + " > "
                            + filePath + "/" + time
                            + ".sql");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return Result.resuleError("导出失败");
        }
        packUpInfo.setUrl(filePath + time + ".sql");
        packUpInfo.setName(time + ".sql");
        boolean result = wbeParameterService.insert(packUpInfo);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

}
