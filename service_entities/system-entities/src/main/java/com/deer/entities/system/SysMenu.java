package com.deer.entities.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenu implements Serializable {

    /** 菜单ID  */
    private String menuId;
    /** 菜单名称  */
    private String menuName;
    /** 父菜单ID */
    private String parentId;
    /** 权限    */
    private String permission;
    /** 路由地址  */
    private String path;
    /** 菜单状态  */
    private Integer status;


}
