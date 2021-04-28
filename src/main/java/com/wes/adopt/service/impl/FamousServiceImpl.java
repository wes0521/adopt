package com.wes.adopt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.adopt.entity.Famous;
import com.wes.adopt.mapper.FamousMapper;
import com.wes.adopt.service.FamousService;
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
public class FamousServiceImpl extends ServiceImpl<FamousMapper, Famous> implements FamousService {

}
