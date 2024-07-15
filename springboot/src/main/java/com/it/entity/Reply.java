package com.it.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体类
 */
@Data
@TableName("gm_reply")
public class Reply implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复时间
     */
    private String time;
    /**
     * 回复人
     */
    private String userName;
    /**
     * 回复人头像
     */
    private String userImg;
    /**
     * 留言id
     */
    private String leaveId;
}