package org.xpb.controller;


import cn.hutool.core.util.StrUtil;
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
*  功能：管理员所需的功能接口
*/

@RestController
public class AdminController {
    @Resource
    AdminService adminService;

    /**
     * 管理员登录接口
     * @param admin
     * @return
     */
    @PostMapping("/admin/login")
    public Result login(@RequestBody Admin admin) {
        if (StrUtil.isBlank(admin.getUsername()) || StrUtil.isBlank(admin.getPassword())){
            return Result.error("数据输入不合法！");
        }
        admin = adminService.login(admin);
        System.out.println(admin);
        return Result.success(admin);
    }

    @PostMapping("/admin/register")
    public Result register(@RequestBody Admin admin) {
        if (StrUtil.isBlank(admin.getUsername()) || StrUtil.isBlank(admin.getPassword())){
            return Result.error("数据输入不合法！");
        }
        if (admin.getUsername().length() > 10 || admin.getPassword().length() > 20){
            return Result.error("数据输入不合法！");
        }
        admin = adminService.register(admin);
        return Result.success(admin);
    }


    /**
     * 修改管理员信息
     * @param admin
     * @return
     */
    @PutMapping("/admin/update")
    public Result update(@RequestBody Admin admin) {
        if (StrUtil.isBlank(admin.getUsername())) {
            throw new ServiceException("500","数据输入不合法");
        }
        try {
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
    @PutMapping("/admin/updateAvatar")
    public Result updateFile(@RequestParam String downUrl,
                             @RequestParam String uploadUrl,
                             @RequestParam Integer id) {
        try {
            System.out.println(id);
            System.out.println(downUrl);
            System.out.println(uploadUrl);
            /** 根据主键id查询用户数据 **/
            Admin admin = adminService.getById(id);
            System.out.println(admin);

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
