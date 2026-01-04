package com.deer.system.sysUser.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.deer.entities.system.SysUser;
import com.deer.framework.result.CommonResult;
import com.deer.system.sysUser.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;

    @PostMapping("/getAllUser")
//    @PreAuthorize("hasAuthority('system::menu')")
    public CommonResult getAllUser(@RequestBody SysUser sysUser) {
        IPage<SysUser> page = new Page<>(sysUser.getPageNum(), sysUser.getPageSize());
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysUser::getStatus,0);
        if (StringUtils.isNotEmpty(sysUser.getUserName())){
            lambdaQueryWrapper.eq(SysUser::getUserName,sysUser.getUserName());
        }else if (StringUtils.isNotEmpty(sysUser.getNickName())){
            lambdaQueryWrapper.eq(SysUser::getNickName, sysUser.getNickName());
        }

        return CommonResult.toObjResult(iSysUserService.page(page,lambdaQueryWrapper));
    }

}
