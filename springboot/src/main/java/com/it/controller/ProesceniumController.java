package com.it.controller;


import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.plugins.Page;
import com.it.entity.*;
import com.it.service.*;
import com.it.util.ItdragonUtils;
import com.it.util.Result;
import com.it.util.ResultResponse;
import com.it.vo.SubmitOrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 前台相关操作接口
 *
 * @version:
 * @Description:
 */
@Controller
public class ProesceniumController {
    private static final String GENERAL_USER = "035e6cd6738c42e5a4112d34e85e0832";//普通用户id
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private LogService logService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private WbeParameterService wbeParameterService;
    @Autowired
    private ItdragonUtils itdragonUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private SlideshowService slideshowService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopCartService shopCartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private BrowseService browseService;
    @Autowired
    private EvaluateService evaluateService;
    @Autowired
    private NoticeService noticeService;

    /**
     * 前台首页跳转(前台初始页面跳转接口)
     *
     * @param mv
     * @return
     */
    @GetMapping("/diplomaProject")
    public ModelAndView diplomaProject(ModelAndView mv) {
        CommonMethods(mv);
        //查询轮播图
        List<Slideshow> slideshowList = slideshowService.getListByType("");
        mv.addObject("slideshowList", slideshowList);
        //封装查询信息
        Product searchProduct = new Product();
        searchProduct.setState("1");
        //查询最新车辆
        Page<Product> productPage1 = productService.selectPage(searchProduct, 1, 12);
        searchProduct.setOrderFiled("collectNum");
        //查询收藏推荐车辆
        Page<Product> productPage2 = productService.selectPage(searchProduct, 1, 20);
        mv.addObject("newProductList", productPage1.getRecords());
        mv.addObject("saleProductList", productPage2.getRecords());
        mv.addObject("indexClass", "active");

        //系统公告
        Page<Notice> noticePage = noticeService.selectPage(new Notice(), 1, 5);
        mv.addObject("noticeList", noticePage.getRecords());
        mv.setViewName("proscenium/index");
        Product product = new Product();
        product.setState("1");
        List<Product> articleList = productService.getList(product);
        Collections.shuffle(articleList);
        if (articleList.size() > 10) {
            mv.addObject("productList", articleList.subList(0, 10));
        } else {
            mv.addObject("productList", articleList);
        }
        return mv;
    }


    /**
     * 浏览记录
     */
    @RequestMapping("/browsePage")
    public ModelAndView browsePage(ModelAndView mv) {
        CommonMethods(mv);
        List<BrowseRecord> browseRecords = browseService.getListMy();
        for (BrowseRecord browseRecord : browseRecords) {
            Product product = productService.getOne(browseRecord.getItemId());
            if (product != null) {
                browseRecord.setProduct(product);
            } else {
                browseRecord.setProduct(new Product());
            }
        }
        mv.addObject("browseRecords", browseRecords);
        mv.setViewName("proscenium/browsePage");
        return mv;
    }


