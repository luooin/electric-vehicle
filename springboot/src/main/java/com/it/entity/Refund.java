package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 退款申请
 */
@Data
@TableName("gm_refund")
public class Refund implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 服务类型
     */
    private String type;
    /**
     * 退款原因
     */
    private String reason;
    /**
     * 退款金额
     */
    private Float price;
    /**
     * 退款说明
     */
    private String content;
    /**
     * 图片
     */
    private String imgs;
    /**
     * 状态
     */
    private String state;
    /**
     * 申请时间
     */
    private String time;
    /**
     * 卖家id
     */
    private String sellerId;
}