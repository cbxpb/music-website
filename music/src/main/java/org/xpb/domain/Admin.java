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
    /** 主键id 自动递增 **/
    @TableId(type= IdType.AUTO)
    private Integer id;
    /** 管理员用户名 **/
    private String username;
    /** 管理员密码 **/
    private String password;
    /** 管理员姓名 **/
    private String name;
    /** 管理员电话 **/
    private String phone;
    /** 管理员邮箱 **/
    private String email;
    /** 管理员地址 **/
    private String address;
    /** 管理员头像下载地址 **/
    private String avatar;

    /** 管理员头像本地地址 **/
    private String avatarLocal;
    /** 角色身份 **/
    private String role;

    /** 返回给前端的token信息 **/
    @TableField(exist = false)
    private String token;
}
