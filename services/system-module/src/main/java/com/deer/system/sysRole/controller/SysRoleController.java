package com.deer.system.sysRole.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.deer.entities.system.RoleMenu;
import com.deer.entities.system.SysRole;
import com.deer.entities.system.UserRole;
import com.deer.framework.result.CommonResult;
import com.deer.system.roleMenu.service.IRoleMenuService;
import com.deer.system.sysRole.mapper.SysRoleMapper;
import com.deer.system.sysRole.service.ISysRoleService;
import com.deer.system.userRole.service.IUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private IRoleMenuService iRoleMenuService;

    @Autowired
    private IUserRoleService iUserRoleService;

    @PostMapping("/getPage")
    @PreAuthorize("hasAuthority('role:list')")
    public CommonResult getPage(@RequestBody SysRole sysRole) {
        return CommonResult.toObjResult(iSysRoleService.getPage(sysRole));
    }

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('role:add')")
    public CommonResult add(@RequestBody SysRole sysRole) {
        return CommonResult.toObjResult(iSysRoleService.add(sysRole));
    }

    @PostMapping("/upd")
    @PreAuthorize("hasAuthority('role:edit')")
    public CommonResult upd(@RequestBody SysRole sysRole) {
        return CommonResult.toObjResult(iSysRoleService.upd(sysRole));
    }

    @DeleteMapping("/del")
    @PreAuthorize("hasAuthority('role:del')")
    public CommonResult del(@RequestBody List<String> ids) {
        return CommonResult.toObjResult(sysRoleMapper.deleteBatchIds(ids));
    }

    @PostMapping("/getRoleMenus")
    @PreAuthorize("hasAuthority('role:assignPermissions')")
    public CommonResult getRoleMenus(@RequestBody SysRole sysRole) {

        return CommonResult.ok(
                iRoleMenuService.list(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, sysRole.getRoleId()))
                        .stream().map(x -> x.getMenuId()).collect(Collectors.toList())
        );
    }

    @PostMapping("/assignPermissions")
    @Transactional
    @PreAuthorize("hasAuthority('role:assignPermissions')")
    public CommonResult assignPermissions(@RequestBody RoleMenu roleMenu) {
//        数据验证
        if (StringUtils.isEmpty(roleMenu.getRoleId())){
            return CommonResult.no(null);
        }

//        清除角色绑定菜单/权限
        iRoleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId,roleMenu.getRoleId()));

//        是否勾选选菜单 / 权限
        if (CollectionUtils.isEmpty(roleMenu.getMenuIds())){
            return CommonResult.ok(null);
        }else {
//        角色菜单权限
            List<RoleMenu> roleMenuList = roleMenu.getMenuIds()
                    .stream()
                    .map(menuId -> new RoleMenu(roleMenu.getRoleId(), menuId))
                    .collect(Collectors.toList());
//        重新添加角色绑定菜单/权限
            return CommonResult.toObjResult(iRoleMenuService.saveBatch(roleMenuList));
        }
    }

    @PostMapping("/getRoleBoundUser")
    @PreAuthorize("hasAuthority('role:assignAccounts')")
    public CommonResult getRoleBoundUser(@RequestBody SysRole sysRole) {
        return CommonResult.ok(
                iUserRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, sysRole.getRoleId()))
                        .stream().map(x -> x.getUserId()).collect(Collectors.toList())
        );
    }

    @PostMapping("/bindUser")
    @PreAuthorize("hasAuthority('role:assignAccounts')")
    public CommonResult bindUser(@RequestBody SysRole sysRole) {
        return CommonResult.ok(
                iUserRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, sysRole.getRoleId()))
                        .stream().map(x -> x.getUserId()).collect(Collectors.toList())
        );
    }

    @PostMapping("/unBindUser")
    @PreAuthorize("hasAuthority('role:assignAccounts')")
    public CommonResult unBindUser(@RequestBody SysRole sysRole) {
        return CommonResult.ok(
                iUserRoleService.list(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, sysRole.getRoleId()))
                        .stream().map(x -> x.getUserId()).collect(Collectors.toList())
        );
    }

}