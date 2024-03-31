package org.xpb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xpb.domain.SongList;
import org.xpb.mapper.SongListMapper;

/**
 * 歌单Service层
 */
@Service
public class SongListService extends ServiceImpl<SongListMapper, SongList> {

}
