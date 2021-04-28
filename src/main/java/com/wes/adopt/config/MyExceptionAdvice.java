package com.wes.adopt.config;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author LengXiaoStudio
 * @ClassName MyExceptionAdvice
 * @date 2021.03.27 18:07
 */

@Controller
public class MyExceptionAdvice implements ErrorController {

    /*@RequestMapping("/error")
    public String handleError(HttpServletRequest request){
        //获取statusCode:401,404,500
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        if(statusCode == 401){
            return "/401";
        }else if(statusCode == 404){
            return "/404";
        }else if(statusCode == 403){
            return "/403";
        }else{
            return "/500";
        }

    }

    @Override
    public String getErrorPath() {
        return "/error";
    }*/

    @Autowired
    HttpServletRequest request;

    @Override
    public String getErrorPath() {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        System.out.println("statusCode:"+statusCode);
        switch(statusCode){
            case 400: return "/error/400";
            case 401: return "/error/401";
            case 403: return "/error/403";
            case 404: return "/error/404";
            case 405: return "/error/405";
            case 406: return "/error/406";
            case 407: return "/error/407";
            case 500: return "/error/500";
            case 501: return "/error/501";
            case 502: return "/error/502";
            case 503: return "/error/503";
            case 504: return "/error/504";
            default: return "/error/505";
        }
    }
}
