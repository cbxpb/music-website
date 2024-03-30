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
 * 歌手表
 */
@Data
@TableName("singer")
public class Singer {
    @TableId(type=IdType.AUTO)
    private Integer id;
    private String name;
    private String gender;
    private String pic;     //显示图片地址
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date birth;
    private String location;
    private String introduction;
    private String filename;    //文件名
    private String url;         //本地存储路径

}
