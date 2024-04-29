package org.xpb.controller;

import cn.hutool.core.util.StrUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.xpb.common.DeleteFile;
import org.xpb.common.Result;
import org.xpb.domain.Admin;
import org.xpb.exception.ServiceException;
import org.xpb.service.AdminService;

import javax.annotation.Resource;

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
        return Result.success(admin);
    }

    /**
     * 修改管理员信息(未修改管理员头像)
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
     * 管理员头像更新
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
            Admin admin = adminService.getById(id);
            /** 删除存储的本地头像文件 **/
            DeleteFile file = new DeleteFile();
            file.deleteFile(admin.getAvatarLocal());
            /** 保存路径到admin表中 **/
            admin.setAvatar(downUrl);
            admin.setAvatarLocal(uploadUrl);
            adminService.updateById(admin);
        } catch (Exception e) {
            if (e instanceof DuplicateKeyException){
                return Result.error("500","管理员头像更新失败");
            } else {
                return Result.error();
            }
        }
        return Result.success();
    }

    /**
     * 修改管理员密码
     * @param id,password,newPassword
     * @return
     */
    @PutMapping("/updatePassword")
    public Result updatePassword(@RequestParam Integer id,
                                 @RequestParam String password,
                                 @RequestParam String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Admin dbAdmin = adminService.getById(id);
        boolean matches = passwordEncoder.matches(password,dbAdmin.getPassword());
        if (!matches) {
            throw new ServiceException("原密码输入错误");
        }
        dbAdmin.setPassword(new BCryptPasswordEncoder().encode(newPassword));
        adminService.updateById(dbAdmin);
        return Result.success();
    }
}
