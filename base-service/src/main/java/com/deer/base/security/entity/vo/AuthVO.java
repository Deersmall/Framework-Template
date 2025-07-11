package com.deer.base.security.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthVO {
    private String token;
    private String tokenType;
    private Long expiresIn;
}