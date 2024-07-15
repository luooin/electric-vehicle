package com.it.config;

public final class PayConstants {
    // 商户appid
    public static final String APP_ID = "2021000121674107";
    // 私钥 pkcs8格式的
    public static final String RSA_PRIVATE_KEY = "MIIEvA*****J889zzc8Z8SVG";
    // 服务器异步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static final String NOTIFY_URL = "http://127.0.0.1:8080/pay/notify";
    // 页面跳转同步通知页面路径 需http://或者https://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问 商户可以自定义同步跳转地址
    //public static final String RETURN_URL = "http://127.0.0.1:8080/pay/return";
    public static final String RETURN_URL = "http://127.0.0.1:8080/pay/return";
    // 请求网关地址，沙箱环境网关比正式环境网关多了 "dev" 。https://openapi.alipay.com/gateway.do
    public static final String GATEWAY_URL = "https://openapi.alipaydev.com/gateway.do";
    // 编码
    public static final String CHARSET = "UTF-8";
    // 返回格式
    public static final String FORMAT = "json";
    // 支付宝公钥，而不是应用公钥
    public static final String ALIPAY_PUBLIC_KEY = "MIIB*****UC/rn9yrXiCwIDAQAB";
    // 日志记录目录
    public static String log_path = "D:/tmp/logs/";
    // RSA2
    public static final String SIGN_TYPE = "RSA2";
}
