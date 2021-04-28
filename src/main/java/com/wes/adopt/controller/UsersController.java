package com.wes.adopt.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wes.adopt.entity.Pet;
import com.wes.adopt.entity.User;
import com.wes.adopt.mapper.PetMapper;
import com.wes.adopt.mapper.UsersMapper;
import com.wes.adopt.utils.PageModel;
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
 * 前端控制器
 * </p>
 * <p>
 * 添加用户、登录功能再拦截器
 *
 * @author wes
 * @since 2020-12-18
 */
@Controller
@RequestMapping("/user")
public class UsersController {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private PetMapper petMapper;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /*
     * 查询用户信息-导航
     * */
    @RequestMapping("/findPersonInfo")
    public String findPersonInfo(String navId, Model model, User user, HttpSession session) {
        System.out.println("=============");
        System.out.println("user-->" + user);
        System.out.println("导肮菜单navId-->" + navId);
        QueryWrapper<User> q = new QueryWrapper();
        q.eq("id", user.getId());
        int result = usersMapper.update(user, q);
        System.out.println("result->" + result);
        if (result == 0) {
            System.out.println("修改失败");
            model.addAttribute("error", "修改失败");
        } else {
            System.out.println("修改成功");
            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id", user.getId());
            User loginUser = usersMapper.selectOne(q1);
            session.setAttribute("user", loginUser);
            model.addAttribute("user", loginUser);
        }
        if ("about".equals(navId)) {
            return "redirect:/toAbout";
        } else if ("adopt".equals(navId)) {
            return "redirect:/adopt/pet/findAllPets";
        } else if ("activities".equals(navId)) {
            return "redirect:/toActivities";
        } else if ("team".equals(navId)) {
            return "redirect:/toFamous";
        }
        return "html/index";
    }


    /*
     * 修改当前用户密码
     * */
    @RequestMapping("/modifyPwd")
    public String modifyPwd(Model model, Integer id, String password, HttpSession session) {
        System.out.println("=============");
        System.out.println("password-->" + password);
        System.out.println("id-->" + id);
        String userById = redisTemplate.opsForValue().get("user" + id);
        if (userById != null) {
            redisTemplate.delete("user" + id);
        }
        User user = new User();
        user.setId(id);
        user.setPassword(password);
        int result = usersMapper.updateById(user);
        System.out.println("result->" + result);
        if (result == 0) {
            System.out.println("修改失败");
            model.addAttribute("error", "修改失败");
            return "html/index";
        } else {
            System.out.println("修改成功");
            QueryWrapper q1 = new QueryWrapper();
            q1.eq("id", id);
            User loginUser = usersMapper.selectOne(q1);
            session.setAttribute("user", loginUser);
            model.addAttribute("user", loginUser);
            return "html/login";
        }
    }

    /*
     * 查询所有用户列表
     * */
    @RequestMapping("/findAllUserList")
    public String findAllUserList(@RequestParam(defaultValue = "1") Integer pageIndex,
                                  @RequestParam(defaultValue = "2") Integer pageSize,
                                  String username, Model model) {
        System.out.println("username-->" + username);
        PageModel pageModel = new PageModel();
        pageModel.setPageIndex(pageIndex);
        pageModel.setPageSize(pageSize);
        System.out.println("pageSize->" + pageSize);
        IPage<User> userPage = new Page<>(pageIndex, pageSize);//参数一是当前页，参数二是每页个数
        if ("".equals(username) || null == username) {
            QueryWrapper wrapper1 = new QueryWrapper();
            wrapper1.eq("is_deleted", 0);
            int recordCount = usersMapper.selectCount(wrapper1);
            pageModel.setRecordCount(recordCount);
            System.out.println("空用户名recordCount->" + recordCount);
            userPage = usersMapper.selectPage(userPage, wrapper1);
        } else {
            QueryWrapper q = new QueryWrapper();
            q.like("username", username);
            q.eq("is_deleted", 0);
            int recordCount2 = usersMapper.selectCount(q);
            pageModel.setRecordCount(recordCount2);
            System.out.println("模糊用户名recordCount->" + recordCount2);
            userPage = usersMapper.selectPage(userPage, q);
        }
        List<User> users = userPage.getRecords();
        System.out.println("user------>" + users.size());
        users.forEach(System.out::println);//循环打印管理员列表
        if (username != null) {
            model.addAttribute("username", username);
        } else {
            model.addAttribute("username", "");
        }
        model.addAttribute("users", users);
        model.addAttribute("pageModel", pageModel);
        return "/admin/admin-user";
    }

