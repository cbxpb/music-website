package org.xpb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.xpb.domain.Song;

import java.util.List;

/**
 * 歌曲
 */
public interface SongMapper extends BaseMapper<Song> {




//    /**
//     * 修改
//     */
//    public int update(@Param("song") Song song);
//
//    /**
//     * 删除
//     */
//    public int delete(Integer id);
//
//    /**
//     * 根据主键查询整个对象
//     */
//    public Song selectByID(Integer id);
//
//    /**
//     * 查询所有歌曲
//     */
//    public List<Song> allSong();
//
//    /**
//     * 根据歌名模糊查询列表
//     */
//    public List<Song> songOfName(String name);
//
//    /**
//     * 根据歌手id查询
//     */
//    public List<Song> songOfSingerId(Integer singerId);
}
