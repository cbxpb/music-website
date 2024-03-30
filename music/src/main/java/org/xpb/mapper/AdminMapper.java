package org.xpb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.xpb.domain.Admin;

/**
 * 管理员Mapper层
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

}
