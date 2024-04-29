package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.ListSong;
import org.xpb.domain.Song;
import org.xpb.service.ListSongService;
import org.xpb.service.SongService;
import org.xpb.util.PageList;

import javax.annotation.Resource;
import java.util.ArrayList;
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

    /**
     * 分页+高级查询
     * @param pageNum
     * @param pageSize
     * @param name
     * @param songListId
     * @return
     */
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
                return Result.error("500","查询歌单歌曲信息失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 模糊查询所有歌曲信息（分页）用于添加歌曲
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/selectByAllSongsPage")
    public Result selectByAllSongsPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam String album,
                               @RequestParam Integer songListId) {
        try {
            QueryWrapper<ListSong> listSongQueryWrapper = new QueryWrapper<ListSong>();
            listSongQueryWrapper.eq(null != songListId ,"song_list_id",songListId);
            List<ListSong> list = listSongService.list(listSongQueryWrapper);
            List<Integer> songIds = list.stream().map(item -> {
                return item.getSongId();
            }).collect(Collectors.toList());
            QueryWrapper<Song> queryWrapper = new QueryWrapper<Song>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
            queryWrapper.like(StrUtil.isNotBlank(album),"album",album);
            queryWrapper.notIn(songIds.size()>0,"id",songIds);
            Page<Song> page = songService.page(new Page<>(pageNum,pageSize),queryWrapper);
            return Result.success(page);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","查询歌曲信息失败");
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
            ListSong listSong = new ListSong();
            listSong.setSongId(songId);
            listSong.setSongListId(songListId);
            listSongService.save(listSong);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","添加歌单歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }
    /**
     * 批量添加歌单歌曲
     * @param songListId
     * @return
     */
    @PostMapping("/addAll")
    public Result addAll(@RequestParam Integer songListId,
                              @RequestParam String songIdsStr) {
        try {
            String[] songIds =  songIdsStr.split(",");
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
                return Result.error("500","批量添加歌单歌曲失败");
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
            listSongService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","删除歌单歌曲失败");
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
                return Result.error("500","批量删除歌单歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /** ==================================================客户端接口================================================== */

    /**
     * 通过歌单id查询对应的歌曲
     * @param songListId
     * @return
     */
    @GetMapping("/selectBySongListId")
    public Result selectBySongListId(@RequestParam Integer songListId) {
        try {
            List<ListSong> songs = listSongService.selectBySongListId(songListId);
            return Result.success(songs);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","查询歌单歌曲信息失败");
            } else {
                return Result.error();
            }
        }
    }
}
