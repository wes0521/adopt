package com.wes.adopt.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.adopt.entity.Admin;
import com.wes.adopt.mapper.AdminMapper;
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
import javax.servlet.http.HttpSession;
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
@RequestMapping("/adopt/admin")
public class AdminController {

    @Resource
    private AdminMapper adminMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /*
    * 管理员登录
    * */
    @RequestMapping("/adminLogin")
    public String adminLogin(Admin admin, Model model, HttpSession session){
        System.out.println("adminName:"+admin.getAdminName());//
        System.out.println("adminPwd:"+admin.getAdminPwd());//
        /*Admin admin1 = (Admin) session.getAttribute("admin");
        if(admin1 != null){
            if(admin.getAdminName().equals(admin1.getAdminName())){
                System.out.println("当前管理员已经登录");
                return "admin/admin-index";
            }
            else {
                System.out.println("其他管理员已经登录");
                return "admin/admin-login";
            }
        }*/
        System.out.println("未有管理员登录");
        QueryWrapper q = new QueryWrapper();
        q.eq("adminName",admin.getAdminName());
        q.eq("adminPwd",admin.getAdminPwd());
        Admin loginAdmin = adminMapper.selectOne(q);
        if(null == loginAdmin){
            model.addAttribute("adminLogin_error","用户名或密码错误");
            return "admin/admin-login";
        }
        else{
            session.setAttribute("admin",loginAdmin);
            return "admin/admin-index";
        }
    }

    /*
    * 查询所有管理员列表
    * */
    @RequestMapping("/adminFindAdminList")
    public String adminFindAdminList(@RequestParam(defaultValue = "1") Integer pageIndex,
                                     @RequestParam(defaultValue = "2") Integer pageSize,
                                     String adminName, Model model){
        System.out.println("adminName-->"+adminName);
        PageModel pageModel = new PageModel();
        pageModel.setPageIndex(pageIndex);
        pageModel.setPageSize(pageSize);
        System.out.println("pageSize->"+pageSize);
        IPage<Admin> adminPage = new Page<>(pageIndex, pageSize);//参数一是当前页，参数二是每页个数
        Admin a = (Admin) session.getAttribute("admin");
        if("".equals(adminName)||null==adminName){
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("is_deleted",0);
            wrapper1.ne("id",a.getId()); //不查询当前管理员，以防自己删除自己
            int recordCount = adminMapper.selectCount(wrapper1);
            pageModel.setRecordCount(recordCount);
            System.out.println("空用户名recordCount->"+recordCount);
            adminPage = adminMapper.selectPage(adminPage,wrapper1);
        }else{
            QueryWrapper q = new QueryWrapper();
            q.like("adminName",adminName);
            q.eq("is_deleted",0);
            q.ne("id",a.getId()); //不查询当前管理员，以防自己删除自己
            int recordCount2 = adminMapper.selectCount(q);
            pageModel.setRecordCount(recordCount2);
            System.out.println("模糊用户名recordCount->"+recordCount2);
            adminPage = adminMapper.selectPage(adminPage,q);
        }
        List<Admin> admins = adminPage.getRecords();
        System.out.println("admin------>"+admins.size());
        admins.forEach(System.out::println);//循环打印管理员列表
        if(adminName!=null){
            model.addAttribute("adminName",adminName);
        }
        else {
            model.addAttribute("adminName","");
        }
        model.addAttribute("admins",admins);
        model.addAttribute("pageModel",pageModel);
        return "/admin/admin-admin";
    }
    /*
    * 修改当前管理员信息
    * */
    @RequestMapping("/modifyAdminInfo")
    public String modifyAdminInfo(String id, Model model, Admin admin, HttpSession session){
        Admin admin1 = (Admin) session.getAttribute("admin");
        if (admin1 == null) {
            System.out.println("管理员未登录");
            return "/adminLogin";
        }
        System.out.println("=============");
        System.out.println("admin-->"+admin);
        System.out.println("管理员id-->"+id);
        //数据修改之后若有缓存就需要删除
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        if(adminById != null){
            redisTemplate.delete("admin"+id);
        }
        QueryWrapper<Admin> q = new QueryWrapper();
        q.eq("id",admin.getId());
        int result = adminMapper.update(admin,q);
        System.out.println("result->"+result);
        if(result==0){
            System.out.println("修改失败");
            model.addAttribute("error","修改失败");
        }else{
            System.out.println("修改成功");
            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id",admin.getId());
            Admin loginAdmin = adminMapper.selectOne(q1);
            session.setAttribute("admin",loginAdmin);
            model.addAttribute("admin",loginAdmin);
        }
        return "/admin/personal-info";
    }

