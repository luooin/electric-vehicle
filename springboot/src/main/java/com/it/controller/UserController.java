package com.it.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.Role;
import com.it.entity.User;
import com.it.service.LogService;
import com.it.service.RoleService;
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
 * 描述：〈用户接口/控制器〉用户相关的请求都在这里
 */
@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private LogService logService;
    @Autowired
    private RoleService roleService;

    /**
     * 用户管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/userManagerHouse.do")
    public ModelAndView userManagerHouse(ModelAndView mv) {
        //查询出所有角色列表
        List<Role> roleList = roleService.getRoleList();
        mv.addObject("roleList", roleList);
        mv.setViewName("systemSet/userManager");
        return mv;
    }

    /**
     * 用户管理界面用户信息列表加载接口,注:列表加载为异步加载
     *
     * @param user
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("user.do")
    public TableResultResponse userTables(User user, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<User> pageInfo = userService.getUserList(user, page, limit);
        for (User userEntity : pageInfo.getRecords()) {
            Map<String, Object> userMap = new HashMap<>(16);
            userMap.put("id", userEntity.getId());
            userMap.put("userName", userEntity.getUserName());
            userMap.put("address", userEntity.getAddress());
            userMap.put("realName", userEntity.getRealName());
            userMap.put("rank", userEntity.getRank());
            userMap.put("sex", userEntity.getSex());
            userMap.put("iphone", userEntity.getIphone());
            userMap.put("email", userEntity.getEmail());
            userMap.put("roleId", userEntity.getRoleId());
            userMap.put("roleName", userEntity.getRoleName());
            userMap.put("status", userEntity.getStatus());
            userMap.put("createdDate", userEntity.getCreatedDate() == null ? "" : userEntity.getCreatedDate().substring(0, 19));
            userMap.put("updateDate", userEntity.getUpdatedDate() == null ? "" : userEntity.getUpdatedDate().substring(0, 19));
            infoList.add(userMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }


    /**
     * 新增用户跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addUserHouser.do")
    public ModelAndView addUserHouser(ModelAndView mv) {
        //查询出所有角色列表
        List<Role> roleList = roleService.getRoleList();
        mv.addObject("roleList", roleList);
        mv.setViewName("systemSet/addUser");
        return mv;
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/user.do")
    public ResultResponse addUser(User user) {
        User checkUser = userService.getUserByUserName(user.getUserName());
        if (checkUser != null) {
            return Result.resuleError("用户名已存在");
        }
        boolean result = userService.insert(user);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除用户,批量删除,单个删除通用
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @DeleteMapping("/user.do")
    public ResultResponse delUser(String ids) {
        boolean result = userService.deleteByPrimaryKey(ids);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 修改用户跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editUserHouser.do")
    public ModelAndView editUserHouser(ModelAndView mv, String id) {
        //查询出所有角色列表
        List<Role> roleList = roleService.getRoleList();
        mv.addObject("roleList", roleList);
        User user = userService.selectByPrimaryKey(id);
        mv.addObject("user", user);
        mv.setViewName("systemSet/editUser");
        return mv;
    }

    /**
     * 查看用户信息跳转界面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/userView.do")
    public ModelAndView userView(ModelAndView mv, String id) {
        User user = userService.selectByPrimaryKey(id);
        mv.addObject("user", user);
        mv.setViewName("systemSet/userView");
        return mv;
    }

    /**
     * 修改用户
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PutMapping("/user.do")
    public ResultResponse editUser(User user) {
        boolean result = userService.updateByPrimaryKey(user);
        if (!result) {
            return Result.resuleError("修改失败,未知错误");
        }
        logService.insert("修改用户信息", ItdragonUtils.getIp());
        return Result.resuleSuccess();
    }

    /**
     * 用户注册
     *
     * @param user
     * @return
     */
    @ResponseBody
    @PostMapping("/userRegister.do")
    public ResultResponse userRegister(User user) {
        System.out.println(user);
        User checkUser = userService.getUserByUserName(user.getUserName());
        if (checkUser != null) {
            return Result.resuleError("用户名已存在");
        }
        boolean result = userService.insert(user);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }
}