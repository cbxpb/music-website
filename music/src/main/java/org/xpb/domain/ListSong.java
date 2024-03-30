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

    @TableId(type = IdType.AUTO)
    private Integer id;              //主键id
    private Integer songListId;      //歌单id
    private Integer songId;          //歌曲id
    @TableField(exist = false)
    private Song song;           //返回给前端歌曲信息
}
