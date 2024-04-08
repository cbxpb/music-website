package org.xpb.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.xpb.domain.Consumer;
import org.xpb.exception.ServiceException;
import org.xpb.mapper.ConsumerMapper;
import org.xpb.util.JwtUtil;

import java.util.Date;

/**
 * 前端用户Service层
 */
@Service
public class ConsumerService extends ServiceImpl<ConsumerMapper, Consumer> {

    /**
     * 根据用户名查询数据库的用户信息
     * @param username
     * @return
     */
    public Consumer selectByUsername(String username) {
        QueryWrapper<Consumer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username",username);
        return  getOne(queryWrapper);
    }

    /**
     * 用户实现登录业务
     * @param consumer
     * @return
     */
    public Consumer login(Consumer consumer) {
        //1.根据用户名查询数据库的用户信息，用户信息为空则抛出一个自定义的异常
        Consumer dbConsumer = selectByUsername(consumer.getUsername());
        if (dbConsumer == null) {
            throw new ServiceException("用户名或密码错误");
        }
        //2.比较密码（MD5、Bcrypt），密码错误则抛出一个自定义的异常
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        boolean matches = passwordEncoder.matches(consumer.getPassword(),dbConsumer.getPassword());
        if (!matches) {
            throw new ServiceException("用户名或密码错误");
        }
        //3.生成token并返回给前端进行身份验证
        String token = JwtUtil.createToken(dbConsumer.getId().toString(),dbConsumer.getRole());
        dbConsumer.setToken(token);
        //4.将密码置空，更安全
        dbConsumer.setPassword(null);
        return dbConsumer;
    }
    /**
     * 用户实现注册业务
     * @param consumer
     * @return
     */
    public Consumer register(Consumer consumer) {
        //根据管理员输入的账号查询数据库的管理员信息
        Consumer dbConsumer = selectByUsername(consumer.getUsername());
        if (dbConsumer != null) {
            //抛出一个自定义的异常
            throw new ServiceException("用户名已存在");
        }
        consumer.setRole("用户");
        consumer.setPassword(new BCryptPasswordEncoder().encode(consumer.getPassword()));
        consumer.setCreateTime(new Date());
        consumer.setUpdateTime(new Date());
        save(consumer);
        return consumer;
    }

//    /**
//     * 管理员实现密码重置业务
//     * @param admin
//     */
//    public void resetPassword(Admin admin) {
//        //1.根据用户名查询数据库的管理员信息，管理员信息为空则抛出一个自定义的异常
//        Admin dbAdmin = selectByUsername(admin.getUsername());
//        if (dbAdmin == null) {
//            //抛出一个自定义的异常
//            throw new ServiceException("用户不存在");
//        }
//        if (!admin.getPhone().equals(dbAdmin.getPhone())) {
//            throw new ServiceException("验证错误");
//        }
//        dbAdmin.setPassword("123");     //重置密码
//        updateById(dbAdmin);
//    }

}
