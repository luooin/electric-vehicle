package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 购物车实体类
 */
@Data
@TableName("gm_shopCart")
public class ShopCart implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 车辆id
     */
    private String productId;
    /**
     * 数量
     */
    private Integer num;
    /**
     * 用户id
     */
    private String userId;
}