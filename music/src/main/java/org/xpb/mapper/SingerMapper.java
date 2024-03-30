package org.xpb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.xpb.domain.Singer;

import java.util.List;

/**
 * 歌手
 */
public interface SingerMapper extends BaseMapper<Singer> {


//    /**
//     * 分页查询歌手信息
//     * @param StartIndex = (page-1)*limit
//     * @param limit
//     * @return
//     */
//    @Select("select * from `singer` order by id desc limit #{StartIndex},#{limit} ")
//    List<Singer> selectByPage(Integer StartIndex, Integer limit);

//    /**
//     * 查询总数量
//     * @return
//     */
//    @Select("select count(id) from `singer` order by id desc")
//    int selectCount();
//
//    /**
//     * 新增操作
//     * @param singer
//     */
//    @Insert("insert into `singer` (name, sex, pic, birth, location, introduction)" +
//            "values (#{name}, #{sex}, #{pic}, #{birth}, #{location}, #{introduction})")
//    int insert(Singer singer);
//
//
//    /**
//     * 修改操作
//     * @param singer
//     */
//    @Update("update `singer` set name = #{name}, sex = #{sex}, pic = #{pic}, birth = #{birth}," +
//            "location = #{location}, introduction = #{introduction} where id = #{id}")
//    int update(Singer singer);
//
//
//    /**
//     * 删除操作
//     * @param id
//     */
//    @Delete("delete from `singer` where id = #{id}")
//    void delete(Integer id);
//
//    /**
//     * 根据主键查询整个对象
//     * @param id
//     * @return
//     */
//    @Select("select * from `singer` where id = #{id}")
//    Singer selectById(Integer id);
//
//
//    /**
//     * 查询所有的歌手信息，不分页
//     * @return
//     */
//    @Select("select * from `singer` order by id desc")
//    List<Singer> selectAll();
//
//    /**
//     * 根据歌手姓名模糊查询
//     * @param name
//     * @return
//     */
//    @Select("select * from `singer` where name like concat('%',#{name},'%') order by id desc")
//    List<Singer> singerofName(String name);
//
//    /**
//     * 根据歌手性别查询
//     * @param sex
//     * @return
//     */
//    @Select("select * from `singer` where sex = #{sex} order by id desc")
//    List<Singer> singerofSex(Integer sex);

}
