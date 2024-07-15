package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 图片信息
 */
@Data
@TableName("gm_slideshow")
public class Slideshow implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 图片地址
     */
    private String url;

    /**
     * 上传时间
     */
    private String time;
    /**
     * 上传人
     */
    private String userName;
    /**
     * 状态
     */
    private String state;
    private String productId;
}