package com.deer.entities.system;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.LinkedHashSet;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu implements Serializable {

    /** 菜单ID  */
    private String menuId;
    /** 菜单名称  */
    private String menuName;
    /** 菜单类型（0目录 / 1菜单 / 2权限）  */
    private Integer menuType;
    /** 显示顺序  */
    private Integer orderNum;
    /** 父菜单ID */
    private String parentId;
    /** 权限    */
    private String permission;
    /** 路由地址  */
    private String path;
    /** 菜单状态  */
    private Integer status;

    @TableField(exist = false)
    private LinkedHashSet<SysMenu> children;


}
