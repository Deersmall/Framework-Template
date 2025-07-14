package com.deer.entities.system.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import com.deer.entities.system.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {

    private SysUser sysUser;
    private String token;

    public LoginUser(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    @JSONField(serialize = false)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return sysUser.getMenus()
                .stream()
                .map(menu -> new SimpleGrantedAuthority(menu.getPermission()))
                .collect(Collectors.toList());

    }

    @Override
    @JSONField(serialize = false)
    public String getUsername() {
        return sysUser.getUserName();
    }

    @Override
    @JSONField(serialize = false)
    public String getPassword() {
        return sysUser.getPassword();
    }

    /**
     * 账户是否未过期,过期无法验证
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否解锁,锁定的用户无法进行身份验证
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 是否是已过期的用户凭据(密码),过期的凭据防止认证
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用 ,禁用的用户不能身份验证
     * @return
     */
    @Override
    @JSONField(serialize = false)
    public boolean isEnabled() {
        return true;
    }
}