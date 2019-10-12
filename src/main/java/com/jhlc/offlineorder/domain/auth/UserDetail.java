package com.jhlc.offlineorder.domain.auth;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author : JoeTao
 * createAt: 2018/9/14
 */
@Data
@AllArgsConstructor
@Builder
@TableName("sys_user")
public class UserDetail implements UserDetails,Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    @TableField(exist = false)
    private SysRole SysRole;
    private Timestamp lastPasswordResetDate;

    public UserDetail(
            int id,
            String username,
            SysRole SysRole,
            String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.SysRole = SysRole;
    }

    public UserDetail(String username, String password, SysRole SysRole) {
        this.username = username;
        this.password = password;
        this.SysRole = SysRole;
    }
    public UserDetail(Integer id ,String username, String password,Timestamp data) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastPasswordResetDate = data;
    }

    public UserDetail(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    public UserDetail(User user,SysRole sysRole,SysUserRole sysUserRole) {
        this.id = sysUserRole.getId();
        this.username = user.getName();
        this.password = user.getPassword();
        this.SysRole = sysRole;
    }

    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(SysRole.getName()));
        return authorities;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
