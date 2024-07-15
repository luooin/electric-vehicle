package com.it.util;

import lombok.Data;

/**
 * 描述:〈自定义配置项〉<br>
 */
@Data
public class CustomConfiguration {
    /**
     * 用户密码是否加密属性
     */
    private String isEncrypted;
    /**
     * 付款方式
     * alipay (支付宝支付) balance (余额付款) simulation(模拟支付)
     */
    private String payType;
}
