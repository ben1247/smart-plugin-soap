package org.smart4j.plugin.soap;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ClassHelper;
import org.smart4j.framework.util.CollectionUtil;
import org.smart4j.framework.util.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.annotation.WebServlet;
import java.util.Set;

/**
 * SOAP Servlet
 * 这个servlet拦截所有的SOAP请求
 * Created by yuezhang on 17/11/14.
 */
@WebServlet(urlPatterns = SoapConstant.SERVLET_URL,loadOnStartup = 0)
public class SoapServlet extends CXFNonSpringServlet{

    @Override
    protected void loadBus(ServletConfig sc) {
        // 初始化CXF总线
        super.loadBus(sc);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        // 发布SOAP服务
        publishSoapService();
    }

    private void publishSoapService(){
        // 遍历所有标注了SOAP注解的类
        Set<Class<?>> soapClassSet = ClassHelper.getClassSetByAnnotation(Soap.class);
        if (CollectionUtil.isNotEmpty(soapClassSet)){
            for (Class<?> soapClass : soapClassSet){
                // 获取SOAP地址
                String address = getAddress(soapClass);
                // 获取SOAP类的接口
                Class<?> soapInterfaceClass = getSoapInterfaceClass(soapClass);
                // 获取SOAP类的实例
                Object soapInstance = BeanHelper.getBean(soapClass);
                // 发布SOAP服务
                SoapHelper.publishService(address,soapInterfaceClass,soapInstance);
            }
        }
    }

    private Class<?> getSoapInterfaceClass(Class<?> soapClass){
        // 获取SOAP实现类的第一个接口作为SOAP服务接口
        return soapClass.getInterfaces()[0];
    }

    private String getAddress(Class<?> soapClass){
        String address;
        // 若SOAP注解的value属性不为空，则获取当前值，否则获取类名
        String soapValue = soapClass.getAnnotation(Soap.class).value();
        if(StringUtil.isNotEmpty(soapValue)){
            address = soapValue;
        }else {
            address = getSoapInterfaceClass(soapClass).getSimpleName();
        }
        // 确保最前面只有一个 /
        if (!address.startsWith("/")){
            address = "/" + address;
        }
        address = address.replaceAll("\\/+","/");

        return address;
    }
}
