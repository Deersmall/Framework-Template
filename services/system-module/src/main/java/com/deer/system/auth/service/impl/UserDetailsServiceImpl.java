package com.deer.system.auth.service.impl;

import com.deer.entities.system.SysRole;
import com.deer.entities.system.SysUser;
import com.deer.entities.system.vo.LoginUser;
import com.deer.framework.exception.AuthExceptions;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private com.deer.system.sysUser.service.ISysUserService ISysUserService;

    @SneakyThrows
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        查询用户
        SysUser sysUser = ISysUserService.sysUserByUserName(username);
        Set<SysRole> roles = sysUser.getRoles();
        sysUser.setRoles(roles);

        if (sysUser == null){
            throw new AuthExceptions(AuthExceptions.USER_NOT_FOUND,"用户不存在:" + username);
        }else if (sysUser.getStatus().equals("1")){
            throw new AuthExceptions(AuthExceptions.ABNORMAL_ACCOUNT_STATUS,"该用户账号以停用");
        }

        return new LoginUser(sysUser);
    }

}
