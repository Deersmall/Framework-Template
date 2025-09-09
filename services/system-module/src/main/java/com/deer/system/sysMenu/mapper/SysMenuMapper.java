package com.deer.system.sysMenu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.deer.entities.system.SysMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysMenuMapper extends BaseMapper<SysMenu> {

    List<SysMenu> selectMenusByRoleIds(List<String> roleIds);

}
