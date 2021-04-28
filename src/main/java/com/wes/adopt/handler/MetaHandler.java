package com.wes.adopt.handler;

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

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Author LengXiaoStudio
 * @ClassName MetaHandler
 * @date 2021.01.30 13:23
 */

@Component
public class MetaHandler implements MetaObjectHandler {
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("gmt_create",new Date(),metaObject);//new Date() 时间类型与实体类对象对应
        this.setFieldValByName("gmt_modified",new Date(),metaObject);
//        this.setFieldValByName("updateTime", LocalDateTime.now(),metaObject);//LocalDateTime.now()
        this.setFieldValByName("version", 0,metaObject);
        this.setFieldValByName("is_deleted", 0,metaObject);
    }

    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("gmt_modified",new Date(),metaObject);
    }
}