    /*
    * 查询当前管理员信息
    * */
    @RequestMapping("/findAdminInfo")
    public String findPersonInfo(String id, Model model, HttpSession session){
        System.out.println("管理员id-->"+id);
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        Admin admin;
        if(adminById == null || "".equals(adminById)){
            QueryWrapper<Admin> q = new QueryWrapper();
            q.eq("is_deleted",0);
            q.eq("id",id);
            admin = adminMapper.selectOne(q);
            redisTemplate.opsForValue().set("admin"+id,JSON.toJSONString(admin),60, TimeUnit.SECONDS);
        }else{
            admin = JSON.parseObject(adminById,Admin.class);
        }
        System.out.println("当前管理员信息admin->"+admin);
        session.setAttribute("admin",admin);
        model.addAttribute("admin",admin);
        return "/admin/personal-info";
    }

    /*
    * 添加管理员用户功能之前的操作-----查询用户是否已存在
    * */
    @RequestMapping("/findIsHaveAdmin")
    @ResponseBody
    public Boolean findIsHaveAdmin(String adminName){
        System.out.println("验证要新增的用户名是否已存在的用户名："+adminName);
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("adminName",adminName);
        Admin admin = adminMapper.selectOne(wrapper);
        if(null==admin){
            return false;
        }
        return true;
    }

    /*
    * 添加管理员用户
    * */
    @RequestMapping("/addAdmin")
    @ResponseBody
    public String addAdmin(Admin admin){
        System.out.println("要新增的用户信息："+admin);
        int result = adminMapper.insert(admin);
        if(result!=0){
            return "success";
        }
        return "fail";
    }

    /*
     * 修改单个管理员之前查询个人信息==通过id
     * */
    @RequestMapping("/findInfoById")
    public String findInfo(String id, Model model){
        System.out.println("要修改的管理员id-->"+id);
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        Admin admin;
        if(adminById == null || "".equals(adminById)){
            QueryWrapper<Admin> q = new QueryWrapper();
            q.eq("is_deleted",0);
            q.eq("id",id);
            admin = adminMapper.selectOne(q);
            redisTemplate.opsForValue().set("admin"+id,JSON.toJSONString(admin),60, TimeUnit.SECONDS);
        }else{
            admin = JSON.parseObject(adminById,Admin.class);
        }
        System.out.println("要修改的管理员信息admin->"+admin);
        model.addAttribute("adminById",admin);
        return "/admin/member-editAdmin";
    }

    /*
     * 修改单个管理员信息==通过id
     * */
    @RequestMapping("/modifyAdminById")
    @ResponseBody
    public String modifyAdminById(String id, Model model, Admin admin, HttpSession session){
        System.out.println("=============");
        System.out.println("admin------>"+admin);
        System.out.println("要修改的管理员id-++++->"+id);
        //数据修改之后若有缓存就需要删除
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        if(adminById != null){
            redisTemplate.delete("admin"+id);
        }
        QueryWrapper<Admin> q = new QueryWrapper();
        q.eq("id",admin.getId());
        int result = adminMapper.update(admin,q);
        System.out.println("result->"+result);
        if(result==0){
            System.out.println("修改管理员信息失败");
            model.addAttribute("error","修改管理员信息失败");
            return "fail";
        }else{
            System.out.println("修改管理员信息成功");
            return "success";
        }
    }

