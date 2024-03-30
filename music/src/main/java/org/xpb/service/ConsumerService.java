package org.xpb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.xpb.domain.Consumer;
import org.xpb.mapper.ConsumerMapper;

/**
 * 前端用户Service层
 */
@Service
public class ConsumerService extends ServiceImpl<ConsumerMapper, Consumer> {

}
