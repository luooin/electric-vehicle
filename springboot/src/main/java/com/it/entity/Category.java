package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 车辆分类
 */
@Data
@TableName("gm_category")
public class Category implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 父id
     */
    private String parentId;
    /**
     * 排序
     */
    private String priority;
    private String rank;
}