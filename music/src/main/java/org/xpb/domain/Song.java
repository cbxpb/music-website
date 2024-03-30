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

    @TableId(type = IdType.AUTO)
    private Integer id;              //主键id
    private Integer singerId;       //歌手id
    private String name;            //歌曲名称
    private String album;           //专辑
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date createTime;        //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date updateTime;        //修改时间
    private String pic;             //图片下载地址
    private String lyric;           //歌词下载地址
    private String url;             //歌曲下载地址
    private String picLocal;       //图片本地地址
    private String lyricLocal;     //歌词本地地址
    private String urlLocal;       //歌曲本地地址

}
