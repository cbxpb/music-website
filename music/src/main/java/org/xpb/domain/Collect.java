package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 用户收藏表
 */
@Data
@TableName("collect")
public class Collect {
    /** 主键id 自动递增 **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 用户id **/
    private Integer consumerId;
    /** 歌曲id **/
    private Integer songId;
    /** 创建时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date createTime;
}
