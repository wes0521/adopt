package com.wes.adopt.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.adopt.entity.Activity;
import com.wes.adopt.mapper.ActivityMapper;
import com.wes.adopt.utils.PageModel;
import com.wes.adopt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wes
 * @since 2020-12-18
 */
@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Resource
    private ActivityMapper activityMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /*
    * 查询所有活动
    * */
    @RequestMapping("/findAllList")
    public String findAllList(@RequestParam(defaultValue = "1") Integer pageIndex,
                           @RequestParam(defaultValue = "3") Integer pageSize,
                           String type, String title, Model model) {
        System.out.println("要查询的主题；" + title);
        PageModel pageModel = new PageModel();
        pageModel.setPageIndex(pageIndex);
        pageModel.setPageSize(pageSize);
        IPage<Activity> activityPage = new Page<>(pageIndex, pageSize);//参数一是当前页，参数二是每页个数
        if ("".equals(title) || null == title) {
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("is_deleted",0);
            int recordCount = activityMapper.selectCount(wrapper1);
            pageModel.setRecordCount(recordCount);
            activityPage = activityMapper.selectPage(activityPage, wrapper1);
        } else {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.like("title", title);
            wrapper.eq("is_deleted",0);
            int recordCount = activityMapper.selectCount(wrapper);
            pageModel.setRecordCount(recordCount);
            activityPage = activityMapper.selectPage(activityPage, wrapper);
        }
        List<Activity> activities = activityPage.getRecords();

        System.out.println("activities----->>" + activities.size());
        activities.forEach(System.out::println);//打印活动列表
        model.addAttribute("activities", activities);
        model.addAttribute("pageModel", pageModel);
        if (title != null) {
            model.addAttribute("title", title);
        } else {
            model.addAttribute("title", "");
        }
        if (type == null || "".equals(type)) {
            System.out.println("type为空说明是前台的查询活动");
            return "/html/activities";
        }
        System.out.println("type不为空说明是后台的查询活动type为：" + type);
        return "/admin/admin-activity";
    }

    /*
    * 添加活动
    * */
    @RequestMapping("/addActivity")
    @ResponseBody
    public String addActivity(Activity activity){
        System.out.println("要新增的活动信息："+activity);
        int result = activityMapper.insert(activity);
        if(result!=0){
            return "success";
        }
        return "fail";
    }

    /*
     * 修改活动之前查询个人信息==通过id
     * */
    @RequestMapping("/findActivityInfoById")
    public String findActivityInfoById(String id, Model model){
        System.out.println("要修改的活动id-->"+id);
        Activity activity;
        String activityById = redisTemplate.opsForValue().get("activity"+id);
        if(activityById == null || "".equals(activityById)){
            QueryWrapper<Activity> q = new QueryWrapper();
            q.eq("id",id);
            activity = activityMapper.selectOne(q);
            redisTemplate.opsForValue().set("activity"+id,JSON.toJSONString(activity),60, TimeUnit.SECONDS);
        }else {
            activity = JSON.parseObject(activityById,Activity.class);
        }
        System.out.println("要修改的活动信息activity->"+activity);
        model.addAttribute("activityById",activity);
        return "/admin/member-editActivity";
    }

    /*
     * 修改单个活动信息==通过id
     * */
    @RequestMapping("/modifyActivityById")
    @ResponseBody
    public String modifyActivityById(String id, Model model, Activity activity){
        System.out.println("修改之后的活动activity------>"+activity);
        System.out.println("要修改的活动id-+++->"+id);
        String activityById = redisTemplate.opsForValue().get("activity"+id);
        if(activityById != null){
            redisTemplate.delete("activity"+id);
        }
        QueryWrapper<Activity> q = new QueryWrapper();
        q.eq("id",activity.getId());
        int result = activityMapper.update(activity,q);
        System.out.println("result->"+result);
        if(result==0){
            System.out.println("修改活动信息失败");
            model.addAttribute("error","修改活动信息失败");
            return "fail";
        }else{
            System.out.println("修改活动信息成功");
            return "success";
        }
    }


    /*
    * 通过id删除活动
    * */
    @RequestMapping("/delActivityById")
    @ResponseBody
    public String delActivityById(String id){
        System.out.println("要删除的活动的id"+id);
        String activityById = redisTemplate.opsForValue().get("activity"+id);
        if(activityById != null){
            redisTemplate.delete("activity"+id);
        }
        QueryWrapper q = new QueryWrapper();
        q.eq("id",id);
        Activity activity = activityMapper.selectOne(q);
        activity.setIsDeleted(1);
        int rows = activityMapper.update(activity,q);
        if(rows>0){
            System.out.println("活动删除成功！");
        }else {
            System.out.println("活动删除失败！");
            return "fail";
        }
        return "success";
    }

    /*
     * 通过id批量删除活动
     * */
    @RequestMapping("/delManyActivityById")
    @ResponseBody
    public String delManyActivityById(String ids){
        System.out.println("ids--->"+ids);
        if(ids != null && !ids.equals("")){
            String[] idss = ids.split(",");
            for(String id:idss){
                System.out.println("要删除的id"+id);
                String activityById = redisTemplate.opsForValue().get("activity"+id);
                if(activityById != null){
                    redisTemplate.delete("activity"+id);
                }
                if(id!=null && !id.equals("")){
                    QueryWrapper q = new QueryWrapper();
                    q.eq("id",id);
                    Activity activity = activityMapper.selectOne(q);
                    activity.setIsDeleted(1);
                    int result = activityMapper.update(activity,q);
                    if(result==0){
                        System.out.println("活动删除失败！");
                        return "fail";
                    }
                }
            }
        }
        System.out.println("活动删除成功！");
        return "success";
    }

}

