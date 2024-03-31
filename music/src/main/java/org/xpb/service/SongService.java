package org.xpb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xpb.domain.Song;
import org.xpb.mapper.SongMapper;

/**
 * 歌曲Service层
 */
@Service
public class SongService extends ServiceImpl<SongMapper, Song> {

}
