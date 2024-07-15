package com.it.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.it.config.PayConstants;
import com.it.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 电脑网站支付
 */
@Controller
@RequestMapping("/pay")
public class AlipayPcController {
    @Autowired
    private com.it.service.OrderService OrderService;

    /**
     * alipay.trade.page.pay(统一收单下单并支付页面接口)
     */
    @ResponseBody
    @RequestMapping("/pcTradePay")
    public void pcTradePay(HttpServletRequest servletRequest, HttpServletResponse servletResponse, String orderIds) throws AlipayApiException, IOException {
        //=================== 1、获取alipay客户端
        AlipayClient alipayClient = getAlipayClient();

        //=================== 2、请求参数
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        //服务器异步通知页面路径
        request.setNotifyUrl(PayConstants.NOTIFY_URL);
        //页面跳转同步通知页面路径
        request.setReturnUrl(PayConstants.RETURN_URL);
        JSONObject bizContent = new JSONObject();
        String outTradeNo = getTradeNo();
        System.out.println("生成的out_trade_no：" + outTradeNo);
        System.out.println("付款的车辆id：" + orderIds);
        //解析前台传过来的订单id
        JSONArray jsonArray = JSONArray.parseArray(orderIds);
        float price = 0;
        for (Object orderId : jsonArray) {
            //修改订单的阿里支付编号
            Order order = OrderService.getOne((String) orderId);
            order.setAliNo(outTradeNo);
            OrderService.edit(order);
            price = price + order.getUnitPrice() * order.getNum();
        }
        //LogFile.logResult("/api/pc/pcTradePay生成的out_trade_no：" + outTradeNo);
        bizContent.put("out_trade_no", outTradeNo);
        bizContent.put("total_amount", price);
        bizContent.put("subject", "订单编号:" + outTradeNo);//订单标题。注意：不可使用特殊字符，如 /，=，& 等。
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());
        //=================== 3、响应内容
        AlipayTradePagePayResponse response = alipayClient.pageExecute(request);
        if (response.isSuccess()) {
            System.out.println("支付调用成功");
        } else {
            System.out.println("支付调用失败");
        }
        //响应为表单格式，可嵌入页面,具体以返回的结果为准
        String form = response.getBody();
        servletResponse.setContentType("text/html;charset=" + PayConstants.CHARSET);
        servletResponse.getWriter().write(form);//直接将完整的表单html输出到页面
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();
    }


    @GetMapping("/return")  // 注意这里必须是POST接口
    public String payNotify(HttpServletRequest servletRequest, @RequestParam("out_trade_no") String outTradeNo) throws Exception {
        System.out.println("=========支付宝异步回调========");
        AlipayClient alipayClient = getAlipayClient();
        //=================== 2、请求参数
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", outTradeNo);
        request.setBizContent(bizContent.toString());
        //=================== 3、响应内容
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("交易查询调用成功");
        } else {
            System.out.println("交易查询调用失败");
        }
        //响应为json格式
        String body = response.getBody();
        System.out.println(body);
        JSONObject parseObject = JSONObject.parseObject(body);
        JSONObject o = (JSONObject) parseObject.get("alipay_trade_query_response");
        String code = o.getString("code");
        String out_trade_no = o.getString("out_trade_no");
        if ("10000".equals(code)) {
            //支付成功,修改订单的状态
            OrderService.editByAliNo(out_trade_no);
            //修改成功后跳转支付成功页面
            return "redirect:/paySuccess";
        } else {
            return "redirect:/payError";
        }

    }

    /**
     * alipay.trade.query(统一收单线下交易查询)
     */
    @RequestMapping("/pcTradeQuery")
    public void pcTradeQuery(HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam("outTradeNo") String outTradeNo) throws AlipayApiException, IOException {
        //=================== 1、获取alipay客户端
        AlipayClient alipayClient = getAlipayClient();

        //=================== 2、请求参数
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        //bizContent.put("out_trade_no", "20220501120853429");
        bizContent.put("out_trade_no", outTradeNo);
        //bizContent.put("trade_no", "2014112611001004680073956707");
        request.setBizContent(bizContent.toString());

        //=================== 3、响应内容
        AlipayTradeQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("交易查询调用成功");
        } else {
            System.out.println("交易查询调用失败");
        }
        //响应为json格式
        String body = response.getBody();
        System.out.println(body);
        servletResponse.setContentType("application/json;charset=" + PayConstants.CHARSET);
        servletResponse.getWriter().write(body);
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();
    }

    /**
     * alipay.trade.refund(统一收单交易退款接口)
     */
    @RequestMapping("/pcTradeRefund")
    public void pcTradeRefund(HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam("tradeNo") String tradeNo) throws AlipayApiException, IOException {
        //=================== 1、获取alipay客户端
        AlipayClient alipayClient = getAlipayClient();

        //=================== 2、请求参数
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        //bizContent.put("trade_no", "2022050122001421280506656470");
        bizContent.put("trade_no", tradeNo);
        bizContent.put("refund_amount", 0.01);
        bizContent.put("out_request_no", "HZ01RF001");

        //// 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);

        request.setBizContent(bizContent.toString());

        //=================== 3、响应内容
        AlipayTradeRefundResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("退款调用成功");
        } else {
            System.out.println("退款调用失败");
        }

        //响应为json格式
        String body = response.getBody();
        servletResponse.setContentType("application/json;charset=" + PayConstants.CHARSET);
        servletResponse.getWriter().write(body);
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();
    }

    /**
     * alipay.trade.fastpay.refund.query(统一收单交易退款查询)
     */
    @RequestMapping("/pcRefundQuery")
    public void pcRefundQuery(HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam("tradeNo") String tradeNo) throws AlipayApiException, IOException {
        //=================== 1、获取alipay客户端
        AlipayClient alipayClient = getAlipayClient();

        //=================== 2、请求参数
        AlipayTradeFastpayRefundQueryRequest request = new AlipayTradeFastpayRefundQueryRequest();
        JSONObject bizContent = new JSONObject();
        //bizContent.put("trade_no", "2022050122001421280506656470");
        bizContent.put("trade_no", tradeNo);
        bizContent.put("out_request_no", "HZ01RF001");

        //// 返回参数选项，按需传入
        //JSONArray queryOptions = new JSONArray();
        //queryOptions.add("refund_detail_item_list");
        //bizContent.put("query_options", queryOptions);
        request.setBizContent(bizContent.toString());

        //=================== 3、响应内容
        AlipayTradeFastpayRefundQueryResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        //响应为json格式
        if ("REFUND_SUCCESS".equals(response.getRefundStatus())) {
            System.out.println("退款成功");
        }
        servletResponse.setContentType("application/json;charset=" + PayConstants.CHARSET);
        servletResponse.getWriter().write(response.getBody());
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();
    }

    /**
     * alipay.trade.close(统一收单交易关闭接口)
     */
    @RequestMapping("/closePcTrade")
    public void closePcTrade(HttpServletRequest servletRequest, HttpServletResponse servletResponse, @RequestParam("tradeNo") String tradeNo) throws AlipayApiException, IOException {
        //=================== 1、获取alipay客户端
        AlipayClient alipayClient = getAlipayClient();

        //=================== 2、请求参数
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        JSONObject bizContent = new JSONObject();
        //bizContent.put("trade_no", "2022050122001421280506656471");
        bizContent.put("trade_no", tradeNo);
        request.setBizContent(bizContent.toString());

        //=================== 3、响应内容
        AlipayTradeCloseResponse response = alipayClient.execute(request);
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }

        //响应为json格式
        String body = response.getBody();
        servletResponse.setContentType("application/json;charset=" + PayConstants.CHARSET);
        servletResponse.getWriter().write(body);
        servletResponse.getWriter().flush();
        servletResponse.getWriter().close();

    }

    //alipay客户端配置（重要，必须保证正确）
    private AlipayClient getAlipayClient() throws AlipayApiException {
        AlipayConfig alipayConfig = new AlipayConfig();
        //支付网关, 默认是正式环境：https://openapi.alipay.com/gateway.do，此处使用沙箱环境测试
        alipayConfig.setServerUrl(PayConstants.GATEWAY_URL);
        //商户appid
        alipayConfig.setAppId(PayConstants.APP_ID);
        //私钥 pkcs8格式的
        alipayConfig.setPrivateKey(PayConstants.RSA_PRIVATE_KEY);
        //返回格式
        alipayConfig.setFormat(PayConstants.FORMAT);
        //编码
        alipayConfig.setCharset(PayConstants.CHARSET);
        //支付宝公钥，而不是应用公钥
        alipayConfig.setAlipayPublicKey(PayConstants.ALIPAY_PUBLIC_KEY);
        //RSA2
        alipayConfig.setSignType(PayConstants.SIGN_TYPE);
        return new DefaultAlipayClient(alipayConfig);
    }

    //生成交易订单号
    private String getTradeNo() {
        //时间（精确到毫秒）
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
        String localDate = LocalDateTime.now().format(ofPattern);
        //随机数
        //String randomNumeric = RandomStringUtils.randomNumeric(8);
        //return localDate + randomNumeric;
        return localDate;
    }

}
