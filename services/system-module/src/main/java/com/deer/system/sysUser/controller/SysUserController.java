package com.deer.system.sysUser.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.deer.entities.system.SysUser;
import com.deer.framework.result.CommonResult;
import com.deer.system.sysUser.mapper.SysUserMapper;
import com.deer.system.sysUser.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sysUser")
public class SysUserController {

    @Autowired
    private ISysUserService iSysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @PostMapping("/getUser")
    @PreAuthorize("hasAuthority('user:list')")
    public CommonResult getUser(@RequestBody SysUser sysUser) {
        return CommonResult.toObjResult(iSysUserService.getUserList(sysUser));
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('user:add')")
    public CommonResult add(@RequestBody SysUser sysUser) {
        return CommonResult.toObjResult(iSysUserService.add(sysUser));
    }

    @PostMapping("/upd")
    @PreAuthorize("hasAuthority('user:edit')")
    public CommonResult upd(@RequestBody SysUser sysUser) {
        return CommonResult.toObjResult(iSysUserService.upd(sysUser));
    }

    @PostMapping("/updatePassword")
    @PreAuthorize("hasAuthority('user:edit')")
    public CommonResult updatePassword(@RequestBody SysUser sysUser) {
        return CommonResult.toObjResult(iSysUserService.updatePassword(sysUser));
    }

    @DeleteMapping("/del")
    @PreAuthorize("hasAuthority('user:del')")
    public CommonResult del(@RequestBody List<String> ids) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SysUser::getStatus,-1).in(SysUser::getUserId,ids);
        return CommonResult.toObjResult(sysUserMapper.update(updateWrapper));
    }

}