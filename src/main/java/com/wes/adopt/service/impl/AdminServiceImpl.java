package com.wes.adopt.service.impl;

import com.wes.adopt.entity.Admin;
import com.wes.adopt.mapper.AdminMapper;
import com.wes.adopt.service.AdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wes
 * @since 2020-12-18
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

}
