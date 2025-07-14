package com.deer.system.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysUser;

public interface ISysUserService extends IService<SysUser> {

    SysUser sysUserByUserName(String userName);

}
