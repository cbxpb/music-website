package org.xpb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xpb.domain.Singer;
import org.xpb.mapper.SingerMapper;

/**
 * 歌手Service层
 */
@Service
public class SingerService extends ServiceImpl<SingerMapper,Singer> {

}
