package com.wes.adopt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.adopt.entity.Type;
import com.wes.adopt.mapper.TypeMapper;
import com.wes.adopt.service.TypeService;
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
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

}