    /**
     * 系统公告页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/noticeListPage")
    public ModelAndView noticeListPage(ModelAndView mv, String type) {
        CommonMethods(mv);
        List<Notice> noticeList = noticeService.getList(new Notice());
        mv.addObject("noticeListPage", "active");
        mv.addObject("noticeList", noticeList);
        mv.setViewName("proscenium/noticeListPage");
        return mv;
    }

    /**
     * 系统公告详情页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/noticeDetailsPage")
    public ModelAndView noticeDetailsPage(ModelAndView mv, String id) {
        CommonMethods(mv);
        mv.addObject("noticeListPage", "active");
        Notice notice = noticeService.getOne(id);
        mv.addObject("notice", notice);
        mv.setViewName("proscenium/noticeDetailsPage");
        return mv;
    }

    /**
     * 讨论区页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/discussPage")
    public ModelAndView discussPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.addObject("discussClass", "active");
        List<Leave> leaveList = leaveService.getList(null);
        for (Leave leave : leaveList) {
            List<Reply> replyList = leaveService.selectListReply(leave.getId());
            if (!replyList.isEmpty()) {
                leave.setReplyList(replyList);
            }
        }
        mv.addObject("leaveList", leaveList);
        mv.setViewName("proscenium/discussPage");
        return mv;
    }

    /**
     * 所有车辆页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/promotionPage")
    public ModelAndView promotionPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.addObject("promotionClass", "active");
        mv.setViewName("proscenium/promotionPage");
        return mv;
    }

    /**
     * 所有车辆页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/productList")
    public ModelAndView productList(ModelAndView mv, String categoryId) {
        CommonMethods(mv);
        Category category = categoryService.getOne(categoryId);
        //查询车辆分类信息
        List<Map<String, Object>> categoryList = categoryService.getCategoryByParentId(categoryId);
        mv.addObject("categoryList", categoryList);
        mv.addObject("category", category);
        mv.addObject("proListClass", "active");
        mv.setViewName("proscenium/productList");
        return mv;
    }

    /**
     * 所有车辆页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/topPage")
    public ModelAndView topPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.addObject("topPageClass", "active");
        List<User> getNumList = userService.getUserInRole("035e6cd6738c42e5a4112d34e85e0832", "getNum");
        List<User> sendNumList = userService.getUserInRole("035e6cd6738c42e5a4112d34e85e0832", "sendNum");
        mv.addObject("sendNumList", sendNumList);
        mv.addObject("getNumList", getNumList);
        Product product = new Product();
        product.setOrderFiled("collectNum");
        Page<Product> productPage = productService.selectPage(product, 1, 10);
        mv.addObject("productList", productPage.getRecords());
        mv.setViewName("proscenium/topPage");
        return mv;
    }

    /**
     * 车辆详情页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/productDetails")
    public ModelAndView productDetails(ModelAndView mv, String id) {
        CommonMethods(mv);
        Product product = productService.getOne(id);
        mv.addObject("product", product);
        Category category = categoryService.getOne(product.getParentCategoryId());
        Category sonCategory = categoryService.getOne(product.getCategoryId());
        mv.addObject("category", category);
        mv.addObject("sonCategory", sonCategory);
        mv.addObject("proListClass", "active");
        //封装查询信息
        Product searchProduct = new Product();
        searchProduct.setCategoryId(product.getCategoryId());
        searchProduct.setState("1");
        searchProduct.setOrderFiled("collectNum");
        //查询热销车辆
        Page<Product> productPage1 = productService.selectPage(searchProduct, 1, 6);
        mv.addObject("saleProductList", productPage1.getRecords());
        //查询卖家信息
        User user = userService.selectByPrimaryKey(product.getSellerId());
        mv.addObject("sellerName", user == null ? " - " : user.getUserName());
        //查询出该车辆的所有评价
        List<Evaluate> evaluateList = productService.getListByProductId(user == null ? " - " : user.getId());
        //解析评价图片格式,计算好评率
        Float good = Float.valueOf(0);
        for (Evaluate evaluate : evaluateList) {
            if (evaluate.getGrade() > 3) {
                good = good + 1;
            }
            JSONArray jsonArray = JSONArray.parseArray(evaluate.getImgs());
            List<String> imgList = new ArrayList<>();
            if (jsonArray != null) {
                for (Object img : jsonArray) {
                    imgList.add((String) img);
                }
                evaluate.setImgList(imgList);
            } else {
                evaluate.setImgList(new ArrayList<>());
            }

        }
        mv.addObject("evaluateList", evaluateList);
        mv.addObject("evaluateListNum", evaluateList.size());
        //计算好评率
        if (evaluateList.size() < 1) {
            mv.addObject("applause", 0);
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            float applause = good / evaluateList.size();
            mv.addObject("applause", df.format(applause * 100));
        }
        if (itdragonUtils.isGogin()) {
            //增加浏览记录
            browseService.insert(itdragonUtils.getSessionUserId(), id);
            //车辆收藏判断
            mv.addObject("isCollect", productService.isCollect(id));
            //判断是否为自己的车辆
            if (product.getSellerId().equals(itdragonUtils.getSessionUserId())) {
                mv.addObject("isMy", true);
            } else {
                mv.addObject("isMy", false);
            }
        } else {
            mv.addObject("isCollect", false);
        }
        mv.addObject("isGogin", itdragonUtils.isGogin());
        mv.setViewName("proscenium/productDetails");
        return mv;
    }

    /**
     * 搜索车辆页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/search")
    public ModelAndView search(ModelAndView mv, String productName) {
        CommonMethods(mv);
        Product product = new Product();
        product.setState("1");
        product.setName(productName);
        List<Product> productList = productService.getList(product);
        mv.addObject("productList", productList);
        mv.addObject("productName", productName);
        mv.setViewName("proscenium/search");
        return mv;
    }

    /**
     * 购物车页面跳转
     *
     * @param mv
     * @return
     */
    @GetMapping("/shopCart")
    public ModelAndView shopCart(ModelAndView mv) {
        CommonMethods(mv);
        //查询购物车信息
        List<ShopCart> shopCartList = shopCartService.getListByUserId(itdragonUtils.getSessionUserId());
        //封装响应数据容器
        List<Map<String, Object>> infoList = new ArrayList<>();
        //将查询到的用户购物车信息解析封装到响应中返回给前台
        for (ShopCart shopCart : shopCartList) {
            Map<String, Object> resultMap = new HashMap<>(16);
            //通过购物车对象中的车辆id查询到车辆对象
            Product product = productService.getOne(shopCart.getProductId());
            if (product != null) {
                resultMap.put("id", shopCart.getId());
                resultMap.put("num", shopCart.getNum());
                resultMap.put("img", product.getImg());
                resultMap.put("productId", product.getId());
                resultMap.put("name", product.getName());
                resultMap.put("disPrice", product.getDisPrice());
                resultMap.put("specification", product.getSpecification());
                resultMap.put("subtotal", product.getDisPrice() * shopCart.getNum());
                infoList.add(resultMap);
            }
        }
        mv.addObject("shopCartList", infoList);
        mv.setViewName("proscenium/shopCart");
        return mv;
    }

