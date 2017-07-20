package com.midea.cmms.rest.proxy.server;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Properties;

/**
 * 自带WebApplicationContext的Servlet
 * Created by liyilin on 2017/7/7.
 */
public class ContextServlet extends HttpServlet {
    private static final Logger LOG = Logger.getLogger(ContextServlet.class);

    private static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";

    private static final String INNER_CONFIG_LOCATION = "classpath:framework/inner-app-web.xml";

    private Properties configProperties;

    public String getProperty(String key) {
        return configProperties.getProperty(key);
    }

    @Override
    public void init() throws ServletException {
        initWebApplicationContext();
        initConfigProperties();
    }

    protected ApplicationContext getWebApplicationContext() {
        return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
    }

    private void initWebApplicationContext() {
        LOG.info("开始初始化WebApplicationContext");

        // 根据父应用上下文创建WebApplicationContext
        WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        AutowireCapableBeanFactory autowireCapableBeanFactory = webApplicationContext.getAutowireCapableBeanFactory();


        if (autowireCapableBeanFactory instanceof BeanDefinitionRegistry) {
            XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader((BeanDefinitionRegistry) autowireCapableBeanFactory);

            // 加载内部配置文件
            LOG.info("加载内部WebApplicationContext配置文件 - " + INNER_CONFIG_LOCATION);
            reader.loadBeanDefinitions(INNER_CONFIG_LOCATION);

            // 加载配置文件
            String configLocation = getServletConfig().getInitParameter(CONFIG_LOCATION_PARAM);
            if (configLocation == null) {
                LOG.info("没有给出Servlet参数[contextConfigLocation]，将不进行配置文件加载");
            } else {
                try {
                    LOG.info("开始读取WebApplicationContext配置文件 - " + configLocation);
                    reader.loadBeanDefinitions(configLocation);
                } catch (Exception e) {
                    LOG.error("读取WebApplicationContext配置文件失败: " + e.getMessage(), e);
                }
            }
        }
    }

    private void initConfigProperties() {
        configProperties = getWebApplicationContext().getBean(Properties.class);
        LOG.info("成功初始化配置对象" + configProperties);
        Global.setConfig(configProperties);
    }
}
