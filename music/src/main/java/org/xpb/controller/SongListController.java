package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.Song;
import org.xpb.domain.SongList;
import org.xpb.service.SongListService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 歌单管理controller,实现歌单相关的接口
 */
@RestController
@RequestMapping("/songList")
public class SongListController {

    @Resource
    SongListService songListService;


    /**
     * 删除本地文件的操作
     * @param songList
     */
    public void deletFile(SongList songList) {
        //删除歌曲图片
        if (!StrUtil.isBlank(songList.getPicLocal())) {
            File file = new File(songList.getPicLocal());
            if(file.exists())  {
                file.delete();
            }
        }
    }


    /**
     * 添加歌单信息，不涉及图片更新
     * @param songList
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody SongList songList) {
        try {
            System.out.println(songList);
            songListService.save(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("添加歌单失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改歌单信息，不涉及文件更新
     * @param songList
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody SongList songList) {
        try {
            System.out.println(songList);
            songListService.updateById(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("修改歌单失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改歌单信息，涉及图片更新
     * @param
     * @return
     */
    @PutMapping("/updateFile")
    public Result updateFile(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            System.out.println(id);
            System.out.println(downUrl);
            System.out.println(uploadUrl);
            SongList songList = songListService.getById(id);
            System.out.println("1"+songList);
            if (!StrUtil.isBlank(songList.getPicLocal())) {
                File file = new File(songList.getPicLocal());
                if(file.exists()) {
                    file.delete();
                }
            }
            songList.setPic(downUrl);
            songList.setPicLocal(uploadUrl);
            songListService.updateById(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("图片上传失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /**
     * 模糊查询所有歌单信息（分页）
     * @param pageNum
     * @param pageSize
     * @param title
     * @return
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String title,
                               @RequestParam String style) {
        try {
            QueryWrapper<SongList> queryWrapper = new QueryWrapper<SongList>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(title),"title",title);
            queryWrapper.like(StrUtil.isNotBlank(style),"style",style);
            Page<SongList> page = songListService.page(new Page<>(pageNum,pageSize),queryWrapper);
            return Result.success(page);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌单信息失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 删除歌单信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            SongList songList = songListService.getById(id);
            System.out.println(songList);
            //删除歌曲图片
            if (!StrUtil.isBlank(songList.getPicLocal())) {
                File file = new File(songList.getPicLocal());
                if(file.exists())  {
                    file.delete();
                }
            }
            songListService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("删除数据失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 批量删除歌单
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            for(Integer id : ids) {
                SongList songList = songListService.getById(id);
                System.out.println(songList);
                deletFile(songList);
            }
            songListService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("批量删除数据失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }
}