    /**
     * 存放购买的车辆信息
     *
     * @param paramText
     * @return
     */
    @PostMapping("/setBuySession")
    @ResponseBody
    public ResultResponse setBuySession(String paramText) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        List<SubmitOrderVo> submitOrderVoList = JSONArray.parseArray(paramText, SubmitOrderVo.class);
        //查询车辆的详细信息
        for (SubmitOrderVo submitOrderVo : submitOrderVoList) {
            Product product = productService.getOne(submitOrderVo.getProductId());
            submitOrderVo.setDisPrice(product.getDisPrice());
            submitOrderVo.setSpecification(product.getSpecification());
            submitOrderVo.setImg(product.getImg());
            submitOrderVo.setProductName(product.getName());
            if (submitOrderVo.getCarId() != null) {
                shopCartService.deleteById(submitOrderVo.getCarId());
            }
        }
        //存入session中
        itdragonUtils.getShiroSession().setAttribute("submitOrderVoList", submitOrderVoList);
        return Result.resuleSuccess();
    }

    /**
     * 确认订单页面
     */
    @RequestMapping("/submitOrder")
    public ModelAndView submitOrder(ModelAndView mv) {
        CommonMethods(mv);
        //返回当前登录用户信息
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        //查询当前用户的所有收货地址
        List<Address> addressList = addressService.getList();
        mv.addObject("addressList", addressList);
        //获取Session中的购买车辆信息
        List<SubmitOrderVo> submitOrderVoList = itdragonUtils.getSessionSubmitOrderVoList();
        mv.addObject("submitOrderVoList", submitOrderVoList);
        //计算实际付款
        Float zumPrice = Float.valueOf(0);
        if (submitOrderVoList != null) {
            for (SubmitOrderVo submitOrderVo : submitOrderVoList) {
                zumPrice = zumPrice + submitOrderVo.getDisPrice() * submitOrderVo.getNum();
            }
        }
        mv.addObject("zumPrice", zumPrice);
        mv.setViewName("proscenium/submitOrder");
        return mv;
    }

    /**
     * 支付订单页面
     */
    @RequestMapping("/toPayPage")
    public ModelAndView toPayPage(ModelAndView mv, String orderIds) {
        CommonMethods(mv);
        mv.addObject("orderIds", orderIds);
        mv.setViewName("proscenium/toPayPage");
        return mv;
    }

    /**
     * 支付成功页面
     */
    @RequestMapping("/paySuccess")
    public ModelAndView paySuccess(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/paySuccess");
        return mv;
    }

    /**
     * 个人中心界面
     */
    @RequestMapping("/userCenter")
    public ModelAndView userCenter(ModelAndView mv) {
        CommonMethods(mv);
        //返回当前登录用户信息
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        //查询自己的订单
        Integer num1 = orderService.orderNum("1");
        Integer num2 = orderService.orderNum("2");
        Integer num3 = orderService.orderNum("3");
        mv.addObject("num1", num1);
        mv.addObject("num2", num2);
        mv.addObject("num3", num3);
        //查询当前用户消息列表
        List<Message> messageList = messageService.getListNew();
        mv.addObject("messageList", messageList);
        mv.setViewName("proscenium/userCenter");
        return mv;
    }

    /**
     * 订单详情页面
     */
    @RequestMapping("/orderDetails")
    public ModelAndView orderDetails(ModelAndView mv, String id) {
        CommonMethods(mv);
        Order order = orderService.getOne(id);
        mv.addObject("order", order);
        //查询该订单的物流信息
        List<Logistics> logisticsList = orderService.getListByOrderId(id);
        mv.addObject("logisticsList", logisticsList);
        mv.setViewName("proscenium/orderDetails");
        return mv;
    }

    /**
     * 车辆收藏页面
     */
    @RequestMapping("/collectPage")
    public ModelAndView collectPage(ModelAndView mv) {
        CommonMethods(mv);
        List<Collect> collectList = productService.getList();
        mv.addObject("collectList", collectList);
        mv.setViewName("proscenium/collectPage");
        return mv;
    }

    /**
     * 评价车辆页面
     */
    @RequestMapping("/evaluatePage")
    public ModelAndView evaluatePage(ModelAndView mv, String id) {
        CommonMethods(mv);
        Order order = orderService.getOne(id);
        mv.addObject("order", order);
        mv.setViewName("proscenium/evaluatePage");
        return mv;
    }

    /**
     * 申请归还页面
     */
    @RequestMapping("/refundPage")
    public ModelAndView refund(ModelAndView mv, String id) {
        CommonMethods(mv);
        Order order = orderService.getOne(id);
        mv.addObject("order", order);
        mv.setViewName("proscenium/refundPage");
        return mv;
    }

    /**
     * 个人资料界面
     */
    @RequestMapping("/editUser")
    public ModelAndView editUser(ModelAndView mv) {
        CommonMethods(mv);
        //返回当前登录用户信息
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("proscenium/editUser");
        return mv;
    }

    /**
     * 我要提问界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addForumPage")
    public ModelAndView addForumPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/addForumPage");
        return mv;
    }


    /**
     * 我的上报
     */
    @RequestMapping("/leavePage")
    public ModelAndView leavePage(ModelAndView mv) {
        CommonMethods(mv);
        List<Leave> leaveList = leaveService.getList(itdragonUtils.getSessionUserName());
        mv.addObject("leaveList", leaveList);
        List<Reply> replyList = leaveService.getListReply(itdragonUtils.getSessionUserName());
        mv.addObject("replyList", replyList);
        mv.setViewName("proscenium/leavePage");
        return mv;
    }

    /**
     * 收货租赁人信息
     */
    @RequestMapping("/address")
    public ModelAndView address(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/address");
        return mv;
    }

    /**
     * 新增收货地址页面
     */
    @RequestMapping("/insertAddressPage")
    public ModelAndView insertAddressPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/insertAddress");
        return mv;
    }

    /**
     * 安全中心
     */
    @RequestMapping("/safety")
    public ModelAndView safety(ModelAndView mv) {
        CommonMethods(mv);
        User user = itdragonUtils.getSessionUser();
        mv.addObject("user", user);
        mv.setViewName("proscenium/safety");
        return mv;
    }

    /**
     * 发布车辆
     */
    @RequestMapping("/addProductPage")
    public ModelAndView addProductPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/addProductPage");
        return mv;
    }

    /**
     * 我的车辆
     */
    @RequestMapping("/myProductPage")
    public ModelAndView myProductPage(ModelAndView mv) {
        CommonMethods(mv);
        //查询出所有个人发布的车辆
        Product product = new Product();
        product.setSellerId(itdragonUtils.getSessionUserId());
        List<Product> productList = productService.getList(product);
        mv.addObject("productList", productList);
        mv.setViewName("proscenium/myProductPage");
        return mv;
    }

    /**
     * 我的订单
     */
    @RequestMapping("/myOrderPage")
    public ModelAndView myOrderPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/myOrderPage");
        return mv;
    }

    /**
     * 归还处理
     */
    @RequestMapping("/myRefundPage")
    public ModelAndView myRefundPage(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("proscenium/myRefundPage");
        return mv;
    }

    /**
     * 前台注册页面跳转
     */
    @RequestMapping("/prosceniumReg")
    public ModelAndView prosceniumReg(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("prosceniumReg");
        return mv;
    }

    /**
     * 前台登录页面跳转
     */
    @RequestMapping("/loginShiro")
    public ModelAndView prosceniumLogin(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("prosceniumLogin");
        return mv;
    }

    /**
     * 忘记密码页面
     *
     * @param mv
     * @return
     */
    @GetMapping("/forgetPas")
    public ModelAndView forgetPas(ModelAndView mv) {
        CommonMethods(mv);
        mv.setViewName("/forgetPas");
        return mv;
    }

    /**
     * 我的钱包
     */
    @RequestMapping("/wallet")
    public ModelAndView wallet(ModelAndView mv) {
        CommonMethods(mv);
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        mv.addObject("user", user);
        mv.setViewName("proscenium/wallet");
        return mv;
    }

    /**
     * 我的钱包
     */
    @RequestMapping("/walletPage")
    public ModelAndView walletPage(ModelAndView mv, Integer price, String newPas) {
        CommonMethods(mv);
        mv.addObject("price", price);
        mv.addObject("newPas", newPas);
        mv.setViewName("proscenium/walletPage");
        return mv;
    }

    /**
     * 充值
     *
     * @param newPas
     * @param price
     * @return
     */
    @ResponseBody
    @PostMapping("/recharge")
    public ResultResponse updatePassword(String newPas, Integer price) {
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUserId());
        if (newPas.equals(user.getPassword())) {
            user.setBalance(user.getBalance() + price);
            boolean update = userService.updateByPrimaryKey(user);
            if (update) {
                return Result.resuleSuccess();
            } else {
                return Result.resuleError("充值失败!");
            }

        }
        return Result.resuleError("支付密码错误!");


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
        //查询车辆分类信息
        List<Map<String, Object>> categoryList = categoryService.getCategoryByParentId("0");
        mv.addObject("categoryList", categoryList);
        //查询用户是否登录
        boolean gogin = itdragonUtils.isGogin();
        boolean is_general = false;
        if (gogin) {
            //查询用户是否非后台管理用户
            is_general = itdragonUtils.getSessionUser().getRoleId().equals(GENERAL_USER);
        }
        mv.addObject("is_loin", gogin);
        mv.addObject("is_general", is_general);
        //付款方式标识
        mv.addObject("payType", ItdragonUtils.payType());

    }
}