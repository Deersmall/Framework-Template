package com.deer.system.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.deer.entities.system.SysMenu;
import com.deer.entities.system.SysUser;
import com.deer.system.sysMenu.mapper.SysMenuMapper;
import com.deer.system.sysUser.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 测试控制器
 * 
 * <p>提供一个简单的 GET 接口用于验证框架是否正常运行</p>
 */
@RestController
@RequestMapping("/sysTest")
public class TestController {

    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private SysMenuMapper sysMenuMapper;

    /**
     * 测试接口
     * 
     * @return 简单的响应消息
     */
    @PostMapping("/sysTestGetUser")
    @PreAuthorize("hasAuthority('test')")
    public SysUser sysTestGetUser(@RequestParam("userId") String userId) {
        LambdaQueryWrapper<SysMenu> sysMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<SysMenu> sysMenus = sysMenuMapper.selectList(sysMenuLambdaQueryWrapper);
        SysMenu sysMenu = sysMenus.stream().filter(x -> "qwertyuioasdfghjkl".equals(x.getMenuId())).findFirst().orElse(null);
        sysMenu.setPath("/test-updete");
        int i = sysMenuMapper.updateById(sysMenu);
        return sysUserMapper.selectById(userId);
    }
}