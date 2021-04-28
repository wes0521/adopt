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

import com.alibaba.fastjson.JSON;
import com.wes.adopt.entity.Famous;
import com.wes.adopt.mapper.FamousMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author LengXiaoStudio
 * @ClassName FamousController
 * @date 2021.03.21 22:22
 */
@Controller
@RequestMapping("/famous")
public class FamousController {

    @Resource
    private FamousMapper famousMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @RequestMapping("/findAll")
    public String findAll(Model model) {
        List<Famous> famousList=null;
        String famous = redisTemplate.opsForValue().get("famous");
        if(famous == null || "".equals(famous)){
            famousList = famousMapper.selectList(null);
            redisTemplate.opsForValue().set("famous", JSON.toJSONString(famousList),60, TimeUnit.SECONDS);
        }
        else{
            famousList = JSON.parseArray(famous,Famous.class);
        }
        if(famousList!=null){
            famousList.forEach(System.out::println);
        }
        try {
            int i = 1;
            for (Famous f : famousList) {
                model.addAttribute("famous" + i, f);
                i++;
            }
        }catch (Exception e){
            System.out.println("异常,可能famousList为空，可能数据库为空："+e);
        }
        return "/html/team";
    }
}
