package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 收藏实体类
 */
@Data
@TableName("gm_collect")
public class Collect implements Serializable {
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
     * 车辆名称
     */
    private String productName;
    /**
     * 车辆图片
     */
    private String productImg;
    /**
     * 车辆价格
     */
    private Float productPrice;
    /**
     * 用户id
     */
    private String userId;
}