package com.wes.adopt.service.impl;

import com.wes.adopt.entity.Adopt;
import com.wes.adopt.mapper.AdoptMapper;
import com.wes.adopt.service.AdoptService;
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
public class AdoptServiceImpl extends ServiceImpl<AdoptMapper, Adopt> implements AdoptService {

}
