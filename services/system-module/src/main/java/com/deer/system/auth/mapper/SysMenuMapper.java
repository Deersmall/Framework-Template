package com.deer.system.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.entities.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Set;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    Set<SysMenu> selectMenusByRoleIds(List<String> roleIds);

}
