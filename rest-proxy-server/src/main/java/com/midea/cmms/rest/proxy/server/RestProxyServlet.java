package com.midea.cmms.rest.proxy.server;

import com.midea.cmms.rest.proxy.server.exception.CorsException;
import com.midea.cmms.rest.proxy.server.exception.MethodHandleMappingException;
import com.midea.cmms.rest.proxy.server.exception.MethodArgumentHandleException;
import com.midea.cmms.rest.proxy.server.exception.ViewResolveException;
import com.midea.cmms.rest.proxy.server.mvc.interceptor.Interceptor;
import com.midea.cmms.rest.proxy.server.mvc.interceptor.InterceptorChain;
import com.midea.cmms.rest.proxy.server.mvc.interceptor.InterceptorProxy;
import com.midea.cmms.rest.proxy.server.mvc.argument.MethodArgumentResolver;
import com.midea.cmms.rest.proxy.server.mvc.argument.MethodArgumentResolverManager;
import com.midea.cmms.rest.proxy.server.mvc.exception.HandleExceptionResolver;
import com.midea.cmms.rest.proxy.server.mvc.exception.HandleExceptionResolverManager;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.MethodMappingHandler;
import com.midea.cmms.rest.proxy.server.mvc.view.ViewResolver;
import com.midea.cmms.rest.proxy.server.mvc.view.ViewResolverManager;
import com.midea.cmms.rest.proxy.server.mvc.method.ContextMethodMappingHandler;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Rest代理Servlet
 * 用于对Http请求进行解析，并分发到对应的Dubbo Service方法上调用。
 * Created by liyilin on 2017/7/1.
 */
public class RestProxyServlet extends ContextServlet {
    private static final Logger LOG = Logger.getLogger(RestProxyServlet.class);

    private static final String DEFAULT_STRATEGIES_PATH = "framework/RestProxyServlet.properties";

    private static final String METHOD_ARGUMENT_RESOLVERS_PROP = "methodArgumentResolvers";

    private static final String VIEW_RESOLVERS_PROP = "viewResolvers";

    private static final String HANDLE_EXCEPTION_RESOLVERS_PROP = "handleExceptionResolvers";

    private static final String INTERCEPTORS_PROP = "interceptors";

    private static final Properties defaultStrategies;

