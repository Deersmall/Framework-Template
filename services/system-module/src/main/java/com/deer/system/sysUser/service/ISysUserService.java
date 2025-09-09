package com.deer.system.sysUser.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysUser;

public interface ISysUserService extends IService<SysUser> {

    SysUser sysUserByUserName(String userName);

}
