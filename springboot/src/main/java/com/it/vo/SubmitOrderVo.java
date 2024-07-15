package com.it.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 购买车辆提交订单参数接收实体类
 */
@Data
public class SubmitOrderVo implements Serializable {
    /**
     * 车辆id
     */
    private String carId;
    /**
     * 车辆id
     */
    private String productId;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 车辆名称
     */
    private String productName;
    /**
     * 车辆图片
     */
    private String img;
    /**
     * 车辆参数
     */
    private String specification;
    /**
     * 车辆价格
     */
    private Float disPrice;

}