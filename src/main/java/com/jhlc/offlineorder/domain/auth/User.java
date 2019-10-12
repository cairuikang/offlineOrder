package com.jhlc.offlineorder.domain.auth;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;

/**
 * <p>
 * 
 * </p>
 *
 * @author wuchong123
 * @since 2019-10-12
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {
    @ApiModelProperty(value = "用户名", required = true)
    @Size(min=5, max=20)
    private String name;
    @ApiModelProperty(value = "密码", required = true)
    @Size(min=6, max=20)
    private String password;


}
