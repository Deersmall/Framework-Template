package com.deer.system.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.entities.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

//    SysUser sysUserByUserName(@Param(Constants.WRAPPER) LambdaQueryWrapper<SysUser> lambdaQueryWrapper);

}





