/**
 * Created by IntelliJ IDEA.
 * User: 枫桥夜泊
 * Date: 2020/4/8
 * Time: 12:37
 */
package com.wes.adopt.utils;

import java.util.UUID;

public class IdUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }
}
