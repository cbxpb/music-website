package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.ListSong;
import org.xpb.domain.Singer;
import org.xpb.domain.Song;
import org.xpb.domain.SongList;
import org.xpb.service.ListSongService;
import org.xpb.service.SongListService;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

/**
 * 歌单管理Controller层,实现歌单相关的接口
 */
@RestController
@RequestMapping("/songList")
public class SongListController {

    @Resource
    SongListService songListService;
    @Resource
    ListSongService listSongService;

    /**
     * 根据歌单id删除歌单歌曲表格中的数据及本地文件
     * @param id
     */
    public void deleteOther(Integer id) {
        //根据歌曲id删除歌单歌曲表格中的数据及本地文件
        DeleteFile file = new DeleteFile();
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<ListSong>();
        queryWrapper.eq("song_list_id",id);
        listSongService.remove(queryWrapper);
        SongList songList = songListService.getById(id);
        file.deleteFile(songList.getPicLocal());
    }


    /**
     * 添加歌单信息，不涉及图片更新
     * @param songList
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody SongList songList) {
        if (StrUtil.isBlank(songList.getTitle())){
            return Result.error("500","请正确输入歌单信息");
        }
        try {
            if (StrUtil.isBlank(songList.getStyle())) {
                songList.setStyle("未知");
            }
            songListService.save(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","添加歌单失败");
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
        if (StrUtil.isBlank(songList.getTitle())){
            return Result.error("500","请正确输入歌单信息");
        }
        try {
            if (StrUtil.isBlank(songList.getStyle())) {
                songList.setStyle("未知");
            }
            songListService.updateById(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","修改歌单失败");
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
    @PutMapping("/updatePic")
    public Result updateFile(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            /** 根据主键id查询歌单信息 **/
            SongList songList = songListService.getById(id);
            /** 删除存储的本地歌手图片 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(songList.getPicLocal());
            /** 保存路径到songList表中 **/
            songList.setPic(downUrl);
            songList.setPicLocal(uploadUrl);
            songListService.updateById(songList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","歌单图片更新失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 删除歌单信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            deleteOther(id);
            songListService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","删除歌单失败");
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
                deleteOther(id);
            }
            songListService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","批量删除歌单失败");
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
                return Result.error("500","查询歌单信息失败");
            } else {
                return Result.error();
            }
        }
    }
}
