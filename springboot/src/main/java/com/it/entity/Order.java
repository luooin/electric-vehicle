package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单实体类
 */
@Data
@TableName("gm_order")
public class Order implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 订单编号
     */
    private String uuId;
    /**
     * 车辆id
     */
    private String productId;
    /**
     * 车辆名称
     */
    private String productName;
    /**
     * 车辆图片
     */
    private String productImg;
    /**
     * 车辆参数
     */
    private String specification;
    /**
     * 单价
     */
    private Float unitPrice;
    /**
     * 数目
     */
    private Integer num;
    /**
     * 收货人信息
     */
    private String consigneeInfo;
    /**
     * 订单留言
     */
    private String leave;
    /**
     * 配送方式
     */
    private String way;
    /**
     * 订单状态
     */
    private String state;
    /**
     * 订单创建时间
     */
    private String time;
    @TableField(exist = false)
    private String endTime;
    /**
     * 所属用户id
     */
    private String userId;
    /**
     * 快递公司
     */
    private String company;
    /**
     * 快递单号
     */
    private String workNum;
    /**
     * 假删除标记
     */
    private String isDelete;
    /**
     * 卖家假删除标记
     */
    private String isDeleteSeller;
    /**
     * 阿里支付订单编号
     */
    private String aliNo;
    /**
     * 下单用户
     */
    private String saleUser;
    /**
     * 卖家Id
     */
    private String sellerId;
}