package com.deer.entities.system;

import com.baomidou.mybatisplus.annotation.TableId;
import com.deer.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole extends BaseEntity {
    /** 角色Id  */
    @TableId
    private String roleId;
    /** 角色名称  */
    private String roleName;

}
