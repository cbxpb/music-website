package org.xpb.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.xpb.common.Result;
import org.xpb.domain.Singer;
import org.xpb.mapper.SingerMapper;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SingerService extends ServiceImpl<SingerMapper,Singer> {

//    @Resource
//    SingerMapper singerMapper;



//    public PageList<Singer> selectByPage(Integer page,Integer limit) {
//        Integer StartIndex = (page-1)*limit;
//        //1.查询总数量
//        Integer total = singerMapper.selectCount();
//        //2.查询数据集合
//        List<Singer> list = singerMapper.selectByPage(StartIndex,limit);
//        return new PageList<Singer>(total,list);
//    }
//
//    /**
//     * 新增操作
//     * @param singer
//     */
//    public boolean insert(Singer singer) {
//        return singerMapper.insert(singer)>0;
//    }
//
//    /**
//     * 修改操作
//     * @param singer
//     */
//    public boolean update(Singer singer) {
//        return singerMapper.update(singer)>0;
//    }
//
//    /**
//     * 删除操作
//     * @param id
//     */
////    public boolean delete(Integer id) {
////
////    }


}
