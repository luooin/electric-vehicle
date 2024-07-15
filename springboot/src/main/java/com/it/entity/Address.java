package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户收货地址实体类
 */
@Data
@TableName("gm_address")
public class Address implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 收货人
     */
    private String consignee;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 住址
     */
    private String site;
    /**
     * 是否默认
     */
    private String isDefault;
}