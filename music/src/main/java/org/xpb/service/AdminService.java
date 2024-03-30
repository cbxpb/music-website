package org.xpb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xpb.domain.Admin;
import org.xpb.exception.ServiceException;
import org.xpb.mapper.AdminMapper;
import org.xpb.util.JwtUtil;

import javax.annotation.Resource;
import java.util.List;


/**
 * 管理员Service层
 */
@Service
public class AdminService extends ServiceImpl<AdminMapper,Admin> {

    @Resource
    AdminMapper adminMapper;

    /**
     * 根据用户名查询数据库的管理员信息
     * @param username
     * @return
     */
    public Admin selectByUsername(String username) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return  getOne(queryWrapper);
    }

    /**
     * 管理员实现登录业务
     * @param admin
     * @return
     */
    public Admin login(Admin admin) {
        //1.根据用户名查询数据库的管理员信息，管理员信息为空则抛出一个自定义的异常
        Admin dbAdmin = selectByUsername(admin.getUsername());
        if (dbAdmin == null) {
            throw new ServiceException("用户名或密码错误");
        }
        //2.比较密码（MD5、Bcrypt），密码错误则抛出一个自定义的异常
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(admin.getPassword(),dbAdmin.getPassword());
        if (!matches) {
            throw new ServiceException("用户名或密码错误");
        }
        //3.生成token并返回给前端进行身份验证
        String token = JwtUtil.createToken(dbAdmin.getId().toString(),dbAdmin.getRole());
        dbAdmin.setToken(token);
        //4.将密码置空，更安全
        dbAdmin.setPassword(null);
        return dbAdmin;
    }

//    public Admin register(Admin admin) {
//        //根据管理员输入的账号查询数据库的管理员信息
//        Admin dbAdmin = selectByUsername(admin.getUsername());
//        if (dbAdmin != null) {
//            //抛出一个自定义的异常
//            throw new ServiceException("用户名已存在");
//        }
//        save(admin);
//        return admin;
//    }

    /**
     * 管理员实现密码重置业务
     * @param admin
     */
    public void resetPassword(Admin admin) {
        //1.根据用户名查询数据库的管理员信息，管理员信息为空则抛出一个自定义的异常
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
