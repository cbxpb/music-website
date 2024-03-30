package org.xpb.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 前端用户表
 */
@Data
@TableName("consumer")
public class Consumer {

    /** 主键id自动递增 **/
    @TableId(type= IdType.AUTO)
    private Integer id;

    /** 用户名 **/
    private String username;

    /** 密码 **/
    private String password;

    /** 姓名 **/
    private String name;

    /** 性别 **/
    private String gender;

    /** 电话 **/
    private String phone;

    /** 邮箱 **/
    private String email;

    /** 出生日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date birth;

    /** 个性签名 **/
    private String introduction;

    /** 地址 **/
    private String address;

    /** 创建时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date createTime;


    /** 修改时间 **/
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    private Date updateTime;

    /** 用户头像 **/
    private String avatar;

    /** 用户头像本地地址 **/
    private String avatarLocal;

    /** 角色身份 **/
    private String role;

    /** 返回给前端的token信息 **/
    @TableField(exist = false)
    private String token;

}
