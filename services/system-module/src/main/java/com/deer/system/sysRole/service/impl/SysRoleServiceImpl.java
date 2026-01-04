package com.deer.system.sysRole.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysRole;
import com.deer.framework.utils.SecurityUtils;
import com.deer.system.sysRole.mapper.SysRoleMapper;
import com.deer.system.sysRole.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.UUID;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public HashSet<SysRole> getRolesByUserId(String userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }

    @Override
    public IPage<SysRole> getPage(SysRole sysRole) {
        IPage<SysRole> page = new Page<>(sysRole.getPageNum(), sysRole.getPageSize());

        LambdaQueryWrapper<SysRole> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotEmpty(sysRole.getRoleName())){
            lambdaQueryWrapper.like(SysRole::getRoleName,sysRole.getRoleName());
        }
        if(sysRole.getStatus() != null){
            lambdaQueryWrapper.eq(SysRole::getStatus,sysRole.getStatus());
        }
        lambdaQueryWrapper.last("ORDER BY COALESCE(update_time, create_time) DESC");

        return sysRoleMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public int add(SysRole sysRole) {

        sysRole.setRoleId(UUID.randomUUID().toString());
        sysRole.setCreateById(SecurityUtils.getUserId());
        sysRole.setCreateTime(System.currentTimeMillis());

        return sysRoleMapper.insert(sysRole);
    }

    @Override
    public int upd(SysRole sysRole) {
        sysRole.setUpdateById(SecurityUtils.getUserId());
        sysRole.setUpdateTime(System.currentTimeMillis());
        return sysRoleMapper.updateById(sysRole);
    }

}




