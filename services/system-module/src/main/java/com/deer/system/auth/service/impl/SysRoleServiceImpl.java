package com.deer.system.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deer.entities.system.SysRole;
import com.deer.system.auth.mapper.SysRoleMapper;
import com.deer.system.auth.service.ISysRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;


@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements ISysRoleService {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Override
    public HashSet<SysRole> getRolesByUserId(String userId) {
        return sysRoleMapper.selectRolesByUserId(userId);
    }
}




