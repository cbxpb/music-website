package org.xpb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.xpb.domain.ListSong;

import java.util.List;

/**
 * 歌单歌曲Mapper层
 */
public interface ListSongMapper extends BaseMapper<ListSong> {


    //1.调用mapper层的方法，查询总数量
    Long selectCountByPage(Integer startIndex, Integer pageSize, String name, Integer songListId);

    //2.调用mapper层的方法，查询当前页的数据集合
    List<ListSong> selectByPage(Integer startIndex, Integer pageSize, String name, Integer songListId);

    // 通过歌单id查询对应的歌曲(不分页)
    List<ListSong> selectBySongListId(Integer songListId);
}
