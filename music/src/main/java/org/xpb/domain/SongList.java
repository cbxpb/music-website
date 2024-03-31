package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 歌单表
 */
@Data
@TableName("song_list")
public class SongList {
    /** 主键id 自动递增 **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 歌单标题 **/
    private String title;
    /** 歌单简介 **/
    private String introduction;
    /** 歌单风格 **/
    private String style;
    /** 图片下载地址 **/
    private String pic;
    /** 图片本地地址 **/
    private String picLocal;
}