    /*
     * 修改单个管理员密码之前查询个人密码==通过id
     * */
    @RequestMapping("/findPwdById")
    public String findPwdById(String id, Model model){
        System.out.println("要修改的管理员id-->"+id);
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        Admin admin;
        if(adminById == null || "".equals(adminById)){
            QueryWrapper<Admin> q = new QueryWrapper();
            q.eq("is_deleted",0);
            q.eq("id",id);
            admin = adminMapper.selectOne(q);
            redisTemplate.opsForValue().set("admin"+id,JSON.toJSONString(admin),60, TimeUnit.SECONDS);
        }else{
            admin = JSON.parseObject(adminById,Admin.class);
        }
        String oldPwd = admin.getAdminPwd();
        System.out.println("要修改管理员密码的个人信息admin->"+admin);
        model.addAttribute("adminPwdById",admin);
        model.addAttribute("oldPwd",oldPwd);
        return "/admin/member-adminPassword";
    }

    /*
     * 修改单个管理员密码==通过id
     * */
    @RequestMapping("/modifyAdminPwdById")
    @ResponseBody
    public String modifyAdminPwdById(String id, String adminPwd,Model model){
        System.out.println("admin---->"+adminPwd);
        System.out.println("要修改的管理员id-+++->"+id);
        //数据修改之后若有缓存就需要删除
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        if(adminById != null){
            redisTemplate.delete("admin"+id);
        }
        QueryWrapper<Admin> q = new QueryWrapper();
        q.eq("id",id);
        Admin admin = adminMapper.selectOne(q);
        admin.setAdminPwd(adminPwd);
        int result = adminMapper.update(admin,q);
        System.out.println("result->"+result);
        if(result==0){
            System.out.println("修改密码失败");
            model.addAttribute("error","修改密码失败");
            return "fail";
        }else{
            System.out.println("修改密码成功");
//            QueryWrapper q1 = new QueryWrapper();
//            q1.eq("id",admin.getId());
//            Admin loginAdmin = adminMapper.selectOne(q1);
//            session.setAttribute("admin",loginAdmin);
//            model.addAttribute("admin",loginAdmin);
            return "success";
        }
    }

    /*
    * 通过id删除管理员
    * */
    @RequestMapping("/delAdminById")
    @ResponseBody
    public String delAdminById(String id){
//        int result = adminMapper.deleteById(id);
        System.out.println("要删除的id"+id);
        String adminById = redisTemplate.opsForValue().get("admin"+id);
        if(adminById != null){
            redisTemplate.delete("adminById"+id);
            System.out.println("redis缓存中有id为"+id+"的管理员得数据,进行删除缓存");
        }
        QueryWrapper q = new QueryWrapper();
        q.eq("id",id);
        Admin admin = adminMapper.selectOne(q);
        admin.setIsDeleted(1);
        int result = adminMapper.update(admin,q);
        if(result>0){
            System.out.println("管理员删除成功！");
        }else {
            System.out.println("管理员删除失败！");
            return "fail";
        }
        return "success";
    }

    /*
     * 通过id批量删除管理员
     * */
    @RequestMapping("/delManyAdminById")
    @ResponseBody
    public String delManyAdminById(String ids){
        System.out.println("ids--->"+ids);
//        int result = adminMapper.deleteById(id);
        boolean flag=true;
        if(ids != null && !ids.equals("")){
            String[] idss = ids.split(",");
            for(String id:idss){
                System.out.println("要删除的id"+id);
                String adminById = redisTemplate.opsForValue().get("admin"+id);
                if(adminById != null){
                    redisTemplate.delete("adminById"+id);
                    System.out.println("redis缓存中有id为"+id+"的管理员的数据,进行删除缓存");
                }
                if(id!=null && !id.equals("")){
                    QueryWrapper q = new QueryWrapper();
                    q.eq("id",id);
                    Admin admin = adminMapper.selectOne(q);
                    admin.setIsDeleted(1);
                    int result = adminMapper.update(admin,q);
                    if(result==0){
                        flag = false;
                    }
                }
            }
        }
        if(flag){
            System.out.println("管理员删除成功！");
            return "success";
        }else{
            System.out.println("管理员删除失败！");
            return "fail";
        }
    }

    /*
    * 管理员退出登录
    * */
    @RequestMapping("/adminLogout")
    public String adminLogout(HttpSession session){
       session.removeAttribute("admin");
       return "/admin/admin-login";
    }
}

