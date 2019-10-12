package com.jhlc.offlineorder.controller;

import com.jhlc.offlineorder.domain.Result;
import com.jhlc.offlineorder.domain.ResultCode;
import com.jhlc.offlineorder.domain.auth.ResponseUserToken;
import com.jhlc.offlineorder.domain.auth.SysRole;
import com.jhlc.offlineorder.domain.auth.User;
import com.jhlc.offlineorder.domain.auth.UserDetail;
import com.jhlc.offlineorder.service.AuthService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */

@RestController
@Api(description = "登陆注册及刷新token")
@RequestMapping("/api/v1")
public class AuthController {
    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    public   AuthService authService;

    @PostMapping(value = "/login")
    @ApiOperation(value = "登陆", notes = "登陆成功返回token,测试管理员账号:admin,123456;用户账号：les123,admin")
    public Result<ResponseUserToken> login(
            @Valid @RequestBody User user){
        final ResponseUserToken response = authService.login(user.getName(), user.getPassword());
        return Result.success(response);
    }

    @GetMapping(value = "/logout")
    @ApiOperation(value = "登出", notes = "退出登陆")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", required = true, dataType = "string", paramType = "header")})
    public Result logout(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return Result.failure(ResultCode.UNAUTHORIZED);
        }
        authService.logout(token);
        return Result.success();
    }

    @GetMapping(value = "/user")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "Authorization token", defaultValue = "Bearer ",required = true, dataType = "string", paramType = "header")})
    public Result getUser(HttpServletRequest request){
        String token = request.getHeader(tokenHeader);
        if (token == null) {
            return Result.failure(ResultCode.UNAUTHORIZED);
        }
        UserDetail userDetail = authService.getUserByToken(token);
        return Result.success(userDetail);
    }

    @PostMapping(value = "/sign")
    @ApiOperation(value = "用户注册")
    public Result sign(@RequestBody User user) {
        if (StringUtils.isAnyBlank(user.getName(), user.getPassword())) {
            return Result.failure(ResultCode.BAD_REQUEST);
        }
        UserDetail userDetail = new UserDetail(user.getName(), user.getPassword(), SysRole.builder().id(1).build());
        return Result.success(authService.register(userDetail));
    }
//    @GetMapping(value = "refresh")
//    @ApiOperation(value = "刷新token")
//    public Result refreshAndGetAuthenticationToken(
//            HttpServletRequest request){
//        String token = request.getHeader(tokenHeader);
//        ResponseUserToken response = authService.refresh(token);
//        if(response == null) {
//            return Result.failure(ResultCode.BAD_REQUEST, "token无效");
//        } else {
//            return Result.ok(response);
//        }
//    }
}
