package com.deer.system.sysUser.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.entities.system.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    int batchInsert(@Param("list") List<SysUser> sysUserList);

}





