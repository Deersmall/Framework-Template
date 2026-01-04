package com.deer.entities.system;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName user_role
 */
@TableName(value ="user_role")
@Data
public class UserRole implements Serializable {
    private String userId;

    private String roleId;

    private static final long serialVersionUID = 1L;
}