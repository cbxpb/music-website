package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 管理员表
 */
@Data
@TableName("admin")
public class Admin {
    @TableId(type= IdType.AUTO)
    private Integer id;              //主键
    private String username;        //用户名
    private String password;        //密码
    private String name;            //姓名
    private String phone;           //电话
    private String email;           //邮箱
    private String address;         //地址
    private String avatar;          //头像

    /** 用户头像本地地址 **/
    private String avatarLocal;
    private String role;            //角色

    @TableField(exist = false)
    private String token;           //返回给前端的token信息

}
