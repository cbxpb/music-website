package org.xpb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xpb.domain.ListSong;
import org.xpb.domain.Song;
import org.xpb.mapper.ListSongMapper;
import org.xpb.mapper.SongMapper;
import org.xpb.util.PageList;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ListSongService extends ServiceImpl<ListSongMapper, ListSong> {

    //分页+高级查询
    public PageList<ListSong> selectByPage(Integer pageNum, Integer pageSize, String name, Integer songListId) {
        Integer startIndex =  (pageNum-1) * pageSize;
        //1.调用mapper层的方法，查询总数量
        Long total = baseMapper.selectCountByPage(startIndex,pageSize,name,songListId);
        //2.调用mapper层的方法，查询当前页的数据集合
        List<ListSong> list = baseMapper.selectByPage(startIndex,pageSize,name,songListId);
        return new PageList<ListSong>(total, list);
    }

//mybatisplus分页测试
//    public Page<ListSong> pageOne(Page<ListSong> objectPage, QueryWrapper<Song> queryWrapper) {
//        return baseMapper.selectPageOne(objectPage,queryWrapper);
//    }
}
