package com.it.controller;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.AddressService;
import com.it.service.OrderService;
import com.it.service.ProductService;
import com.it.service.UserService;
import com.it.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 描述：〈订单相关接口/控制器〉
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService OrderService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;

    /**
     * 订单管理首页页面跳转
     * （将数据库中所有的订单信息查询出来封装好传给前台）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        mv.setViewName("/order/index");
        return mv;
    }

    /**
     * 异步加载订单信息列表
     *
     * @param order
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("order.do")
    public TableResultResponse reloadTable(int page, int limit, Order order) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Order> pageInfo = OrderService.selectPage(order, page, limit);
        for (Order record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("uuId", record.getUuId());
            resultMap.put("userId", record.getUserId());
            resultMap.put("productId", record.getProductId());
            resultMap.put("productName", record.getProductName());
            resultMap.put("productImg", record.getProductImg());
            resultMap.put("specification", record.getSpecification());
            resultMap.put("unitPrice", record.getUnitPrice());
            resultMap.put("num", record.getNum());
            resultMap.put("saleUser", record.getSaleUser());
            resultMap.put("leave", ItdragonUtils.stringIsNotBlack(record.getLeave()) ? record.getLeave() : " - ");
            resultMap.put("way", record.getWay());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 11) + " - " + DateUtil.addDate3(record.getTime().substring(0, 11), record.getNum()));
            resultMap.put("endTime", DateUtil.addDate3(record.getTime().substring(0, 11), record.getNum()));
            resultMap.put("state", record.getState());
            resultMap.put("consigneeInfo", record.getConsigneeInfo());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 异步加载订单信息列表(仅查询卖家自己的订单)
     *
     * @param order
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("orderProSeller.do")
    public TableResultResponse reloadProTable(int page, int limit, Order order) {
        order.setSellerId(itdragonUtils.getSessionUserId());
        order.setIsDeleteSeller("0");
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Order> pageInfo = OrderService.selectPage(order, page, limit);
        for (Order record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("uuId", record.getUuId());
            resultMap.put("userId", record.getUserId());
            resultMap.put("productId", record.getProductId());
            resultMap.put("productName", record.getProductName());
            resultMap.put("productImg", record.getProductImg());
            resultMap.put("specification", record.getSpecification());
            resultMap.put("unitPrice", record.getUnitPrice());
            resultMap.put("num", record.getNum());
            resultMap.put("saleUser", record.getSaleUser());
            resultMap.put("leave", ItdragonUtils.stringIsNotBlack(record.getLeave()) ? record.getLeave() : " - ");
            resultMap.put("way", record.getWay());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            resultMap.put("state", record.getState());
            resultMap.put("endTime", DateUtil.addDate3(record.getTime().substring(0, 11), record.getNum()));
            resultMap.put("consigneeInfo", record.getConsigneeInfo());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 前台异步加载订单信息列表
     *
     * @param order
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("orderPro.do")
    public TableResultResponse reloadTablePro(int page, int limit, Order order) {
        order.setUserId(itdragonUtils.getSessionUserId());
        order.setIsDelete("0");
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Order> pageInfo = OrderService.selectPage(order, page, limit);
        for (Order record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("uuId", record.getUuId());
            resultMap.put("productId", record.getProductId());
            resultMap.put("productName", record.getProductName());
            resultMap.put("productImg", record.getProductImg());
            resultMap.put("unitPrice", record.getUnitPrice());
            resultMap.put("num", record.getNum());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            resultMap.put("endTime", DateUtil.addDate3(record.getTime().substring(0, 11), record.getNum()));
            resultMap.put("state", record.getState());
            String addTime = DateUtil.addMinute(30, "yyyy-MM-dd HH:mm:ss", record.getTime());
            //计算订单剩余时间,只取小时分钟
            String distanceHour = DateUtil.getDistanceHour(addTime);
            resultMap.put("distanceHour", distanceHour);
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    public static String getStringRandom() {
        Random random = new Random();
        //把随机生成的数字转成字符串
        String str = String.valueOf(random.nextInt(9));
        for (int i = 0; i < 5; i++) {
            str += random.nextInt(9);
        }
        return str;
    }

    /**
     * 提交订单接口
     *
     * @param address
     * @param orderInfo
     * @return
     */
    @PostMapping("/submitOrder.do")
    @ResponseBody
    public ResultResponse submitOrder(String orderInfo, String address) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        boolean result = false;
        //需要返回给前台的订单id集合
        JSONArray orderIds = new JSONArray();
        //解析orderInfo
        List<Order> orderList = JSONArray.parseArray(orderInfo, Order.class);
        //查询收货地址信息
        Address addressServiceOne = addressService.getOne(address);
        //查询车辆信息然后插入到数据库
        for (Order order : orderList) {
            Product product = productService.getOne(order.getProductId());
            order.setProductName(product.getName());
            order.setProductImg(product.getImg());

            order.setSellerId(product.getSellerId());
            order.setSpecification(product.getSpecification());
            order.setUnitPrice(product.getDisPrice());
            order.setConsigneeInfo(addressServiceOne.getSite() + "   " +
                    "(" + addressServiceOne.getConsignee() + ")   " + addressServiceOne.getPhone());
            result = OrderService.insert(order);
            if (result) {
                //修改商品对应的销量和库存
                product.setCollectNum(product.getCollectNum() + 1);
                product.setState("3");
                productService.editById(product);
                orderIds.add(order.getId());
            }
        }
        if (result) {
            userService.changeGetNum();
            return Result.resuleSuccess(orderIds);
        }
        return Result.resuleError("提交失败!");
    }

    /**
     * 立即支付
     *
     * @param orderIds
     * @return
     */
    @PostMapping("/toPay.do")
    @ResponseBody
    public ResultResponse toPay(String orderIds, String pass) {
        //验证密码
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        if (!pass.equals(user.getPassword())) {
            return Result.resuleError("密码错误!");
        }
        boolean result = false;
        //解析前台传过来的订单id
        JSONArray jsonArray = JSONArray.parseArray(orderIds);
        float price = 0;
        if ("balance".equals(ItdragonUtils.payType())) {
            for (Object orderId : jsonArray) {
                //修改订单状态
                Order order = OrderService.getOne((String) orderId);
                price = price + order.getUnitPrice() * order.getNum();
                //给卖家账户添加余额
                User user1 = userService.selectByPrimaryKey(order.getSellerId());
                if (user1 != null) {
                    user1.setBalance(user1.getBalance() + price);
                    userService.updateByPrimaryKey(user1);
                }
            }
            if (user.getBalance() < price) {
                return Result.resuleError("余额不足!");
            }
        }
        for (Object orderId : jsonArray) {
            //修改订单状态
            result = OrderService.editState((String) orderId, "1");
        }
        if (result) {
            if ("balance".equals(ItdragonUtils.payType())) {
                //成功扣除余额
                user.setBalance(user.getBalance() - price);
                userService.updateByPrimaryKey(user);
            }
            return Result.resuleSuccess();
        }
        return Result.resuleError("提交失败!");
    }

    /**
     * 订单确认页面跳转
     *
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping("/deliverPage.do")
    public ModelAndView deliverPage(ModelAndView mv, String id) {
        mv.addObject("id", id);
        mv.setViewName("/order/deliverPage");
        return mv;
    }

    /**
     * 订单确认页面跳转
     *
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping("/longPage.do")
    public ModelAndView longPage(ModelAndView mv, String id) {
        mv.addObject("id", id);
        mv.setViewName("/order/longPage");
        return mv;
    }

    /**
     * 编辑信息接口
     *
     * @param order
     * @return
     */
    @PutMapping("/order.do")
    @ResponseBody
    public ResultResponse edit(Order order) {
        if ("2".equals(order.getState())) {
            order.setWorkNum(getStringRandom());
        }
        boolean result = OrderService.edit(order);
        if (result) {
            return Result.resuleSuccess();
        }
        return Result.resuleError("编辑失败!");
    }

    /**
     * 编辑信息接口
     *
     * @param order
     * @return
     */
    @PutMapping("/long.do")
    @ResponseBody
    public ResultResponse longNum(Order order) {
        Order order1 = OrderService.getOne(order.getId());
        order1.setNum(order1.getNum() + order.getNum());
        boolean result = OrderService.edit(order1);
        if (result) {
            return Result.resuleSuccess();
        }
        return Result.resuleError("编辑失败!");
    }

    /**
     * 订单详情页面跳转
     *
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping("/detailsPage.do")
    public ModelAndView detailsPage(ModelAndView mv, String id) {
        Order order = OrderService.getOne(id);
        mv.addObject("order", order);
        //查询该订单的物流信息
        List<Logistics> logisticsList = OrderService.getListByOrderId(id);
        mv.addObject("logisticsList", logisticsList);
        mv.setViewName("/order/detailsPage");
        return mv;
    }

    /**
     * 添加物流信息页面跳转
     *
     * @param mv
     * @param id
     * @return
     */
    @RequestMapping("/logisticsPage.do")
    public ModelAndView logisticsPage(ModelAndView mv, String id) {
        mv.addObject("id", id);
        mv.setViewName("/order/logisticsPage");
        return mv;
    }

    /**
     * 添加物流信息接口
     *
     * @param logistics
     * @return
     */
    @PostMapping("/logistics.do")
    @ResponseBody
    public ResultResponse insertLogistics(Logistics logistics) {
        boolean result = OrderService.insert(logistics);
        if (result) {
            return Result.resuleSuccess();
        }
        return Result.resuleError("新增失败!");
    }

    /**
     * 订单评价接口
     *
     * @param evaluate
     * @return
     */
    @PostMapping("/evaluate.do")
    @ResponseBody
    public ResultResponse evaluate(Evaluate evaluate, String orderId) {
        //新增评价信息
        Product product = productService.getOne(evaluate.getProductId());
        if (product != null) {
            evaluate.setProductId(product.getSellerId());
        }
        boolean insert = OrderService.insert(evaluate);
        if (insert) {
            //如果评价成功修改订单状态
            OrderService.editState(orderId, "3");
            return Result.resuleSuccess();
        }
        return Result.resuleError("提交失败!");

    }

    /**
     * 订单申请归还接口
     *
     * @param refund
     * @return
     */
    @PostMapping("/refund.do")
    @ResponseBody
    public ResultResponse refund(Refund refund, String orderId) {
        Order one = OrderService.getOne(orderId);
        Product product = productService.getOne(one.getProductId());
        refund.setSellerId(one.getSellerId());
        boolean insert = OrderService.insert(refund);
        if (insert) {

            OrderService.editState(orderId, "4");
            return Result.resuleSuccess();
        }
        return Result.resuleError("提交失败!");

    }

    /**
     * 归还申请管理首页页面跳转
     * （将数据库中所有的归还申请查询出来封装好传给前台）
     *
     * @param mv
     * @return
     */
    @RequestMapping("/refund.do")
    public ModelAndView refund(ModelAndView mv) {
        mv.setViewName("/order/refund");
        return mv;
    }

    /**
     * 异步加载订单信息列表
     *
     * @param refund
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("refundTable.do")
    public TableResultResponse reloadTable(int page, int limit, Refund refund) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        if (!"1".equals(itdragonUtils.getSessionUser().getRoleId())) {
            refund.setSellerId(itdragonUtils.getSessionUserId());
        }

        Page<Refund> pageInfo = OrderService.selectPage(refund, page, limit);
        for (Refund record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("type", record.getType());
            resultMap.put("orderId", record.getOrderId());
            resultMap.put("price", record.getPrice());
            resultMap.put("reason", record.getReason());
            resultMap.put("content", record.getContent());
            resultMap.put("imgs", record.getImgs());
            Order order = OrderService.getOne(record.getOrderId());
            resultMap.put("productName", order.getProductName());
            resultMap.put("specification", order.getSpecification());
            resultMap.put("consigneeInfo", order.getConsigneeInfo());
            resultMap.put("uuId", order.getUuId());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            resultMap.put("state", record.getState());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 归还申请审核接口
     *
     * @param refund
     * @return
     */
    @PutMapping("/refund.do")
    @ResponseBody
    public ResultResponse refundEdit(Refund refund, String orderId) {
        Order order = OrderService.getOne(orderId);
        //编辑归还审核信息
        boolean insert = OrderService.edit(refund);
        if (insert) {
            //如果成功修改归还审核状态
            if ("1".equals(refund.getState())) {
                OrderService.editState(orderId, "5");
                //修改车辆的状态
                Product product = productService.getOne(order.getProductId());
                product.setState("1");
                productService.editById(product);
                if ("balance".equals(ItdragonUtils.payType())) {
                    //返回用户余额
                    User user = userService.selectByPrimaryKey(order.getUserId());
                    if (user != null) {
                        user.setBalance(user.getBalance() + order.getUnitPrice() * order.getNum());
                        userService.updateByPrimaryKey(user);
                    }
                    //给卖家账户扣除余额
                    User user1 = userService.selectByPrimaryKey(order.getSellerId());
                    if (user1 != null) {
                        user1.setBalance(user1.getBalance() - order.getUnitPrice() * order.getNum());
                        userService.updateByPrimaryKey(user1);
                    }
                }
            } else if ("2".equals(refund.getState())) {
                OrderService.editState(orderId, "6");
            }
            return Result.resuleSuccess();
        }
        return Result.resuleError("提交失败!");

    }

    /**
     * 删除订单接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/order.do")
    @ResponseBody
    public ResultResponse deleteOrder(String id) {
        boolean result = OrderService.deleteOrder(id);
        if (result) {
            return Result.resuleSuccess();
        }
        return Result.resuleError("新增失败!");
    }

    /**
     * 删除归还申请接口
     *
     * @param id
     * @return
     */
    @DeleteMapping("/refund.do")
    @ResponseBody
    public ResultResponse deleteRefund(String id) {
        boolean result = OrderService.deleteRefund(id);
        if (result) {
            return Result.resuleSuccess();
        }
        return Result.resuleError("新增失败!");
    }

    /**
     * 数据统计
     *
     * @param mv
     * @return
     */
    @RequestMapping("/statistics.do")
    public ModelAndView statistics(ModelAndView mv) {
        //计算趋势
        List<Order> orderList = OrderService.getOrderList(DateUtil.getNowMonth(), "3");
        Map<String, List<Order>> resMap = ListUtils.groupBy(orderList, new ListUtils.GroupBy<String, Order>() {
            @Override
            public String groupBy(Order row) {
                String ktrq = row.getTime();
                String ktrqStr = "";
                if (ktrq != null) {
                    ktrqStr = ktrq.substring(8, 11);
                }
                return ktrqStr;
            }
        });
        List<String> timeList = new ArrayList<>();
        List<Integer> orderNumberList = new ArrayList<>();
        mv.addObject("timeList", timeList);
        for (Map.Entry<String, List<Order>> m : resMap.entrySet()) {
            timeList.add(m.getKey() + "日");
            orderNumberList.add(m.getValue().size());
        }
        mv.addObject("timeList", timeList);
        mv.addObject("orderNumberList", orderNumberList);
        //金额统计
        List<String> timeList01 = new ArrayList<>();
        List<String> priceList = new ArrayList<>();
        for (Map.Entry<String, List<Order>> m : resMap.entrySet()) {
            timeList01.add(m.getKey() + "日");
            List<Order> orders = m.getValue();
            float price = 0;
            for (Order order : orders) {
                price = price + order.getUnitPrice() * order.getNum();
            }
            priceList.add(price + "");
        }
        mv.addObject("timeList01", timeList01);
        mv.addObject("priceList", priceList);
        //本年
        List<Order> orderListYear = OrderService.getOrderList(DateUtil.getNowYear(), "3");
        Map<String, List<Order>> resMap01 = ListUtils.groupBy(orderListYear, new ListUtils.GroupBy<String, Order>() {
            @Override
            public String groupBy(Order row) {
                String ktrq = row.getTime();
                String ktrqStr = "";
                if (ktrq != null) {
                    ktrqStr = ktrq.substring(5, 7);
                }
                return ktrqStr;
            }
        });
        List<String> monthList = new ArrayList<>();
        List<Integer> orderNumberList01 = new ArrayList<>();
        for (Map.Entry<String, List<Order>> m : resMap01.entrySet()) {
            monthList.add(m.getKey() + "月");
            orderNumberList01.add(m.getValue().size());
        }
        mv.addObject("monthList", monthList);
        mv.addObject("orderNumberList01", orderNumberList01);
        Product product = new Product();
        product.setOrderFiled("collectNum");
        Page<Product> productPage = productService.selectPage(product, 0, 10);
        mv.addObject("productList", productPage.getRecords());
        mv.setViewName("/order/statistics");
        return mv;
    }

//    @Configuration      //1.主要用于标记配置类，兼备Component的效果。
//    @EnableScheduling   // 2.开启定时任务
//    public class SaticScheduleTask {
//        //3.添加定时任务
//        //@Scheduled(cron = "0 0 21 * * ?")
//        //或直接指定时间间隔，例如：5秒
//        @Scheduled(fixedRate = 5000)
//        private void configureTasks() {
//            System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
//            //查询所有订单
//            List<Order> orderList = OrderService.getOrderList();
//            for (Order order : orderList) {
//                //获取改订单应截止时间
//                String endTime = DateUtil.addMinute(30, "yyyy-MM-dd HH:mm:ss", order.getTime());
//                //判断当前时间是否大于订单截止时间
//                boolean compare = DateUtil.compare(endTime, DateUtil.getNowDateSS());
//                //如果大于则修改状态
//                if (compare) {
//                    order.setState("7");
//                    order.setIsDelete("1");
//                    OrderService.edit(order);
//                }
//            }
//        }
//    }

}