package com.deer.system.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysMenu;

import java.util.List;
import java.util.Set;

public interface ISysMenuService extends IService<SysMenu> {

    Set<SysMenu> getMenusByRoleIds(List<String> roleIds);

}
