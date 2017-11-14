package org.smart4j.plugin.soap;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * SOAP 服务注解
 * Created by yuezhang on 17/11/14.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Soap {

    /**
     * 服务名
     * 用于描述SOAP服务的名称，若为空，则默认使用服务类名为SOAP的服务，比如：CustomerSoapService。
     * @return
     */
    String value() default "";

}
