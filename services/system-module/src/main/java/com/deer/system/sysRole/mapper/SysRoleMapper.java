package com.deer.system.sysRole.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.entities.system.SysRole;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashSet;

@Mapper
public interface SysRoleMapper extends BaseMapper<SysRole> {

    HashSet<SysRole> selectRolesByUserId(String userId);
}




