package com.wes.adopt.service.impl;

import com.wes.adopt.entity.User;
import com.wes.adopt.mapper.UsersMapper;
import com.wes.adopt.service.UsersService;
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
public class UsersServiceImpl extends ServiceImpl<UsersMapper, User> implements UsersService {

}
