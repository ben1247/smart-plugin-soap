package org.smart4j.plugin.soap;

import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.frontend.ServerFactoryBean;
import org.apache.cxf.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * SOAP助手类
 * 首先通过static块来初始化CXF提供的日志拦截器。
 * 然后利用CXF提供的ServerFactoryBean来发布SOAP服务。
 * 最后利用CXF提供的ClientProxyFactoryBean来创建SOAP客户端。
 * Created by yuezhang on 17/11/14.
 */
public class SoapHelper {

    private static final List<Interceptor<? extends Message>> inInterceptorList = new ArrayList<>();

    private static final List<Interceptor<? extends Message>> outInterceptorList = new ArrayList<>();

    static {
        // 添加Logging Interceptor
        if (SoapConfig.isLog()){
            LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
            inInterceptorList.add(loggingInInterceptor);
            LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
            outInterceptorList.add(loggingOutInterceptor);
        }
    }

    /**
     * 发布SOAP服务
     * @param wsdl
     * @param interfaceClass
     * @param implementInstance
     */
    public static void publishService(String wsdl , Class<?> interfaceClass , Object implementInstance){
       ServerFactoryBean factory = new ServerFactoryBean();
       factory.setAddress(wsdl);
       factory.setServiceClass(interfaceClass);
       factory.setServiceBean(implementInstance);
       factory.setInInterceptors(inInterceptorList);
       factory.setOutInterceptors(outInterceptorList);
       factory.create();
    }

    /**
     * 创建SOAP客户端
     * @param wsdl
     * @param interfaceClass
     * @param <T>
     * @return
     */
    public static <T> T createClient(String wsdl , Class<? extends T> interfaceClass){
        ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
        factory.setAddress(wsdl);
        factory.setServiceClass(interfaceClass);
        factory.setInInterceptors(inInterceptorList);
        factory.setOutInterceptors(outInterceptorList);
        return factory.create(interfaceClass);
    }
}
