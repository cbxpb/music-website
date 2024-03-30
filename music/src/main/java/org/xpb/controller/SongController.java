package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.Singer;
import org.xpb.domain.Song;
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


    /**
     * 删除本地文件的操作
     * @param song
     */
    public void deletFile(Song song) {
        //删除歌曲图片
        if (!StrUtil.isBlank(song.getPicLocal())) {
            File file = new File(song.getPicLocal());
            if(file.exists())  {
                file.delete();
            }
        }
        //删除歌词文件
        if (!StrUtil.isBlank(song.getLyricLocal())) {
            File file = new File(song.getLyricLocal());
            if(file.exists())  {
                file.delete();
            }
        }
        //删除歌曲文件
        if (!StrUtil.isBlank(song.getUrlLocal())) {
            File file = new File(song.getUrlLocal());
            if(file.exists())  {
                file.delete();
            }
        }
    }


    /**
     * 添加歌曲信息，不涉及文件更新
     * @param song
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Song song) {
        try {
            song.setCreateTime(new Date());
            song.setUpdateTime(new Date());
            System.out.println(song);
            songService.save(song);
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
     * 修改歌曲信息，不涉及文件更新
     * @param song
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Song song) {
        try {
            System.out.println(song);
            song.setUpdateTime(new Date());
            songService.updateById(song);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("修改歌曲失败");
            } else {
                return Result.error("系统错误");
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
            System.out.println(id);
            System.out.println(downUrl);
            System.out.println(uploadUrl);
            System.out.println(type);
            Song song = songService.getById(id);
            System.out.println(song);
            //图片文件
            if (type == 3) {
                if (!StrUtil.isBlank(song.getPicLocal())) {
                    File file = new File(song.getPicLocal());
                    if(file.exists()) {
                        file.delete();
                    }
                }
                song.setPic(downUrl);
                song.setPicLocal(uploadUrl);
            }
            //歌曲文件
            if (type == 5) {
                if (!StrUtil.isBlank(song.getUrlLocal())) {
                    File file = new File(song.getUrlLocal());
                    if(file.exists()) {
                        file.delete();
                    }
                }
                song.setUrl(downUrl);
                song.setUrlLocal(uploadUrl);
            }
            //歌词文件
            if (type == 6) {
                if (!StrUtil.isBlank(song.getLyricLocal())) {
                    File file = new File(song.getLyricLocal());
                    if(file.exists()) {
                        file.delete();
                    }
                }
                song.setLyric(downUrl);
                song.setLyricLocal(uploadUrl);
            }
            songService.updateById(song);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("文件上传失败");
            } else {
                return Result.error("系统错误");
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
                return Result.error("查询歌曲信息失败");
            } else {
                return Result.error("系统错误");
            }
        }
    }

    /**
     * 删除歌曲信息
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            Song song = songService.getById(id);
            System.out.println(song);
//            //删除歌曲图片
//            if (!StrUtil.isBlank(song.getPicLocal())) {
//                File file = new File(song.getPicLocal());
//                if(file.exists())  {
//                    file.delete();
//                }
//            }
//            //删除歌词文件
//            if (!StrUtil.isBlank(song.getLyricLocal())) {
//                File file = new File(song.getLyricLocal());
//                if(file.exists())  {
//                    file.delete();
//                }
//            }
//            //删除歌曲文件
//            if (!StrUtil.isBlank(song.getUrlLocal())) {
//                File file = new File(song.getUrlLocal());
//                if(file.exists())  {
//                    file.delete();
//                }
//            }
            deletFile(song);
            songService.removeById(id);
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
     * 批量删除歌曲信息
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            for(Integer id : ids) {
                Song song = songService.getById(id);
                System.out.println(song);
                deletFile(song);
            }
            songService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("批量删除数据失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    @GetMapping("/lrc")
    public Result lrc(@RequestParam String url) {
        List<String> word = new ArrayList<>();
        String path = url;
        System.out.println(path);
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
            return Result.error();//只是测试用例，生产环境下不要这样做异常处理
        }
        return Result.success(word);
    }
}
