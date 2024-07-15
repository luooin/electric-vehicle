package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 物流信息实体类
 */
@Data
@TableName("gm_logistics")
public class Logistics implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 订单信息
     */
    private String orderId;
    /**
     * 物流内容
     */
    private String content;
    /**
     * 时间
     */
    private String time;

}