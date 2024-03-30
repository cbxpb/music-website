package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.ListSong;
import org.xpb.domain.Singer;
import org.xpb.domain.Song;
import org.xpb.service.ListSongService;
import org.xpb.service.SongService;
import org.xpb.util.PageList;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 歌单歌曲管理controller,实现歌单歌曲相关的接口
 */
@RestController
@RequestMapping("/listSong")
public class ListSongController {
    @Resource
    ListSongService listSongService;

    @Resource
    SongService songService;
    //分页+高级查询
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam Integer songListId){
        try {
            PageList<ListSong> pageList = listSongService.selectByPage(pageNum,pageSize,name,songListId);
            return Result.success(pageList);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌单歌曲信息失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 模糊查询所有歌曲信息（分页）
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/selectByAllSongsPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam String album,
                               @RequestParam Integer songListId) {
        try {
            QueryWrapper<ListSong> listSongQueryWrapper = new QueryWrapper<ListSong>();
            listSongQueryWrapper.eq(null != songListId ,"song_list_id",songListId);
            List<ListSong> list = listSongService.list(listSongQueryWrapper);
//            System.out.println(list);
            //List<Integer> songIds = list.stream().map(ListSong::getSongId).collect(Collectors.toList());
            List<Integer> songIds = list.stream().map(item -> {
                return item.getSongId();
            }).collect(Collectors.toList());
            System.out.println(songIds.size());
            QueryWrapper<Song> queryWrapper = new QueryWrapper<Song>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
            queryWrapper.like(StrUtil.isNotBlank(album),"album",album);
            queryWrapper.notIn(songIds.size()>0,"id",songIds);
//            System.out.println(songIds.size()>0);
            Page<Song> page = songService.page(new Page<>(pageNum,pageSize),queryWrapper);
            return Result.success(page);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌曲信息失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 添加歌曲
     * @param songId
     * @return
     */
    @PostMapping("/addOne")
    public Result add(@RequestParam Integer songId,
                      @RequestParam Integer songListId) {
        try {
            //还可以验证数据库中是否已经添加了这条数据  还没做
            System.out.println(songId);
            System.out.println(songListId);
            ListSong listSong = new ListSong();
            listSong.setSongId(songId);
            listSong.setSongListId(songListId);
            listSongService.save(listSong);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("添加歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }
    /**
     * 批量删除歌曲信息
     * @param songListId
     * @return
     */
    @PostMapping("/addAll")
    public Result batchDelete(@RequestParam Integer songListId,
                              @RequestParam String songIdsStr) {
        try {
            System.out.println(songIdsStr);
            System.out.println(songListId);
            String[] songIds =  songIdsStr.split(",");
            System.out.println(songIds);
            List<ListSong> list = new ArrayList<>();
            for(String songId : songIds) {
                ListSong listSong = new ListSong();
                listSong.setSongListId(songListId);
                listSong.setSongId(Integer.valueOf(songId));
                list.add(listSong);
            }
            listSongService.saveBatch(list);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("批量添加数据失败");
            } else {
                return Result.error();
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
            System.out.println(id);
            listSongService.removeById(id);
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
     * 批量删除歌曲
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            listSongService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("批量删除数据失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }
//    mybatisplus分页测试
//    @GetMapping("/selectByPageOne")
//    public Result selectByPage(@RequestParam Integer pageNum,
//                               @RequestParam Integer pageSize,
//                               @RequestParam String name,
//                               @RequestParam String songListId) {
//        try {
//            QueryWrapper<Song> queryWrapper = new QueryWrapper<Song>();
//            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
//            queryWrapper.eq(null != songListId ,"song_list_id",songListId);
//            Page<ListSong> page = listSongService.pageOne(new Page<>(pageNum,pageSize),queryWrapper);
//            return Result.success(page);
//        } catch (Exception e) {
//            if (e instanceof DuplicateKeyException){
//                return Result.error("查询歌手信息失败");
//            } else {
//                return Result.error();
//            }
//        }
//    }
}
