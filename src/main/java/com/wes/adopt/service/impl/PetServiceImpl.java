package com.wes.adopt.service.impl;

import com.wes.adopt.entity.Pet;
import com.wes.adopt.mapper.PetMapper;
import com.wes.adopt.service.PetService;
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
public class PetServiceImpl extends ServiceImpl<PetMapper, Pet> implements PetService {

}
