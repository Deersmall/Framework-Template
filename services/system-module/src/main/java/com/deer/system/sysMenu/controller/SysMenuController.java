package com.deer.system.sysMenu.controller;

import com.deer.entities.system.SysMenu;
import com.deer.framework.result.CommonResult;
import com.deer.system.sysMenu.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/sysMenu")
public class SysMenuController {

    @Autowired
    private ISysMenuService iSysMenuService;

    @PostMapping("/getMenuTree")
    @PreAuthorize("hasAuthority('menu:list')")
    public CommonResult getMenuTree() {
        return CommonResult.toObjResult(iSysMenuService.getMenuTree());
    }

    @PostMapping("/addMenu")
    @PreAuthorize("hasAuthority('menu:add')")
    public CommonResult addMenu(@RequestBody SysMenu sysMenu) {
        sysMenu.setMenuId(UUID.randomUUID().toString());
        return CommonResult.toBooleanResult(iSysMenuService.save(sysMenu) , sysMenu);
    }

    @PostMapping("/updateById")
    @PreAuthorize("hasAuthority('menu:edit')")
    public CommonResult updateMenu(@RequestBody SysMenu sysMenu) {
        return CommonResult.toBooleanResult(iSysMenuService.updateById(sysMenu));
    }

    @DeleteMapping("/{ids}")
    @PreAuthorize("hasAuthority('menu:del')")
    public CommonResult removeMenu(@PathVariable List<String> ids) {
        return CommonResult.toIntResult(iSysMenuService.delMenu(ids));
    }

}
