package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
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

    /** 主键id 自动递增 **/
    @TableId(type=IdType.AUTO)
    private Integer id;

    /** 歌手姓名 **/
    private String name;

    /** 歌手性别（男，女，组合，不明） **/
    private String gender;

    /** 歌手生日 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date birth;

    /** 歌手所属地区 **/
    private String location;

    /** 歌手简介 **/
    private String introduction;

    /** 歌手图片下载地址 **/
    private String pic;

    /** 歌手图片本地地址 **/
    private String picLocal;
}
