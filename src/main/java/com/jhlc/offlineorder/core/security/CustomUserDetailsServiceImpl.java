package com.jhlc.offlineorder.core.security;

import com.jhlc.offlineorder.domain.auth.SysRole;
import com.jhlc.offlineorder.domain.auth.SysUserRole;
import com.jhlc.offlineorder.domain.auth.UserDetail;
import com.jhlc.offlineorder.mapper.SysRoleMapper;
import com.jhlc.offlineorder.mapper.SysUserMapper;
import com.jhlc.offlineorder.mapper.SysUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * 登陆身份认证
 *
 * @author: JoeTao
 * createAt: 2018/9/14
 */
@Component(value = "CustomUserDetailsService")
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserRoleMapper userRoleMapper;
    @Autowired
    private SysRoleMapper roleMapper;


    @Override
    public UserDetail loadUserByUsername(String name) throws UsernameNotFoundException {
        UserDetail userDetail = userMapper.selectOne(UserDetail.builder().username(name).build());
        if (userDetail == null) {
            throw new UsernameNotFoundException(String.format("No userDetail found with username '%s'.", name));
        }
        SysUserRole userRole = userRoleMapper.selectOne(SysUserRole.builder().userId(userDetail.getId()).build());
        SysRole role = roleMapper.selectById(userRole.getRoleId());
        userDetail.setSysRole(role);
        return userDetail;
    }
}
