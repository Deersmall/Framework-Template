package com.deer.system.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.base.security.entity.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    HashSet<SysRole> selectRolesByUserId(String userId);
}




