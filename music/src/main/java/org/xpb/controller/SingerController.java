package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.Collect;
import org.xpb.domain.ListSong;
import org.xpb.domain.Singer;
import org.xpb.domain.Song;
import org.xpb.service.CollectService;
import org.xpb.service.ListSongService;
import org.xpb.service.SingerService;
import org.xpb.service.SongService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 歌手Controller层
 */
@RestController
@RequestMapping("/singer")
public class SingerController {

    @Resource
    SingerService singerService;
    @Resource
    SongService songService;
    @Resource
    ListSongService listSongService;
    @Resource
    CollectService collectService;


    /**
     * 根据歌手id删除对应的歌曲表格中的数据和歌单歌曲表格中的数据以及收藏数据
     * @param id
     * @return
     */
    public void deleteOther(Integer id) {
        //1.通过歌手id查询其对应的歌曲，并删除对应的信息及文件
        QueryWrapper<Song> songWrapper = new QueryWrapper<Song>();
        songWrapper.eq("singer_id",id);
        List<Song> songList = songService.list(songWrapper);
        DeleteFile file = new DeleteFile();
        if(songList.size()>0) {
            for (Song song : songList) {
                file.deleteFile(song.getPicLocal());
                file.deleteFile(song.getLyricLocal());
                file.deleteFile(song.getUrlLocal());
                //2.根据歌曲id，查询ListSong中对应的数据，并删除
                QueryWrapper<ListSong> listSongQueryWrapper = new QueryWrapper<ListSong>();
                listSongQueryWrapper.eq("song_id",song.getId());
                listSongService.remove(listSongQueryWrapper);
                //3.根据歌曲id，查询收藏表中对应的数据，并删除
                QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<Collect>();
                collectQueryWrapper.eq("song_id",song.getId());
                collectService.remove(collectQueryWrapper);
            }
            songService.remove(songWrapper);
        }
        Singer singer = singerService.getById(id);
        file.deleteFile(singer.getPicLocal());
    }

    /**
     * 新增歌手信息
     * @param singer
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Singer singer) {
        if (StrUtil.isBlank(singer.getName()) || StrUtil.isBlank(singer.getGender())){
            return Result.error("500","请正确输入歌手信息");
        }
        try {
            if (StrUtil.isBlank(singer.getLocation())) {
                singer.setLocation("未知");
            }
            singerService.save(singer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","新增歌手失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /**
     * 修改歌手信息（不包含歌手图片）
     * @param singer
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Singer singer) {
        if (StrUtil.isBlank(singer.getName()) || StrUtil.isBlank(singer.getGender())){
            return Result.error("500","请正确输入歌手信息");
        }
        try {
            if (StrUtil.isBlank(singer.getLocation())) {
                singer.setLocation("未知");
            }
            singerService.updateById(singer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","修改歌手信息失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 歌手图片更新
     * @param downUrl
     * @param uploadUrl
     * @param id
     * @return
     */
    @PutMapping("/updatePic")
    public Result updatePic(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            /** 根据主键id查询歌手信息 **/
            Singer singer = singerService.getById(id);
            /** 删除存储的本地歌手图片 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(singer.getPicLocal());
            /** 保存路径到singer表中 **/
            singer.setPic(downUrl);
            singer.setPicLocal(uploadUrl);
            singerService.updateById(singer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","歌手图片更新失败");
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
            //调用deleteOther函数删除其他表格数据及对应的本地文件
            deleteOther(id);
            singerService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","删除歌手失败");
            } else {
                return Result.error();
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
                deleteOther(id);
            }
            singerService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","批量删除歌手失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
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
                return Result.error("500","查询歌手信息失败");
            } else {
                return Result.error();
            }
        }
    }


    /** ==================================================客户端接口================================================== */

    /**
     * 收集所有歌手的地址，返回给前端做导航栏
     * @return
     */
    @GetMapping("/getAllLocation")
    public Result getAllLocation() {
        try {
            List<Singer> listSinger = singerService.list();
            // 收集所有歌手的地址，返回给前端做导航栏
            List<String> locations = listSinger.stream().map(Singer::getLocation).distinct().collect(Collectors.toList());
            return Result.success(locations);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","获取歌手地址失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 收集所有歌手的性别，返回给前端做导航栏
     * @return
     */
    @GetMapping("/getAllGender")
    public Result getAllGender() {
        try {
            List<Singer> listSinger = singerService.list();
            // 收集所有歌手的性别，返回给前端做导航栏
            List<String> genders = listSinger.stream().map(Singer::getGender).distinct().collect(Collectors.toList());
            return Result.success(genders);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","获取歌手性别失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 查询全部歌手信息（不分页）
     * @return
     */
    @GetMapping("/selectAll")
    public Result selectAll(@RequestParam String location,
                            @RequestParam String gender) {
        try {
            QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>().orderByDesc("id");
            queryWrapper.eq(StrUtil.isNotBlank(location),"location",location);
            queryWrapper.eq(StrUtil.isNotBlank(gender),"gender",gender);
            List<Singer> list = singerService.list(queryWrapper);
            return Result.success(list);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("查询歌手失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 模糊搜索歌手（不分页）,
     * @param name
     * @return
     */
    @GetMapping("/selectByName")
    public Result selectByName(@RequestParam String name) {
        try {
            QueryWrapper<Singer> queryWrapper = new QueryWrapper<Singer>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(name),"name",name);
            List<Singer> list = singerService.list(queryWrapper);
            return Result.success(list);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","搜索歌手失败");
            } else {
                return Result.error();
            }
        }
    }
}
