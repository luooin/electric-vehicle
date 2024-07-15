package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 异常状态实体类
 */
@Data
@TableName("gm_abnormal")
public class Abnormal implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 异常名称
     */
    private String name;
    /**
     * 异常类型
     */
    private String type;
    /**
     * 异常说明
     */
    private String content;
    /**
     * 时间
     */
    private String time;
    /**
     * 状态
     */
    private String state;
    private String userName;
    private String sellName;
}