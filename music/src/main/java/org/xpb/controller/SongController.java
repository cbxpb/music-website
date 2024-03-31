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
import org.xpb.service.ListSongService;
import org.xpb.service.SongService;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * 歌曲管理controller,实现歌曲相关的接口
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Resource
    SongService songService;
    @Resource
    ListSongService listSongService;


    /**
     * 根据歌曲id删除歌单歌曲表格中的数据及本地文件
     * @param id
     */
    public void deleteOther(Integer id) {
        //根据歌曲id删除歌单歌曲表格中的数据及本地文件
        DeleteFile file = new DeleteFile();
        QueryWrapper<ListSong> queryWrapper = new QueryWrapper<ListSong>();
        queryWrapper.eq("song_id",id);
        listSongService.remove(queryWrapper);
        Song song = songService.getById(id);
        file.deleteFile(song.getPicLocal());
        file.deleteFile(song.getLyricLocal());
        file.deleteFile(song.getUrlLocal());
    }


    /**
     * 添加歌曲信息，不涉及文件更新
     * @param song
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Song song) {
        if (StrUtil.isBlank(song.getName())){
            return Result.error("500","请正确输入歌曲信息");
        }
        try {
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            songService.save(song);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","添加歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改歌曲信息，不涉及文件更新
     * @param song
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Song song) {
        if (StrUtil.isBlank(song.getName())){
            return Result.error("500","请正确输入歌曲信息");
        }
        try {
            song.setUpdateTime(new Date());
            songService.updateById(song);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","修改歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改歌曲信息，涉及文件更新
     * @param
     * @return
     */
    @PutMapping("/updateFile")
    public Result updateFile(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id,
                             @RequestParam Integer type) {
        try {
            Song song = songService.getById(id);
            DeleteFile file = new DeleteFile();
            //歌曲图片文件
            if (type == 4) {
                file.deleteFile(song.getPicLocal());
                song.setPic(downUrl);
                song.setPicLocal(uploadUrl);
            }
            //歌曲文件
            if (type == 6) {
                file.deleteFile(song.getUrlLocal());
                song.setUrl(downUrl);
                song.setUrlLocal(uploadUrl);
            }
            //歌词文件
            if (type == 7) {
                file.deleteFile(song.getLyricLocal());
                song.setLyric(downUrl);
                song.setLyricLocal(uploadUrl);
            }
            songService.updateById(song);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","文件上传失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 删除歌曲信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            deleteOther(id);
            songService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","删除歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }
    /**
     * 批量删除歌曲信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            for(Integer id : ids) {
                deleteOther(id);
            }
            songService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","批量删除歌曲失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 模糊查询对应歌手歌曲信息（分页）,如果不传歌手id，则是查询全部歌曲
     * @param pageNum
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String name,
                               @RequestParam String album,
                               @RequestParam Integer singerId) {
        try {
            QueryWrapper<Song> queryWrapper = new QueryWrapper<Song>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
            queryWrapper.like(StrUtil.isNotBlank(album),"album",album);
            queryWrapper.eq(null != singerId ,"singer_id",singerId);
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
     * 读取歌词文件，返回歌词数组到前端
     * @param url
     * @return
     */
    @GetMapping("/lrc")
    public Result lrc(@RequestParam String url) {
        List<String> word = new ArrayList<>();
        String path = url;
        Path filePath = Paths.get(path);
        try (Stream<String> lines = Files.lines( filePath ))
        {
            lines.forEach(item -> {
                if (StrUtil.isNotEmpty(item)){
                    word.add(item);
                }
            });
        }
        catch (IOException e)
        {
            return Result.error();
        }
        return Result.success(word);
    }
}