    static {
        try {
            Resource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH);
            defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not load 'RestProxyServlet.properties': " + ex.getMessage());
        }
    }

    private MethodMappingHandler methodMappingHandler;
    private MethodArgumentResolverManager methodArgumentResolverManager;
    private ViewResolverManager viewResolverManager;
    private HandleExceptionResolverManager handleExceptionResolverManager;
    private InterceptorProxy interceptorProxy;

    private void initMethodMappingHandler() {
        LOG.info("初始化MethodMappingHandler");
        methodMappingHandler = new ContextMethodMappingHandler(getWebApplicationContext());
    }

    private void initMethodArgumentResolvers() {
        LOG.info("初始化MethodArgumentResolvers");
        List<MethodArgumentResolver> methodArgumentResolverList = initServletListBean(METHOD_ARGUMENT_RESOLVERS_PROP);
        methodArgumentResolverManager = createBean(getWebApplicationContext(), MethodArgumentResolverManager.class);
        methodArgumentResolverManager.setMethodArgumentResolvers(methodArgumentResolverList);
    }

    private void initViewResolvers() {
        LOG.info("初始化ViewResolvers");
        List<ViewResolver> viewResolvers = initServletListBean(VIEW_RESOLVERS_PROP);
        viewResolverManager = createBean(getWebApplicationContext(), ViewResolverManager.class);
        viewResolverManager.setViewResolvers(viewResolvers);
    }

    private void initHandleExcpetionResolvers() {
        LOG.info("初始化HandleExceptionResolvers");
        List<HandleExceptionResolver> handleExceptionResolvers = initServletListBean(HANDLE_EXCEPTION_RESOLVERS_PROP);
        handleExceptionResolverManager = createBean(getWebApplicationContext(), HandleExceptionResolverManager.class);
        handleExceptionResolverManager.setHandleExceptionResolvers(handleExceptionResolvers);
    }

    private void initInterceptors() {
        LOG.info("初始化Interceptors");
        interceptorProxy = new InterceptorProxy();

        HandleMethodInvokeInterceptor handleMethodInvokeInterceptor =
                createBean(getWebApplicationContext(), HandleMethodInvokeInterceptor.class);
        handleMethodInvokeInterceptor.dubboServlet = this;

        try {
            interceptorProxy.init(getServletContext());
            interceptorProxy.getInterceptors().add(handleMethodInvokeInterceptor);
        } catch (Exception e) {
            LOG.error("拦截器初始化失败: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends Ordered> List<T> initServletListBean(String propertyName) {
        String prop = defaultStrategies.getProperty(propertyName);
        String[] classes = StringUtils.commaDelimitedListToStringArray(prop);
        List<T> list = new ArrayList<>();

        for (String className : classes) {
            try {
                Class<?> clazz = Class.forName(className);
                T bean = (T) getWebApplicationContext().getAutowireCapableBeanFactory().createBean(clazz);
                list.add(bean);
                LOG.info("初始化" + clazz.getName() + "成功");
            } catch (ClassNotFoundException e) {
                throw new BeanInitializationException("找不到类：" + className, e);
            }
        }

        OrderComparator.sort(list);

        return list;
    }

    private <T> T createBean(ApplicationContext context, Class<T> beanClass) {
        return context.getAutowireCapableBeanFactory().createBean(beanClass);
    }

    @Override
    public void init() throws ServletException {

        LOG.info("初始化开始");
        super.init();

        try {
            initMethodMappingHandler();
            initMethodArgumentResolvers();
            initViewResolvers();
            initHandleExcpetionResolvers();
            initInterceptors();
        } catch (Throwable e) {
            LOG.error("Servlet初始化错误：" + e.getMessage());
            System.exit(0);
        }

        LOG.info("初始化结束");
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        WebRequest webRequest = new WebRequest(req);
        WebResponse webResponse = new WebResponse(resp);
        try {
            doService(webRequest, webResponse);
        } catch (MethodHandleMappingException e) {
            resp.setStatus(404);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        } catch (MethodArgumentHandleException e) {
            resp.setStatus(400);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            resp.setStatus(400);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
            resp.setStatus(500);
            handleExceptionResolverManager.handle(webRequest, webResponse, e.getCause());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            resp.setStatus(500);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        } catch (CorsException e) {
            resp.setStatus(403);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500);
            handleExceptionResolverManager.handle(webRequest, webResponse, e);
        }
    }

    protected void doService(WebRequest req, WebResponse resp)
            throws Exception {

        LOG.info("接收请求：[" + req.getMethod() + "] " + req.getRequestURI());

        // 根据URI获取调用方法
        HandleMethod handleMethod = methodMappingHandler.handle(req);

        if (handleMethod == null)
            throw new MethodHandleMappingException("找不到路径：[" + req.getMethod() + "] " + req.getRequestURI());
        else
            LOG.info("映射请求方法：" + handleMethod.getServiceClassName() + "." + handleMethod.getMethod().getName());

        // 分析PathVariable参数
        req.setRequestMapping(handleMethod.getRequestPattern());

        // 开始执行拦截器
        Object result = interceptorProxy.handle(req, resp, handleMethod, null);

        // 渲染结果
        viewResolverManager.render(req, resp, handleMethod.getMethod(), result);
    }

    protected Object doHandle(WebRequest req, WebResponse resp, HandleMethod handleMethod)
            throws ViewResolveException, InvocationTargetException, IllegalAccessException, MethodArgumentHandleException {
        // 处理方法参数
        Object[] args = getArgs(req, handleMethod);

        // 执行方法
        return handleMethod.invoke(args);
    }

    private Object[] getArgs(WebRequest req, HandleMethod handleMethod) throws MethodArgumentHandleException {
        Object[] args = new Object[handleMethod.getMethod().getParameterTypes().length];
        int idx = 0;

        for (HttpParameterInfo param : handleMethod.getHttpParameterInfos()) {
            args[idx++] = methodArgumentResolverManager.resolve(req, handleMethod, param);
        }

        return args;
    }

    private static class HandleMethodInvokeInterceptor implements Interceptor {
        RestProxyServlet dubboServlet;

        @Override
        public void init(ServletContext servletContext) throws Exception {}

        @Override
        public Object handle(WebRequest request, WebResponse response, HandleMethod handler, InterceptorChain chain) throws Exception {
            return dubboServlet.doHandle(request, response, handler);
        }

        @Override
        public void destroy(ServletContext servletContext) throws Exception {}

        @Override
        public int getOrder() {
            return Ordered.LOWEST_PRECEDENCE;
        }
    }
}
