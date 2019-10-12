package com.jhlc.offlineorder.service.impl;

import com.jhlc.offlineorder.domain.auth.UserDetail;
import com.jhlc.offlineorder.mapper.SysUserMapper;
import com.jhlc.offlineorder.service.SysUserService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wuchong123
 * @since 2019-10-12
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, UserDetail> implements SysUserService {

}
