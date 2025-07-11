package com.deer.system.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysMenu;
import com.deer.system.auth.mapper.SysMenuMapper;
import com.deer.system.auth.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;

    @Override
    public Set<SysMenu> getMenusByRoleIds(List<String> roleIds) {
        return sysMenuMapper.selectMenusByRoleIds(roleIds);
    }
}
