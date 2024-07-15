package com.it.config;

public final class PayConstants {
    // 商户appid
    public static final String APP_ID = "2021000121674107";
    // 私钥 pkcs8格式的
    public static final String RSA_PRIVATE_KEY = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDwZcMpgb32IOmOEXNSJCqR6Efucg3c26/oFsQ28N3cOquvL0czSZvJCZF3yFAtH7EfCjQiTCwGL8cpTX7okTQVnGa7iKFOpc04aySL+HCVg2ROcDqDOcHqALIgs2udFcQ7Sig1VwatOEwoWIXuxAJ65gVAsyT2ekKxsil9y9yghvcLHaNLEL+QHbUAajW7phRHvKhVhp6X3QiMr2wPO/CwJGMcgkuv2OEm5W4MZwyT082ykh3MCDdu2SaM92jLu8z1mhBf30ApbuoIT7fO7KliITtyk3LWh4Ls0cg5Qs4Qf9vW9zzctWruXvahPLV7P5OygUpSRo1kpNYwrr87AvhAgMBAAECggEAcK3vcHHNI8mXTWBy0c1tazM3KNrJ0CJ4ZInPV+uaj1uSWT4wvaD1bbeMw9S/q4axo3hLr4ykhvWi++yO7lUoZMSJA+cEvswM5LzbyWoJuRm/IKQzx/ag3nthZfn3zWrRyzgHtwTBcFWhAOYD7iec398MgWuTah7CnuTCVK3Bdjt1OmFST2EygCwAZUdjThOEoeL3k6kBTBuaiEOzLeYp6EdAbTyDHb6gqF+jgUGes1vXHQPS9NHGOcSkd2hsWfwZqwRD4FNWCKgXNmPzMs8s5Hg3K2e4+e14Q0qYhJGoFwiW807HWj+Rm1Qw8x8gCWQFituQe3bw5IJWz72e/Ew0AQKBgQC8gbWPoUDT6uG3/hKReTVfikI8dJuV2vFgxdlEsdJoJLXqvq47KNWxI2/UykfREaPOIs1FUGX8RBfivphhBASoxFdRjBIzsmZQv3TsmcPKMOaYy9SlCeC1yVbJoxpY8/wzNDqfpMDOUNy2XOQVYN68wNIkPenlztuLwuoOYXOlmQKBgQCy7jPnEIRPIiOe94PqnbXrub7/caP8mN3uXU7AKqj6SY46spfPEow8B92TnZMe2tSHB7vl4UTLUQU5O/D5NmcPqntLKMmhdW1iuxfaBgYfnVCaobV5idSsqKkY79LS0h5FRUMG5P1ks6Dc188cRHBVqCA9et4Wrak9JMVjCxX1iQKBgB+FlSXKcju9qDcLPnGjEiDCzuarHSVG8lwbWYSaM7QEh4MUlbAA2RMA+P9dP/se9wROX4L6ucMgY9ofwv1eHaVXB770LEsGxwMzsMqrv8i8uqLMWueqKjBb1a5sHsjHK+IoFrGBbFhTv0SjbeSBSV3I5Jr423b5S08ordjvxNXpAoGAYgPSMrT9hUuyL5ZukMqrqOErG0rpBanHiJdcWDAGOHSqkMTcG/aREyFL41ziBnjhCsbVyutHgAh10BBhxGJkulQtDdLeALAOp5LNUgUSvaazTMFgY594/Mu3NY3J8upe4ps6SECP/eXYLKGbNmTXFFTwtDzmA45FstS9u6+K6+ECgYALcrkMDMt0Bgp2r88gkpVtuBi8bJCfEAPh4CqRP0C3CqGtXSlwUiWjKvM6rOg/gmc4AOfNcGlpZYCkFkTf1JFM2sdpuFhaNtBCXjM1tEDEp9s05IIvJL7Wp2iaXlO3k47AU1LnTe3Gz0lvR/M1mRJC0mo8yUQ+4RiWS7TZj1Kcxw==";
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
    public static final String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtn9ASSolKhSN0fOfLSLfJDgVKGycyNR4Y+LtGC6FAD9cjA13Gl4OgQMYk+cxs6f1ZvEslBhywKp6EA9I/lz6K7GO6oZPQGV6cEc097l5BF1mAoC88ONIyw0UhJiwWaKj4IEWy8uro4pRTAZp6d6v0OlbaNScvJttXxgOBJmIhSJ4GoXZWnwW3CIwbLHxUS6prkujYNbRJpm2xX612VBU/PBTxViPD1oBCcCDwec0GVNPRKI6m90n5yalDl8lCX+Z5Wzt3y2IQj603QQa4QcnhGVTi5F0Z3DMEkgvID23bQlW77tbb+lZDmC3dFPHYzTs0UrcesNPiDUC/rn9yrXiCwIDAQAB";
    // 日志记录目录
    public static String log_path = "D:/tmp/logs/";
    // RSA2
    public static final String SIGN_TYPE = "RSA2";
}
