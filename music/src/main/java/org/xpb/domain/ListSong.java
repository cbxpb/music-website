package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 歌单里面的歌曲表
 */
@Data
@TableName("list_song")
public class ListSong {
    /** 主键id 自动递增 **/
    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 歌单id **/
    private Integer songListId;
    /** 歌曲id **/
    private Integer songId;
    /** 返回给前端歌曲信息 **/
    @TableField(exist = false)
    private Song song;
}
