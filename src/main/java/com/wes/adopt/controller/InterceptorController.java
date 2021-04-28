package com.wes.adopt.controller;

/**
 * *
 * ////////////////////////////////////////////////////////////////////
 * //                          _ooOoo_                               //
 * //                         o8888888o                              //
 * //                         88" . "88                              //
 * //                         (| ^_^ |)                              //
 * //                         O\  =  /O                              //
 * //                      ____/`---'\____                           //
 * //                    .'  \\|     |//  `.                         //
 * //                   /  \\|||  :  |||//  \                        //
 * //                  /  _||||| -:- |||||-  \                       //
 * //                  |   | \\\  -  /// |   |                       //
 * //                  | \_|  ''\---/''  |   |                       //
 * //                  \  .-\__  `-`  ___/-. /                       //
 * //                ___`. .'  /--.--\  `. . ___                     //
 * //              ."" '<  `.___\_<|>_/___.'  >'"".                  //
 * //            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
 * //            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
 * //      ========`-.____`-.___\_____/___.-`____.-'========         //
 * //                           `=---='                              //
 * //      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
 * //            佛祖保佑       永不宕机      永无BUG                  //
 * ////////////////////////////////////////////////////////////////////
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wes.adopt.entity.Pet;
import com.wes.adopt.entity.User;
import com.wes.adopt.mapper.PetMapper;
import com.wes.adopt.mapper.UsersMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @Author LengXiaoStudio
 * @ClassName InterceptorController
 * @date 2021.01.28 15:49
 */
@Controller
@RequestMapping("/interceptor")
public class InterceptorController {

    @Resource
    private UsersMapper usersMapper;

    @Resource
    private PetMapper petMapper;

    /*
    * 用户登录
    * */
    @RequestMapping("/login")
    @ResponseBody
    public String login(User user, Model model, HttpSession session){
        System.out.println("username:"+user.getUsername());//
        System.out.println("password:"+user.getPassword());//
        session.setAttribute("pname","无");
        QueryWrapper q = new QueryWrapper();
        q.eq("username",user.getUsername());
        q.eq("password",user.getPassword());
        q.eq("is_deleted",0);
        User loginUser = usersMapper.selectOne(q);
        if(null == loginUser){
            return "error";
        }
        else{
            session.setAttribute("user",loginUser);
            QueryWrapper qw = new QueryWrapper();
            qw.eq("id",loginUser.getPid());
            Pet pet = petMapper.selectOne(qw);

            System.out.println("领养的宠物"+pet);
            if(pet != null){
                System.out.println("pname"+pet.getPetName());
                session.setAttribute("pname",pet.getPetName());
            }

//            session.setAttribute("state",state);
//            model.addAttribute("state",state);
            return "success";
        }
    }

    /*
    * 用户注册
    * */
    @RequestMapping("/register")
    @ResponseBody
    public String register(User user, HttpSession session){
        System.out.println(user);//
        int rows = usersMapper.insert(user);
        if(rows==0){
            return "fail";
        }
        session.setAttribute("user",user);
        return "success";
    }

    /*
    * 验证码
    * */
    @RequestMapping("/valCode")
    @ResponseBody
    public String code(String code, Model model, HttpSession session){
        System.out.println("原验证码："+code);//
        if(null!=code){
            code = code.toLowerCase();
        }
        System.out.println("不区分大小写之后的验证码："+code);//
        if(!session.getAttribute("code").equals(code)){
            model.addAttribute("error","验证码错误，请重新输入！");
            return "codeError";
        }else{
            return "success";
        }
    }
}
