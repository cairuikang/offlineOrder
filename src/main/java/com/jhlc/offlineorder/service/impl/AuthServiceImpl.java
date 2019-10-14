package com.jhlc.offlineorder.service.impl;

import com.jhlc.offlineorder.conmmon.utils.JwtUtils;
import com.jhlc.offlineorder.core.exception.CustomException;
import com.jhlc.offlineorder.domain.Result;
import com.jhlc.offlineorder.domain.ResultCode;
import com.jhlc.offlineorder.domain.auth.*;
import com.jhlc.offlineorder.mapper.SysRoleMapper;
import com.jhlc.offlineorder.mapper.SysUserMapper;
import com.jhlc.offlineorder.mapper.SysUserRoleMapper;
import com.jhlc.offlineorder.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author: JoeTao
 * createAt: 2018/9/17
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    @Qualifier("CustomUserDetailsService")
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtUtils jwtTokenUtil;
    @Autowired
    public SysUserRoleMapper userRoleMapper;
    @Autowired
    public SysRoleMapper roleMapper;
    @Autowired
    public SysUserMapper userMapper;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Override
    public UserDetail register(UserDetail userDetail) {
        SysUser sysUser = userDetail.convertToSysUser();
        final String username = sysUser.getUsername();
        if (userMapper.selectOne(SysUser.builder().username(username).build()) != null) {
            throw new CustomException(Result.failure(ResultCode.BAD_REQUEST, "用户已存在"));
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        final String rawPassword = encoder.encode(sysUser.getPassword());
        sysUser.setPassword(rawPassword);
        userMapper.insert(sysUser);

        int roleId = userDetail.getSysRole().getId();
        SysUserRole userRole = SysUserRole.builder().userId(sysUser.getId()).roleId(roleId).build();
        userRoleMapper.insert(userRole);
        SysRole role = roleMapper.selectById(roleId);
        userDetail.setSysRole(role);
        return userDetail;
    }

    @Override
    public ResponseUserToken login(String username, String password) {
        //用户验证
        final Authentication authentication = authenticate(username, password);
        //存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //生成token
        final UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        final String token = jwtTokenUtil.generateAccessToken(userDetail);
        //存储token
        jwtTokenUtil.putToken(username, token);
        return new ResponseUserToken(token, userDetail);

    }

    @Override
    public void logout(String token) {
        token = token.substring(tokenHead.length());
        String userName = jwtTokenUtil.getUsernameFromToken(token);
        jwtTokenUtil.deleteToken(userName);
    }

    @Override
    public ResponseUserToken refresh(String oldToken) {
        String token = oldToken.substring(tokenHead.length());
        String username = jwtTokenUtil.getUsernameFromToken(token);
        UserDetail userDetail = (UserDetail) userDetailsService.loadUserByUsername(username);
        if (jwtTokenUtil.canTokenBeRefreshed(token, userDetail.getLastPasswordResetDate())) {
            token = jwtTokenUtil.refreshToken(token);
            return new ResponseUserToken(token, userDetail);
        }
        return null;
    }

    @Override
    public UserDetail getUserByToken(String token) {
        token = token.substring(tokenHead.length());
        return jwtTokenUtil.getUserFromToken(token);
    }

    private Authentication authenticate(String username, String password) {
        try {
            //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException | BadCredentialsException e) {
            e.printStackTrace();
            throw new CustomException(Result.failure(ResultCode.LOGIN_ERROR, e.getMessage()));
        }
    }
}
