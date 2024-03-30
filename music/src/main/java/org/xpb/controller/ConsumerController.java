package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.Consumer;
import org.xpb.exception.ServiceException;
import org.xpb.service.ConsumerService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 前端用户Controller层
 */
@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    @Resource
    ConsumerService consumerService;


    /**
     * 添加前端用户
     * @param consumer
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Consumer consumer) {
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getPassword())) {
            throw new ServiceException("500","数据输入不合法");
        }
        consumer.setRole("用户");
        consumer.setCreateTime(new Date());
        try {
            consumerService.save(consumer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","新增用户失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改前端用户信息
     * @param consumer
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Consumer consumer) {
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getPassword())) {
            throw new ServiceException("500","数据输入不合法");
        }
        consumer.setUpdateTime(new Date());
        try {
            consumerService.updateById(consumer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","修改用户失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }


    /**
     * 前端用户头像更新
     * @param downUrl
     * @param uploadUrl
     * @param id
     * @return
     */
    @PutMapping("/updateAvatar")
    public Result updateFile(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            System.out.println(id);
            System.out.println(downUrl);
            System.out.println(uploadUrl);
            /** 根据主键id查询用户数据 **/
            Consumer consumer = consumerService.getById(id);
            System.out.println(consumer);

            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(consumer.getAvatarLocal());
            /** 保存路径到consumer表中 **/
            consumer.setAvatar(downUrl);
            consumer.setAvatarLocal(uploadUrl);
            consumerService.updateById(consumer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","头像更新失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 根据id单个删除前端用户
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        try {
            /** 根据主键id查询用户数据 **/
            Consumer consumer = consumerService.getById(id);
            System.out.println(consumer);
            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(consumer.getAvatarLocal());
            /** 删除consumer表中的数据 **/
            consumerService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","数据删除失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 根据ids批量删除前端用户
     * @param ids
     * @return
     */
    @DeleteMapping("/delete/batch")
    public Result batchDelete(@RequestBody List<Integer> ids) {
        try {
            DeleteFile file = new DeleteFile();
            for(Integer id : ids) {
                Consumer consumer = consumerService.getById(id);
                file.deleteFile(consumer.getAvatarLocal());
            }
            consumerService.removeBatchByIds(ids);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","批量删除用户失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 模糊查询全部用户信息（分页）
     * @param pageNum
     * @param pageSize
     * @param username
     * @param gender
     * @return
     */
    @GetMapping("/selectByPage")
    public Result selectByPage(@RequestParam Integer pageNum,
                               @RequestParam Integer pageSize,
                               @RequestParam String username,
                               @RequestParam String gender) {
        try {
            QueryWrapper<Consumer> queryWrapper = new QueryWrapper<Consumer>().orderByDesc("id");
            queryWrapper.like(StrUtil.isNotBlank(username),"username",username);
            queryWrapper.eq(StrUtil.isNotBlank(gender),"gender",gender);
            Page<Consumer> page = consumerService.page(new Page<>(pageNum,pageSize),queryWrapper);
            return Result.success(page);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","查询用户失败");
            } else {
                return Result.error();
            }
        }
    }

}
