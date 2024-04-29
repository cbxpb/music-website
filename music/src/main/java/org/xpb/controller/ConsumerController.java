package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.Collect;
import org.xpb.domain.Consumer;
import org.xpb.exception.ServiceException;
import org.xpb.service.CollectService;
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

    @Resource
    CollectService collectService;

    /**
     * 根据用户id，查询收藏表中对应的数据，并删除
     * @param id
     */
    public void deleteOther(Integer id) {
        //根据歌曲id，查询收藏表中对应的数据，并删除
        QueryWrapper<Collect> collectQueryWrapper = new QueryWrapper<Collect>();
        collectQueryWrapper.eq("consumer_id",id);
        collectService.remove(collectQueryWrapper);
    }

    /**
     * 用户统一的认证接口，接收post请求，执行登录业务
     * @param consumer 前端传递的登录表单数据（用户名和密码）
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Consumer consumer) {
        //1.后端参数校验，如果输入的参数不符合，则返回错误提示
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getPassword())){
            return Result.error("500","用户名或密码输入不合法");
        }
        //2.调用ConsumerService层的login方法，返回consumer信息和token到前端
        consumer = consumerService.login(consumer);
        return Result.success(consumer);
    }

    /**
     * 用户统一的认证接口，接收post请求，执行注册业务
     * @param consumer 前端传递的登录表单数据（用户名和密码）
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody Consumer consumer) {
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getPassword())){
            return Result.error("数据输入不合法！");
        }
        if (consumer.getUsername().length() > 10 || consumer.getPassword().length() > 20){
            return Result.error("数据输入不合法！");
        }
        consumer = consumerService.register(consumer);
        return Result.success(consumer);
    }

    /**
     * 添加前端用户
     * @param consumer
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Consumer consumer) {
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getPassword()) || StrUtil.isBlank(consumer.getGender())) {
            throw new ServiceException("500","数据输入不合法");
        }
        consumer.setRole("用户");
        consumer.setPassword(new BCryptPasswordEncoder().encode(consumer.getPassword()));
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());
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
     * 修改前端用户信息(不修改用户头像)
     * @param consumer
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Consumer consumer) {
        if (StrUtil.isBlank(consumer.getUsername()) || StrUtil.isBlank(consumer.getGender())) {
            throw new ServiceException("500","数据输入不合法");
        }
        if (StrUtil.isBlank(consumer.getPassword())) {
            Consumer dbConsumer = consumerService.getById(consumer.getId());
            consumer.setPassword(dbConsumer.getPassword());
        } else {
            consumer.setPassword(new BCryptPasswordEncoder().encode(consumer.getPassword()));
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
    public Result updateAvatar(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            /** 根据主键id查询用户数据 **/
            Consumer consumer = consumerService.getById(id);
            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(consumer.getAvatarLocal());
            /** 保存路径到consumer表中 **/
            consumer.setAvatar(downUrl);
            consumer.setAvatarLocal(uploadUrl);
            consumerService.updateById(consumer);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","用户头像更新失败");
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
            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(consumer.getAvatarLocal());
            //根据用户id，查询收藏表中对应的数据，并删除
            deleteOther(id);
            /** 删除consumer表中的数据 **/
            consumerService.removeById(id);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","删除用户失败");
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
                //根据用户id，查询收藏表中对应的数据，并删除
                deleteOther(id);
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

    /**=================================客户端接口==========================================**/

    /**
     * 修改前端用户密码
     * @param id,password,newPassword
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestParam Integer id,
                                 @RequestParam String password,
                                 @RequestParam String newPassword) {
        Consumer dbConsumer = consumerService.getById(id);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(password,dbConsumer.getPassword());
        if (!matches) {
            throw new ServiceException("原密码输入错误");
        }
        dbConsumer.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        consumerService.updateById(dbConsumer);
        return Result.success();
    }
}