    /*
     * 删除单个用户通过id
     * */
    @RequestMapping("/delUserById")
    @ResponseBody
    public String delUserById(String id) {
        System.out.println("要删除的用户id" + id);
        String userById = redisTemplate.opsForValue().get("user" + id);
        if (userById != null) {
            redisTemplate.delete("user" + id);
        }
        QueryWrapper q = new QueryWrapper();
        q.eq("id", id);
        User user = usersMapper.selectOne(q);
        user.setIs_deleted(1);
        int result = usersMapper.update(user, q);
        if (result > 0) {
            System.out.println("管理员删除成功！");
        } else {
            System.out.println("管理员删除失败！");
            return "fail";
        }
        return "success";
    }

    /*
     * 通过id批量删除用户
     * */
    @RequestMapping("/delManyUserById")
    @ResponseBody
    public String delManyUserById(String ids) {
        System.out.println("ids--->" + ids);
        boolean flag = true;
        if (ids != null && !ids.equals("")) {
            String[] idss = ids.split(",");
            for (String id : idss) {
                System.out.println("要删除的id" + id);
                String userById = redisTemplate.opsForValue().get("user" + id);
                if (userById != null) {
                    redisTemplate.delete("user" + id);
                }
                if (id != null && !id.equals("")) {
                    QueryWrapper qw = new QueryWrapper();
                    qw.eq("id", id);
                    User user = usersMapper.selectOne(qw);
                    user.setIs_deleted(1);
                    int result = usersMapper.update(user, qw);
                    if (result == 0) {
                        flag = false;
                    }
                }
            }
        }
        if (flag) {
            System.out.println("用户删除成功！");
            return "success";
        } else {
            System.out.println("用户删除失败！");
            return "fail";
        }
    }

    /*
     * 修改单个管理员密码之前查询个人密码==通过id
     * */
    @RequestMapping("/findPwdById")
    public String findPwdById(String id, Model model) {
        System.out.println("要修改的管理员id-->" + id);
        QueryWrapper<User> q = new QueryWrapper();
        q.eq("id", id);
        User user = usersMapper.selectOne(q);
        model.addAttribute("userPwdById", user);
        return "/admin/member-userPassword";
    }

