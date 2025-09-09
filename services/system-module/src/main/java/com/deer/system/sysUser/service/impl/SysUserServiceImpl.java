package com.deer.system.sysUser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysMenu;
import com.deer.entities.system.SysRole;
import com.deer.entities.system.SysUser;
import com.deer.system.sysUser.mapper.SysUserMapper;
import com.deer.system.sysMenu.service.ISysMenuService;
import com.deer.system.sysRole.service.ISysRoleService;
import com.deer.system.sysUser.service.ISysUserService;
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
        Map<Integer, List<SysMenu>> menusByType = iSysMenuService.getMenusByRoleIds(
//                角色Ids
                roles.stream()
                        .filter(x -> StringUtils.isNotEmpty(x.getRoleId()))
                        .map(SysRole::getRoleId)
                        .collect(Collectors.toList())
        ).stream().collect(Collectors.groupingBy(SysMenu::getMenuType));

//        用户角色
        sysUser.setRoles(roles);
//        用户权限
        sysUser.setPermissionList(menusByType.get(2).stream().map(SysMenu::getPermission).collect(Collectors.toList()));
//        用户菜单
        sysUser.setMenus(getMenuTree(menusByType));

        return sysUser;
    }


    private List<SysMenu> getMenuTree(Map<Integer, List<SysMenu>> menusByType) {

//        组装目录菜单结构
        List<SysMenu> directories = menusByType.getOrDefault(0, new ArrayList<>());
        List<SysMenu> menus = menusByType.getOrDefault(1, new ArrayList<>());
        List<SysMenu> permissions = menusByType.getOrDefault(2, new ArrayList<>());

        // 创建菜单ID到菜单对象的映射，便于快速查找
        Map<String, SysMenu> menuMap = new HashMap<>();

        // 处理目录（第一层）
        for (SysMenu directory : directories) {
            directory.setChildren(new ArrayList<>());
            menuMap.put(directory.getMenuId(), directory);
        }

        // 处理菜单（第二层），挂载到对应目录下
        for (SysMenu menu : menus) {
            menu.setChildren(new ArrayList<>());
            if (menu.getParentId() != null && menuMap.containsKey(menu.getParentId())) {
                menuMap.get(menu.getParentId()).addChild(menu);
            }
            menuMap.put(menu.getMenuId(), menu);
        }

        // 处理权限（第三层），挂载到对应菜单下
        for (SysMenu permission : permissions) {
            if (permission.getParentId() != null && menuMap.containsKey(permission.getParentId())) {
                menuMap.get(permission.getParentId()).addChild(permission);
            }
        }

        return directories
                .stream()
                .sorted(Comparator.comparingInt(SysMenu::getOrderNum))
                .collect(Collectors.toList());
    }
}




