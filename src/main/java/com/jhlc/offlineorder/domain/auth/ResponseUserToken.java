package com.jhlc.offlineorder.domain.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author JoeTao
 * createAt: 2018/9/17
 */
@Data
@AllArgsConstructor
public class ResponseUserToken implements Serializable {
    private String token;
    private UserDetail userDetail;
}
