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

import com.wes.adopt.entity.Admin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

/**
 * @Author LengXiaoStudio
 * @ClassName IndexController
 * @date 2020.12.18 17:00
 */

@Controller
@RequestMapping("")
public class IndexController {

    //前台
    @RequestMapping("toLogin")
    public String login() {
        return "html/login";
    }

    @RequestMapping("toRegister")
    public String register() {
        return "html/register";
    }

    @RequestMapping("index")
    public String index() {
        return "html/index";
    }

    @RequestMapping("toAbout")
    public String about() {
        return "html/about";
    }

    @RequestMapping("toAdopt")
    public String toAdopt() {
        return "redirect:/adopt/pet/findAllPets";
    }

    @RequestMapping("toActivities")
    public String toActivities() {
        return "redirect:/activity/findAllList";
    }

    @RequestMapping("toFamous")
    public String toTeam() {
        return "redirect:/famous/findAll";
    }

    @RequestMapping("toUser")
    public String user() {
        return "/admin/admin-user";
    }

    @RequestMapping("toNav")
    public String toNav() {
        return "/html/nav";
    }

    /*
     * 对登录之前进行拦截时，通过判断登录得是用户还是管理员来跳转到对应的登录界面
     * */
    @RequestMapping("/nav")
    public String nav(String role) {
        System.out.println("nav:" + role);
        if ("user".equals(role)) {
            return "redirect:/toLogin";
        } else {
            return "redirect:/adminLogin";
        }
    }

    //后台
    @RequestMapping("/adminLogin")
    public String adminLogin1(Model model, HttpSession session) {
        Admin admin1 = (Admin) session.getAttribute("admin");
        if (admin1 != null) {
            System.out.println("当前管理员已经登录");
            return "admin/admin-index";
        }
//        model.addAttribute("adminLogin_error","失败");
        return "admin/admin-login";
    }
}
