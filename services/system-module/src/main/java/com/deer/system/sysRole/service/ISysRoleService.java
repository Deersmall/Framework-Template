package com.deer.system.sysRole.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysRole;

import java.util.HashSet;

public interface ISysRoleService extends IService<SysRole> {

    IPage<SysRole> getPage(SysRole sysRole);

    HashSet<SysRole> getRolesByUserId(String userId);

    int add(SysRole sysRole);

    int upd(SysRole sysRole);
}
