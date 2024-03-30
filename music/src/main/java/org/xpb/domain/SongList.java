package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 歌单表
 */
@Data
@TableName("song_list")
public class SongList {

    @TableId(type = IdType.AUTO)
    private Integer id;              //主键id
    private String title;            //歌单标题
    private String introduction;     //歌单简介
    private String style;            //歌单风格
    private String pic;              //图片下载地址
    private String picLocal;         //图片本地地址
}
