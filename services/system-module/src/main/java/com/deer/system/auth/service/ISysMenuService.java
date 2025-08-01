package com.deer.system.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.deer.entities.system.SysMenu;

import java.util.List;

public interface ISysMenuService extends IService<SysMenu> {

    List<SysMenu> getMenusByRoleIds(List<String> roleIds);

}
