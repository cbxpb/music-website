package org.xpb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.xpb.domain.Admin;

import java.util.List;

@Mapper
public interface AdminMapper extends BaseMapper<Admin> {

//    //查询管理员登录账号
//    @Select("select * from `admin` where name = #{name}")
//    Admin selectByname(String name);
//
//    //注册账号
//    @Insert("insert into `admin` (name, password)" + "values (#{name},#{password})")
//    void insert(Admin admin);
//
//    @Select("select * from `admin` where id = #{id}")
//    Admin selectById(Integer id);
//
//
//    @Select("select * from `admin`")
//    List<Admin> selectAll();
}
