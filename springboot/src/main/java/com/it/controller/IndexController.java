package com.it.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.*;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import java.util.List;

/**
 * 后台相关操作接口(页面跳转,个人信息操作等)
 *
 * @version:
 * @Description:
 */
@Controller
public class IndexController {
    private static final transient Logger log = LoggerFactory.getLogger(IndexController.class);
    private static final String GENERAL_USER = "035e6cd6738c42e5a4112d34e85e0832";//普通用户id
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private LogService logService;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrderService orderService;


    /**
     * 后台首页跳转(后台初始页面跳转接口)
     *
     * @param mv
     * @param model
     * @return
     */
    @GetMapping("/diplomaProjectAdmin")
    public ModelAndView diplomaProjectAdmin(ModelAndView mv, Model model) {
        CommonMethods(mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        List<Menu> menuList = permissionService.getMenuInfoByUserId(user.getId());
        JSONArray jsonArray = new JSONArray();
        for (Menu menu : menuList) {
            JSONObject menuMap = new JSONObject();
            menuMap.put("F_ModuleId", menu.getMenuId());
            menuMap.put("F_ParentId", menu.getPerentMenuId());
            menuMap.put("F_FullName", menu.getMenuName());
            menuMap.put("F_UrlAddress", menu.getMenuURL());
            menuMap.put("F_Icon", menu.getIcon());
            jsonArray.add(menuMap);
        }
        model.addAttribute("menuInfo", jsonArray);
        model.addAttribute("user", user);
        mv.setViewName("content/index");
        return mv;
    }

    /**
     * 后台注册页面跳转
     */
    @RequestMapping("/backstageReg")
    public ModelAndView reg(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("backstageReg");
        return mv;
    }

    /**
     * 登录页面
     *
     * @param mv
     * @return
     */
    @RequestMapping("/backstageLogin")
    public ModelAndView login(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("/login");
        return mv;
    }

    /**
     * 404页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/404")
    public ModelAndView silingsi(ModelAndView mv) {
        mv.setViewName("content/404");
        return mv;
    }

    /**
     * 修改密码页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/changePwd")
    public ModelAndView changePwd(ModelAndView mv) {
        User user = itdragonUtils.getSessionUser();
        mv.addObject("user", user);
        mv.setViewName("content/changePwd");
        return mv;
    }

    /**
     * 用户修改密码
     *
     * @param newPas
     * @param oldPas
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePas")
    public ResultResponse updatePassword(String newPas, String oldPas) {
        User user = itdragonUtils.getSessionUser();
        if (oldPas.equals(user.getPlainPassword())) {
            boolean result = userService.changePass(newPas, user.getUserName());
            if (result) {
                user.setPlainPassword(newPas);
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("修改失败");
            }

        }
        return Result.resuleError("原密码错误,请重新输入");


    }

    /**
     * 用户个人信息
     *
     * @param mv
     * @return
     */
    @GetMapping("/userInfo")
    public ModelAndView userInfo(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("content/userInfo");
        return mv;
    }

    /**
     * 修改头像信息
     *
     * @param mv
     * @return
     */
    @GetMapping("/changeImg")
    public ModelAndView changeImg(ModelAndView mv) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("content/changeImg");
        return mv;
    }

    /**
     * 找回密码
     *
     * @param toEmail
     * @return
     */
    @ResponseBody
    @PostMapping("/retrievePass")
    public ResultResponse retrievePass(String toEmail, String userName) throws Exception {
        User userByUserName = userService.getUserByUserName(userName);
        if (!toEmail.equals(userByUserName.getEmail())) {
            return Result.resuleError("账号和邮箱不符!");
        }
        try {
            final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setFrom("1156326165@qq.com");
            message.setTo(toEmail);
            message.setSubject("找回密码");
            message.setText("您的密码是:" + userByUserName.getPassword());
            this.mailSender.send(mimeMessage);
            return Result.resuleSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
            return Result.resuleError("发送失败");
        }
    }

    /**
     * 首页
     *
     * @param mv
     * @return
     */
    @GetMapping("/main")
    public ModelAndView main(ModelAndView mv) {
        CommonMethods(mv);
        Log log = new Log();
        log.setOperation("登录");
        Page<Log> logPage = logService.selectPage(log, 1, 10);
        mv.addObject("logList", logPage.getRecords());
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        mv.addObject("user", user);
        //查询订单总金额
        Float orderPrice = orderService.getOrderPrice();
        mv.addObject("orderPrice", orderPrice);
        //访问量
        int LoginNum = logService.selectList("登录").size();
        mv.addObject("LoginNum", LoginNum);
        //活跃用户
        int userNum = userService.selectListInRole(GENERAL_USER).size();
        mv.addObject("userNum", userNum);
        //查询最新订单
        Page<Order> orderPage = orderService.selectPage(new Order(), 1, 3);
        mv.addObject("orderList", orderPage.getRecords());
        mv.setViewName("content/main");
        return mv;
    }

    /**
     * 页面有一些公用需求在这里抽取出来
     *
     * @param mv
     */
    public void CommonMethods(ModelAndView mv) {
        //网站参数
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
    }

    /**
     * 消息记录页
     *
     * @param mv
     * @return
     */
    @GetMapping("/messaging")
    public ModelAndView messaging(ModelAndView mv) {
        CommonMethods(mv);
        //获取消息记录
        List<Message> messageList = messageService.getListNew();
        if (!messageList.isEmpty()) {
            for (Message message : messageList) {
                message.setSendUserName(userService.selectByPrimaryKey(message.getSendUser())
                        == null ? "用户已注销" : userService.selectByPrimaryKey(message.getSendUser()).getUserName());
                message.setSendUserImg(userService.selectByPrimaryKey(message.getSendUser())
                        == null ? " " : userService.selectByPrimaryKey(message.getSendUser()).getImgUrl());
            }
            //然后获取第一个sendUserid
            mv.addObject("sendUserId", messageList.get(0).getSendUser());
            mv.addObject("sendUserName", messageList.get(0).getSendUserName());
        }
        mv.addObject("messageList", messageList);
        mv.addObject("type", "serviceState");
        mv.setViewName("content/messaging");
        return mv;
    }
}
