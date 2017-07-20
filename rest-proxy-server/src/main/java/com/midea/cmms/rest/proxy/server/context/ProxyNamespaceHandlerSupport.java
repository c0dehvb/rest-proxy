package com.midea.cmms.rest.proxy.server.context;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * <p>该类用于注册自定义命名空间<b>http://www.midea.com/cmms/dubbo-config</b>中的自定义元素的处理器</p>
 *
 * <p>需要处理的自定义元素有两个: service, cors</p>
 *
 * Created by liyilin on 2017/7/17.
 *
 * @see HandleMethodBeanDefinitionParser
 * @see CorsConfigBeanDefinitionParser
 */
public class ProxyNamespaceHandlerSupport extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("service",
                new HandleMethodBeanDefinitionParser());
        registerBeanDefinitionParser("cors",
                new CorsConfigBeanDefinitionParser());
    }
}
