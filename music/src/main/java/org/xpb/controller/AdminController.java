package org.xpb.controller;


import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.Admin;
import org.xpb.domain.Consumer;
import org.xpb.exception.ServiceException;
import org.xpb.service.AdminService;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

/**
 * 管理员Controller层
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;


    /**
     * 管理员统一的认证接口，接收post请求，执行登录业务
     * @param admin 前端传递的登录表单数据（用户名和密码）
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody Admin admin) {
        //1.后端参数校验，如果输入的参数不符合，则返回错误提示
        if (StrUtil.isBlank(admin.getUsername()) || StrUtil.isBlank(admin.getPassword())){
            return Result.error("500","用户名或密码输入不合法");
        }
        //2.调用AdminService层的login方法，返回admin信息和token到前端
        admin = adminService.login(admin);
        System.out.println(admin);
        return Result.success(admin);
    }

//    @PostMapping("/admin/register")
//    public Result register(@RequestBody Admin admin) {
//        if (StrUtil.isBlank(admin.getUsername()) || StrUtil.isBlank(admin.getPassword())){
//            return Result.error("数据输入不合法！");
//        }
//        if (admin.getUsername().length() > 10 || admin.getPassword().length() > 20){
//            return Result.error("数据输入不合法！");
//        }
//        admin = adminService.register(admin);
//        return Result.success(admin);
//    }


    /**
     * 修改管理员信息
     * @param admin
     * @return
     */
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        if (StrUtil.isBlank(admin.getUsername())) {
            throw new ServiceException("500","数据输入不合法");
        }
        try {
            Admin dbAdmin = adminService.getById(admin.getId());
            admin.setPassword(dbAdmin.getPassword());
            adminService.updateById(admin);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","修改管理员信息失败");
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
            /** 根据主键id查询用户数据 **/
            Admin admin = adminService.getById(id);
            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(admin.getAvatarLocal());
            /** 保存路径到consumer表中 **/
            admin.setAvatar(downUrl);
            admin.setAvatarLocal(uploadUrl);
            adminService.updateById(admin);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","头像更新失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

//    /**
//     * 根据id查询管理员信息
//     * @param id
//     * @return
//     */
//    @GetMapping("/selectById/{id}")
//    public Result selectByPage(@PathVariable Integer id) {
//        try {
//            Admin dbAdmin = adminService.getById(id);
//            return Result.success(dbAdmin);
//        } catch (Exception e) {
//            if (e instanceof DuplicateKeyException){
//                return Result.error("500","获取管理员信息失败");
//            } else {
//                return Result.error();
//            }
//        }
//    }
    //查询全部管理员
//    @GetMapping("/selectAll")
//    public Result selectAll() {
//        List<Admin> adminList = adminService.selectAll();
//        return Result.success(adminList);
//    }
//    @PostMapping("/admin/lrc")
//    public Result lrc(String lrcName) {
//        List<String> word = new ArrayList<>();
//
//        String path = "E:\\code\\素材\\" + lrcName;
//        Path filePath = Paths.get(path);
//        try (Stream<String> lines = Files.lines( filePath ))
//        {
//            lines.forEach(item -> {
//                if (StrUtil.isNotEmpty(item)){
//                    word.add(item);
//                }
//            });
//        }
//        catch (IOException e)
//        {
//            e.printStackTrace();//只是测试用例，生产环境下不要这样做异常处理
//        }
//        return Result.success(word);
//    }
}
