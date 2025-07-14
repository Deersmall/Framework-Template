package com.deer.entities.system;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole{
    /** 角色Id  */
    private String roleId;
    /** 角色名称  */
    private String roleName;
    /** 角色状态  */
    private Integer status;

}
