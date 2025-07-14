package com.deer.system.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysRole;

import java.util.HashSet;

public interface ISysRoleService extends IService<SysRole> {

    HashSet<SysRole> getRolesByUserId(String userId);

}
