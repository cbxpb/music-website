package org.xpb.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.xpb.mapper.AdminMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Component
public class JwtUtil {

    private static long time = 1000*60*60*2;    //过期时间两小时
    private static String singnature = "xupeibin"; //密钥

    private static AdminMapper staticAdminMapper;

//    @Resource
//    AdminMapper adminMapper;

    /**
     * 生成token
     * @param ID
     * @return
     */

    public static String createToken(String ID,String role ){
        return JWT.create().withAudience(ID,role)
                .withExpiresAt(DateUtil.offsetHour(new Date(),2))
                .sign(Algorithm.HMAC256(singnature));
    }


    /**
     * 解析token
     * @param
     * @return
     */
    public static String getCurrentID() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String token = request.getHeader("token");
            if (StrUtil.isNotBlank(token)) {
                String Id = JWT.decode(token).getAudience().get(0);
                return Id;
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }
}
