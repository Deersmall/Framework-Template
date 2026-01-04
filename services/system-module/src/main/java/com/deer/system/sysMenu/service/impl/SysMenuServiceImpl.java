package com.deer.system.sysMenu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.RoleMenu;
import com.deer.entities.system.SysMenu;
import com.deer.framework.exception.AuthExceptions;
import com.deer.system.roleMenu.mapper.RoleMenuMapper;
import com.deer.system.sysMenu.mapper.SysMenuMapper;
import com.deer.system.sysMenu.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    @Autowired
    private SysMenuMapper sysMenuMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;

    /**
     * 获取角色可用菜单
     * @param roleIds
     * @return
     */
    @Override
    public List<SysMenu> getMenusByRoleIds(List<String> roleIds) {
        return sysMenuMapper.selectMenusByRoleIds(roleIds);
    }

    /**
     * 获取角色菜单树
     * @return
     */
    @Override
    public List<SysMenu> getMenuTree() {

//        查询菜单
        Map<Integer, List<SysMenu>> menusByType = sysMenuMapper.selectList(
                new LambdaQueryWrapper<SysMenu>()
                        .orderByAsc(SysMenu::getMenuType,SysMenu::getOrderNum)
        ).stream().collect(Collectors.groupingBy(SysMenu::getMenuType));

        return assemblyMenuTree(menusByType);
    }

    @Override
    public int delMenu(List<String> ids) {

        List<RoleMenu> roleMenus = roleMenuMapper.selectList(
                new LambdaQueryWrapper<RoleMenu>().in(RoleMenu::getMenuId, ids)
        );

        if (!CollectionUtils.isEmpty(roleMenus)){
//            throw new ServiceException(500,"改菜单或权限绑定了角色，请先解除绑定");
            throw new AuthExceptions("改菜单或权限绑定了角色，请先解除绑定!!!");
        }

        return sysMenuMapper.deleteBatchIds(ids);
    }


    /**
     * 组装菜单树
     * @param menusByType
     * @return
     */
    private List<SysMenu> assemblyMenuTree(Map<Integer, List<SysMenu>> menusByType) {

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
