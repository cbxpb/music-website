package org.xpb.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.Singer;
import org.xpb.service.SingerService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Queue;


@RestController
@RequestMapping("/singer")
public class SingerController {

    @Resource
    SingerService singerService;

//    /**
//     * 分页查询
//     */
//    @GetMapping("/selectByPage")
//    public Result selecByPage(@RequestParam Integer page,@RequestParam Integer limit){
//        PageList<Singer> pageList = singerService.selectByPage(page,limit);
//        return Result.success(pageList);
//    }
//
//    /**
//     * 新增操作
//     */
//    @PostMapping("/add")
//    public Result insert(@RequestBody Singer singer){
//        if(StrUtil.isBlank(singer.getName())){
//            return Result.error("歌手名字不能为空");
//        }
//        if (!("男".equals(singer.getSex()) || "女".equals(singer.getSex()))){
//            return Result.error("歌手性别错误");
//        }
//        if (singer.getId() != null){
//            return Result.error("数据来源不合法！");
//        }
//        boolean flag = singerService.insert(singer);
//        if(!flag){
//            return Result.error("新增歌手失败");
//        }
//        return Result.success("新增歌手成功");
//    }
//
//    /**
//     * 新增操作
//     */
//    @PutMapping("/update")
//    public Result update(@RequestBody Singer singer){
//        boolean flag = singerService.update(singer);
//        if(!flag){
//            return Result.error("修改歌手失败");
//        }
//        return Result.success("修改歌手成功");
//    }

    /**
     * 新增歌手信息
     * @param singer
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Singer singer) {
        try {
            singerService.save(singer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("插入数据失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /**
     * 修改歌手信息
     * @param singer
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Singer singer) {
        try {
            singerService.updateById(singer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("修改数据失败");
            } else {
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }

    /**
     * 删除歌手信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            Singer singer = singerService.getById(id);
            System.out.println(singer);
            if (!StrUtil.isBlank(singer.getUrl())) {
                File file = new File(singer.getUrl());
                if(file.exists())  {
                    file.delete();
                }
            }
            singerService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("删除数据失败");
            } else {
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }

    /**
     * 批量删除歌手信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            for(Integer id : ids) {
                Singer singer = singerService.getById(id);
                System.out.println(singer);
                if (!StrUtil.isBlank(singer.getUrl())) {
                    File file = new File(singer.getUrl());
                    if(file.exists())  {
                        file.delete();
                    }
                }
            }
            singerService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("批量删除数据失败");
            } else {
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }

    /**
     * 查询全部歌手信息（不分页）
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll() {
        try {
            List<Singer> list = singerService.list(new QueryWrapper<Singer>().orderByDesc("id"));
            return Result.success(list);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌手信息失败");
            } else {
                return Result.error("系统错误");
            }
        }
    }

    /**
     * 模糊查询全部歌手信息（分页）
     * @param pageNum
     * @param pageSize
     * @param name
     * @param gender
     * @return
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam String gender) {
        try {
            QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
            queryWrapper.eq(StrUtil.isNotBlank(gender),"gender",gender);
            Page<Singer> page = singerService.page(new Page<>(pageNum,pageSize),queryWrapper);
            return Result.success(page);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌手信息失败");
            } else {
                return Result.error("系统错误");
            }
        }
    }

}
