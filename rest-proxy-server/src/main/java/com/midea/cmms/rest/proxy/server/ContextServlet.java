package com.midea.cmms.rest.proxy.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 自带WebApplicationContext的Servlet
 * Created by liyilin on 2017/7/7.
 */
public class ContextServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ContextServlet.class);

    private static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private static final String INNER_CONFIG_LOCATION = "classpath:applicationContext-web.xml";

    private Properties configProperties;

    private WebApplicationContext context;

    public String getProperty(String key) {
        return configProperties.getProperty(key);
    }

    @Override
    public void init() throws ServletException {
        initWebApplicationContext();
        initConfigProperties();
    }

    protected ApplicationContext getWebApplicationContext() {
        return context;
    }

    private void initWebApplicationContext() {
        LOG.info("开始初始化WebApplicationContext");

        WebApplicationContext rootApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        context = new XmlWebApplicationContext();
        ((ConfigurableWebApplicationContext) context).setParent(rootApplicationContext);

        // 加载内部配置文件
        StringBuilder stringBuilder = new StringBuilder(INNER_CONFIG_LOCATION);

        // 加载配置文件
        String configLocations = getServletConfig().getInitParameter(CONFIG_LOCATION_PARAM);
        if (configLocations != null) {
            stringBuilder.append(", ");
            stringBuilder.append(configLocations);
        }
        ((ConfigurableWebApplicationContext)context).setConfigLocation(stringBuilder.toString());

        // 刷新
        ((ConfigurableWebApplicationContext) context).refresh();

        getServletContext().setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, context);
    }

    private void initConfigProperties() {
        configProperties = getWebApplicationContext().getBean(Properties.class);
        LOG.info("成功初始化配置对象" + configProperties);
        Global.setConfig(configProperties);
    }
}
