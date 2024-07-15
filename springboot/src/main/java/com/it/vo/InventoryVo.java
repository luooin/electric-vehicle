package com.it.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存,库存信息参数接收实体类
 */
@Data
public class InventoryVo implements Serializable {
    /**
     * 车辆id
     */
    private String productId;
    /**
     * 车辆编号
     */
    private String productUuId;
    /**
     * 入库数目
     */
    private Integer stock;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 成本单价
     */
    private Float costPrice;
    /**
     * 备注
     */
    private String content;
}