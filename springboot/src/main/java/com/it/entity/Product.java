package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆
 */
@Data
@TableName("gm_product")
public class Product implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 分类id
     */
    private String categoryId;
    /**
     * 父分类id
     */
    private String parentCategoryId;
    /**
     * 车辆编号
     */
    private String uuId;
    /**
     * 规格参数
     */
    private String specification;
    /**
     * 租赁价格
     */
    private Float disPrice;
    /**
     * 图片
     */
    private String img;
    /**
     * 详情
     */
    private String content;
    /**
     * 审核状态
     */
    private String state;
    /**
     * 上架时间
     */
    private String time;
    /**
     * 收藏数
     */
    private Integer collectNum;
    private Integer inventory;
    /**
     * 卖家Id
     */
    private String sellerId;
    /**
     * 促销推荐
     * 0不推荐1推荐
     */
    private String promotion;
    /**
     * 排序方式
     */
    @TableField(exist = false)
    private String orderFiled;
    private String rank;
}