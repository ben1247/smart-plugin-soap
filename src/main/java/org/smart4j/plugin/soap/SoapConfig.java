package org.smart4j.plugin.soap;

import org.smart4j.framework.helper.ConfigHelper;

/**
 * 从配置文件中获取相关属性
 * Created by yuezhang on 17/11/14.
 */
public class SoapConfig {

    public static boolean isLog(){
        return ConfigHelper.getBoolean(SoapConstant.LOG);
    }

}
