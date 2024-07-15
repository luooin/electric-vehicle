package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 实体类
 */
@Data
@TableName("gm_evaluate")
public class Evaluate implements Serializable {
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
     * 评价内容
     */
    private String content;
    /**
     * 评价图片
     */
    private String imgs;
    /**
     * 评分
     */
    private Integer grade;
    /**
     * 评价人
     */
    private String userName;
    /**
     * 用户头像
     */
    private String userImg;
    /**
     * 评价时间
     */
    private String time;
    /**
     * 回复内容
     */
    private String reply;
    /**
     * 评价图片
     */
    @TableField(exist = false)
    private List<String> imgList;
    /**
     * 回复
     */
    @TableField(exist = false)
    private List<Reply> replyList;
}