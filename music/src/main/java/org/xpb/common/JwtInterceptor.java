package org.xpb.common;

import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.xpb.domain.Admin;
import org.xpb.domain.Consumer;
import org.xpb.exception.ServiceException;
import org.xpb.mapper.AdminMapper;
import org.xpb.mapper.ConsumerMapper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    AdminMapper adminMapper;
    @Resource
    ConsumerMapper consumerMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");      // header里面传过来的参数
        if (StrUtil.isBlank(token)) {                      // url参数 ?token=xxxxxx
            token = request.getParameter("token");
        }

        // 执行认证
        if (StrUtil.isBlank(token)) {
            throw new ServiceException("401","请登录");
        }
        // 获取 token 中的 id
        String Id;
        String Role;
        Admin admin = null;
        Consumer consumer = null;
        try {
            Id = JWT.decode(token).getAudience().get(0);
            Role = JWT.decode(token).getAudience().get(1);
            System.out.println("ID:"+Id+"Role:"+Role);
        } catch (JWTDecodeException j) {
            throw new ServiceException("401","请登录");
        }
        //根据token中的Id查询数据库
        if (Role.equals("管理员")) {
             admin = adminMapper.selectById(Integer.valueOf(Id));
        }else {
            consumer = consumerMapper.selectById(Integer.valueOf(Id));
        }
        
        if (admin == null && consumer == null) {
            throw new ServiceException("401","请登录");
        }

        // 通过密钥加密之后生成一个验证器
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("xupeibin")).build();
        try {
            jwtVerifier.verify(token);  //验证token
        } catch (JWTVerificationException e) {
            throw new ServiceException("401","请登录");
        }
        return true;
    }
}
