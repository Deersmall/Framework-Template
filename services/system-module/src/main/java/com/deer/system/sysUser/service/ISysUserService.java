package com.deer.system.sysUser.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysUser;

public interface ISysUserService extends IService<SysUser> {

    IPage<SysUser> getUserList(SysUser sysUser);

    SysUser sysUserByUserName(String userName);

    int add(SysUser sysUser);

    int upd(SysUser sysUser);

    int updatePassword(SysUser sysUser);
}
