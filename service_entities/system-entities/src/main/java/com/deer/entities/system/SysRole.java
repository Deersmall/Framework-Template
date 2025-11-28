package com.deer.entities.system;

import com.deer.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole extends BaseEntity {
    /** 角色Id  */
    private String roleId;
    /** 角色名称  */
    private String roleName;

}
