package org.xpb.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.Result;
import org.xpb.domain.Collect;
import org.xpb.service.CollectService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 收藏controller,实现歌曲收藏相关的接口
 */
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Resource
    CollectService collectService;

    /**
     * 根据consumerId、songId查询是否已经收藏了歌曲
     * @param consumerId、songId
     * @return
     */
    public Collect  existCollect(Integer consumerId,Integer songId) {
        QueryWrapper<Collect> queryWrapper = new QueryWrapper<Collect>();
        queryWrapper.eq(null != consumerId,"consumer_id",consumerId);
        queryWrapper.eq(null != songId,"song_id",songId);
        Collect collect = collectService.getOne(queryWrapper);
        return  collect;
    }

    /**
     * 添加收藏
     * @param
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestParam Integer consumerId,
                      @RequestParam Integer songId) {
        try {
            // 判断是否已经收藏
            Collect song = existCollect(consumerId,songId);
            if (song != null) {
                return Result.error("500","该歌曲已收藏");
            }else {
                Collect collect = new Collect();
                collect.setConsumerId(consumerId);
                collect.setSongId(songId);
                collect.setCreateTime(new Date());
                collectService.save(collect);
            }
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","歌曲收藏失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 取消收藏前端执行
     * @param consumerId,songId
     * @return
     */
    @DeleteMapping("/deleteSong")
    public Result deleteSong(@RequestParam Integer consumerId,
                         @RequestParam Integer songId) {
        try {
            QueryWrapper<Collect> queryWrapper = new QueryWrapper<Collect>();
            queryWrapper.eq(null != consumerId,"consumer_id",consumerId);
            queryWrapper.eq(null != songId,"song_id",songId);
            collectService.remove(queryWrapper);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","取消收藏失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 取消收藏
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            collectService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","取消收藏失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 批量取消收藏
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            collectService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","批量取消收藏失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /** ==================================================客户端接口================================================== */

    /**
     * 通过用户id查询对应的收藏
     * @param consumerId
     * @return
     */
    @GetMapping("/selectByConsumerId")
    public Result selectByConsumerId(@RequestParam Integer consumerId) {
        try {
            QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<Collect>();
            collectQueryWrapper.eq(null != consumerId,"consumer_id",consumerId);
            List<Collect> list = collectService.list(collectQueryWrapper);
            return Result.success(list);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","用户收藏查询失败");
            } else {
                return Result.error();
            }
        }
    }

    /**
     * 查询用户是否已收藏了某个歌曲
     * @param consumerId
     * @param songId
     * @return
     */
    @GetMapping("/existSong")
    public Result existSong(@RequestParam Integer consumerId,
                              @RequestParam Integer songId) {
        try {
            Collect collect = existCollect(consumerId,songId);
            if (collect == null) {
                return Result.success("false");
            }else {
                return Result.success(collect);
            }
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","查询失败");
            } else {
                return Result.error();
            }
        }
    }
}
