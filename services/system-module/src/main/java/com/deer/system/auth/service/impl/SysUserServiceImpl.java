package com.deer.system.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysMenu;
import com.deer.entities.system.SysRole;
import com.deer.entities.system.SysUser;
import com.deer.system.auth.mapper.SysUserMapper;
import com.deer.system.auth.service.ISysMenuService;
import com.deer.system.auth.service.ISysRoleService;
import com.deer.system.auth.service.ISysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private ISysRoleService iSysRoleService;
    @Autowired
    private ISysMenuService iSysMenuService;

    @Override
    public SysUser sysUserByUserName(String userName) {
//        设置查询条件
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getUserName,userName);

//        查询用户
        SysUser sysUser = sysUserMapper.selectOne(lambdaQueryWrapper);
//        判断用户是否存在
        if(ObjectUtils.isEmpty(sysUser)) throw new RuntimeException("未查询到系统用户：" + userName);

//        查询用户绑定角色
        Set<SysRole> roles = iSysRoleService.getRolesByUserId(sysUser.getUserId());
//        判断用户是否拥有角色
        if (CollectionUtils.isEmpty(roles)) throw new RuntimeException(userName + "账号还未分配角色");

//        查询用户可用权限及菜单
        Map<Integer, List<SysMenu>> menuMap = iSysMenuService.getMenusByRoleIds(
//                角色Ids
                roles.stream()
                        .filter(x -> StringUtils.isNotEmpty(x.getRoleId()))
                        .map(SysRole::getRoleId)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(SysMenu::getMenuType));

//        组装目录菜单结构
        List<SysMenu> directory = menuMap.get(0);
        List<SysMenu> menu = menuMap.get(1);

        directory.forEach(x -> {
//            List<SysMenu> children = new ArrayList<>();
            LinkedHashSet children = new LinkedHashSet();
            menu.forEach(y -> {
                if (x.getMenuId().equals(y.getParentId())){
                    children.add(y);
                }
            });
            x.setChildren(children);
        });

//        用户角色
        sysUser.setRoles(roles);
//        用户权限
        sysUser.setPermissionList(menuMap.get(2).stream().map(SysMenu::getPermission).collect(Collectors.toList()));
//        用户菜单
        sysUser.setMenus(directory);

        return sysUser;
    }
}




