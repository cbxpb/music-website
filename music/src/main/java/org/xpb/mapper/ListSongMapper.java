package org.xpb.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.xpb.domain.ListSong;
import org.xpb.domain.Song;

import java.util.List;

/**
 * 歌曲
 */
public interface ListSongMapper extends BaseMapper<ListSong> {


    //1.调用mapper层的方法，查询总数量
    Long selectCountByPage(Integer startIndex, Integer pageSize, String name, Integer songListId);

    //2.调用mapper层的方法，查询当前页的数据集合
    List<ListSong> selectByPage(Integer startIndex, Integer pageSize, String name, Integer songListId);

    //mybatisplus分页测试
    //Page<ListSong> selectPageOne(Page<ListSong> objectPage, QueryWrapper<Song> queryWrapper);
}
