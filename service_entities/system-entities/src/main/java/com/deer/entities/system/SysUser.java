package com.deer.entities.system;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    /** 用户Id  */
    private String userId;
    /** 用户账号  */
    private String userName;
    /** 用户名称  */
    private String nickName;
    /** 用户密码  */
    private String password;
    /** 用户加密盐  */
    private String salt;
    /** 用户状态  */
    private Integer status;

    /** 用户绑定角色    */
    @TableField(exist = false)
    private Set<SysRole> roles;

    /** 用户可用权限    */
    @TableField(exist = false)
    private Set<SysMenu> menus;

}