    /*
     * 修改单个用户密码==通过id
     * */
    @RequestMapping("/modifyUserPwdById")
    @ResponseBody
    public String modifyUserPwdById(String id, String password, Model model) {
        System.out.println("新的password------>" + password);
        System.out.println("要修改的管理员id-++++->" + id);
        String userById = redisTemplate.opsForValue().get("user" + id);
        if (userById != null) {
            redisTemplate.delete("user" + id);
        }
        QueryWrapper<User> q = new QueryWrapper();
        q.eq("id", id);
        User user = usersMapper.selectOne(q);
        user.setPassword(password);
        int result = usersMapper.update(user, q);
        if (result == 0) {
            System.out.println("修改密码失败");
//            model.addAttribute("error","修改密码失败");
            return "fail";
        } else {
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
     * 修改单个用户之前查询个人信息==通过id
     * */
    @RequestMapping("/findUserInfoById")
    public String findUserInfoById(String id, Model model) {
        System.out.println("要修改的用户id-->" + id);
        User user = null;
        String userById = redisTemplate.opsForValue().get("user" + id);
        if (userById == null || "".equals(userById)) {
            QueryWrapper<User> q = new QueryWrapper();
            q.eq("id", id);
            user = usersMapper.selectOne(q);
            redisTemplate.opsForValue().set("user" + id, JSON.toJSONString(user), 60, TimeUnit.SECONDS);
        } else {
            user = JSON.parseObject(userById, User.class);
        }
        System.out.println("要修改的用户信息user->" + user);
        model.addAttribute("userById", user);
        return "/admin/member-editUser";
    }

    /*
     * 修改单个用户信息==通过id
     * */
    @RequestMapping("/modifyUserById")
    @ResponseBody
    public String modifyUserById(String id, Model model, User user, HttpSession session) {
        System.out.println("user------>" + user);
        System.out.println("要修改的用户id-++++->" + id);
        String userById = redisTemplate.opsForValue().get("user" + id);
        if (userById != null) {
            redisTemplate.delete("user" + id);
        }
        QueryWrapper<User> q = new QueryWrapper();
        q.eq("id", user.getId());
        int result = usersMapper.update(user, q);
        System.out.println("result->" + result);
        if (result == 0) {
            System.out.println("修改用户信息失败");
            model.addAttribute("error", "修改用户信息失败");
            return "fail";
        } else {
            System.out.println("修改用户信息成功");
//            QueryWrapper q1 = new QueryWrapper();
//            q1.eq("id",admin.getId());
//            Admin loginAdmin = adminMapper.selectOne(q1);
//            session.setAttribute("admin",loginAdmin);
//            model.addAttribute("admin",loginAdmin);
            return "success";
        }
    }


    /*
     * 申请领养之前的查询动物信息
     * */
    @RequestMapping("/addAdopt")
    @ResponseBody
    public String addAdopt(String id, HttpSession session) {
        System.out.println("要申请领养的动物id-->" + id);
        QueryWrapper<Pet> q = new QueryWrapper();
        q.eq("id", id);
        Pet pet = petMapper.selectOne(q);
        System.out.println("要申请领养的动物：" + pet);
        try {
            User user = (User) session.getAttribute("user");
            if (user != null) {
                System.out.println("当前用户：" + user);
                QueryWrapper q1 = new QueryWrapper();
                q1.eq("id", id);
                pet.setUid(user.getId());
                pet.setState(1);
                int r1 = petMapper.update(pet, q1);

                QueryWrapper q2 = new QueryWrapper();
                q2.eq("id", user.getId());
                user.setPid(Integer.parseInt(id));
                user.setState(1);
                int r2 = usersMapper.update(user, q2);

                if (r1 == 0 || r2 == 0) {
                    System.out.println("领养失败");
                    return "fail";
                }

                //针对修改过数据库之后，当前用户的领养状态会及时更正
                User user1 = (User) session.getAttribute("user");
                User user2 = usersMapper.selectById(user1.getId());
                session.setAttribute("user", user2);
                System.out.println("当前用户的信息" + user2);

                //修改当前用户信息新领养的动物名称
                QueryWrapper qw1 = new QueryWrapper();
                qw1.eq("id", user2.getPid());
                Pet pet1 = petMapper.selectOne(qw1);
                if (pet1 != null) {
                    session.setAttribute("pname", pet.getPetName());
                }
                /*String animalById = redisTemplate.opsForValue().get("animal"+id);
                if(animalById != null){
                    redisTemplate.delete("animal"+id);
                    System.out.println("redis缓存中有id为"+id+"的动物得数据,用户领养成功之后需进行删除缓存");//加上报错，序列化转换错误
                }*/
                System.out.println("领养的宠物；" + pet1);
                System.out.println("领养成功");
                return "success";
            }
        } catch (Exception e) {
            System.out.println("异常,session没有user属性: " + e);
            System.out.println("领养失败");
        }
        System.out.println("领养失败");
        return "fail";
    }

    /*
     * 退出登录
     * */
    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        System.out.println("用户成功退出系统");
        return "html/login";
    }

}

