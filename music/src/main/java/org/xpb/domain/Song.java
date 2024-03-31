package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 歌曲表
 */
@Data
@TableName("song")
public class Song {

    /** 主键id自动递增 **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 歌手id **/
    private Integer singerId;
    /** 歌曲名称 **/
    private String name;
    /** 歌曲专辑 **/
    private String album;
    /** 创建时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date createTime;
    /** 修改时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date updateTime;
    /** 图片下载地址 **/
    private String pic;
    /** 歌词下载地址 **/
    private String lyric;
    /** 歌曲下载地址 **/
    private String url;
    /** 图片本地地址 **/
    private String picLocal;
    /** 歌词本地地址 **/
    private String lyricLocal;
    /** 歌曲本地地址 **/
    private String urlLocal;

}
