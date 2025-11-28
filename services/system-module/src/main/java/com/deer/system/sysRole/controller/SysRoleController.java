package com.deer.system.sysRole.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deer.entities.system.SysRole;
import com.deer.framework.result.CommonResult;
import com.deer.system.sysRole.mapper.SysRoleMapper;
import com.deer.system.sysRole.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysRole")
public class SysRoleController {

    @Autowired
    private ISysRoleService iSysRoleService;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @PostMapping("/getList")
//    @PreAuthorize("hasAuthority('system::menu')")
    public CommonResult getList(@RequestBody SysRole sysRole) {
        Page<SysRole> sysRolePage = sysRoleMapper.selectPage(new Page<SysRole>(sysRole.getPageNum(), sysRole.getPageSize()), null);
        return CommonResult.toObjResult(sysRolePage);
    }

}
