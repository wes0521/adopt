package com.wes.adopt.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wes
 * @since 2020-12-18
 */
@Controller
@RequestMapping("/adopt")
public class AdoptController {

    @RequestMapping("")
    public String adopt() {
        return "html/services.html";
    }

}

