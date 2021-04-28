package com.wes.adopt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wes.adopt.entity.Activity;
import com.wes.adopt.mapper.ActivityMapper;
import com.wes.adopt.service.ActivityService;
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
public class ActivityServiceImpl extends ServiceImpl<ActivityMapper, Activity> implements ActivityService {

}
