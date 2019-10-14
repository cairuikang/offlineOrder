package com.jhlc.offlineorder.domain.auth;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.google.common.base.Converter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
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
@NoArgsConstructor
public class SysUser implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String password;
    private Timestamp lastPasswordResetDate;
    public SysUser(String userName, String password,Timestamp last){
        this.username = userName;
        this.password = password;
        this.lastPasswordResetDate = last;
    }
    private static class UserDTOConvert extends Converter<SysUser,UserDetail > {
        @Override
        protected SysUser doBackward(UserDetail userDTO) {
            SysUser user = new SysUser();
            BeanUtils.copyProperties(userDTO,user);
            return user;
        }

        @Override
        protected UserDetail doForward(SysUser user) {
            UserDetail userDetail = new UserDetail();
            BeanUtils.copyProperties(user,userDetail);
            return userDetail;
        }
    }
    public UserDetail convertToUserDetail(){
        UserDTOConvert userDTOConvert = new UserDTOConvert();
        UserDetail convert = userDTOConvert.convert(this);
        return convert;
    }
}
