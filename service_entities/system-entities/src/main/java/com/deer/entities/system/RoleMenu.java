package com.deer.entities.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @TableName role_menu
 */
@TableName(value ="role_menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleMenu implements Serializable {
    private String roleId;

    private String menuId;

    @TableField(exist = false)
    private List<String> menuIds;

    private static final long serialVersionUID = 1L;

    public RoleMenu(String roleId, String menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}