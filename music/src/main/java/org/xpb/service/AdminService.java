package org.xpb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xpb.domain.Admin;
import org.xpb.exception.ServiceException;
import org.xpb.mapper.AdminMapper;
import org.xpb.util.JwtUtil;

import javax.annotation.Resource;
import java.util.List;

@Service
public class AdminService extends ServiceImpl<AdminMapper,Admin> {

    @Resource
    AdminMapper adminMapper;

//    //验证管理员账户是否合法
//    public Admin login(Admin admin) {
//        //根据管理员账号查询数据库的管理员信息
//        Admin dbAdmin = adminMapper.selectByname(admin.getName());
//        if (dbAdmin == null) {
//            //抛出一个自定义的异常
//            throw new ServiceException("用户名或密码错误");
//        }
//        if (!admin.getPassword().equals(dbAdmin.getPassword())) {
//            throw new ServiceException("用户名或密码错误");
//        }
//        //生成token
//        String token = JwtUtil.createToken(dbAdmin.getId().toString());
//        dbAdmin.setToken(token);
//        return dbAdmin;
//    }
//
//    public Admin register(Admin admin) {
//        //根据管理员输入的账号查询数据库的管理员信息
//        Admin dbAdmin = adminMapper.selectByname(admin.getName());
//        if (dbAdmin != null) {
//            //抛出一个自定义的异常
//            throw new ServiceException("用户名已存在");
//        }
//        adminMapper.insert(admin);
//        return admin;
//    }
//
//    public List<Admin> selectAll() {
//        return adminMapper.selectAll();
//    }


    public Admin selectByUsername(String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        //根据用户名查询数据库的管理员信息
        return  getOne(queryWrapper);
    }

    //验证管理员账户是否合法
    public Admin login(Admin admin) {
        Admin dbAdmin = selectByUsername(admin.getUsername());
        if (dbAdmin == null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户名或密码错误");
        }
        if (!admin.getPassword().equals(dbAdmin.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        //生成token
        String token = JwtUtil.createToken(dbAdmin.getId().toString(),dbAdmin.getRole());
        dbAdmin.setToken(token);
        return dbAdmin;
    }

    public Admin register(Admin admin) {
        //根据管理员输入的账号查询数据库的管理员信息
        Admin dbAdmin = selectByUsername(admin.getUsername());
        if (dbAdmin != null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户名已存在");
        }
        save(admin);
        return admin;
    }

    public void resetPassword(Admin admin) {
        //根据管理员输入的账号查询数据库的管理员信息
        Admin dbAdmin = selectByUsername(admin.getUsername());
        if (dbAdmin == null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户不存在");
        }
        if (!admin.getPhone().equals(dbAdmin.getPhone())) {
            throw new ServiceException("验证错误");
        }
        dbAdmin.setPassword("123");     //重置密码
        updateById(dbAdmin);
    }

}
