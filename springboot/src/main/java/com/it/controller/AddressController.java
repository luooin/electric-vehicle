package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Address;
import com.it.service.AddressService;
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
 * 描述：〈用户收货地址相关接口/控制器〉
 */
@Controller
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressService AddressService;
    @Autowired
    private ItdragonUtils itdragonUtils;

    /**
     * 用户收货管理首页页面跳转
     * （将数据库中所有的菜单信息查询出来封装好传给前台，前台通过
     * layui-tree插件现实菜单数据）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/address/index");
        return mv;
    }

    /**
     * 异步加载收货地址信息列表
     *
     * @param Address
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("address.do")
    public TableResultResponse reloadTable(int page, int limit, Address Address) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Address.setUserId(itdragonUtils.getSessionUserId());
        Page<Address> pageInfo = AddressService.selectPage(Address, page, limit);
        for (Address record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userId", record.getUserId());
            resultMap.put("consignee", record.getConsignee());
            resultMap.put("site", record.getSite());
            resultMap.put("phone", record.getPhone());
            resultMap.put("isDefault", record.getIsDefault());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增收货地址页面弹窗
     *
     * @return
     */
    @RequestMapping("/addPage.do")
    public ModelAndView addPage(ModelAndView mv) {
        mv.setViewName("/address/addPage");
        return mv;
    }

    /**
     * 新增收货地址
     *
     * @param address
     * @return
     */
    @PostMapping("/address.do")
    @ResponseBody
    public ResultResponse insert(Address address) {
        boolean result = AddressService.insert(address);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑收货地址页面弹窗
     *
     * @param id
     * @return
     */
    @RequestMapping("/editPage.do")
    public ModelAndView editPage(String id, ModelAndView mv) {
        Address address = AddressService.getOne(id);
        mv.addObject("address", address);
        mv.setViewName("/address/editPage");
        return mv;
    }

    /**
     * 编辑收货地址
     *
     * @param address
     * @return
     */
    @PutMapping("/address.do")
    @ResponseBody
    public ResultResponse edit(Address address) {
        boolean result = AddressService.editById(address);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除收货地址
     *
     * @param id
     * @return
     */
    @DeleteMapping("/address.do")
    @ResponseBody
    public ResultResponse delete(String id) {
        boolean result = AddressService.deleteById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}