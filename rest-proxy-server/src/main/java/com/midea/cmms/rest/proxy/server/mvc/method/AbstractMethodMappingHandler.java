package com.midea.cmms.rest.proxy.server.mvc.method;

import com.midea.cmms.rest.proxy.server.exception.RequestPathConflictException;
import org.apache.log4j.Logger;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 抽象方法映射处理器。
 * 负责进行方法映射查询的逻辑，对方法映射配置的加载在派生类中完成。
 */
public abstract class AbstractMethodMappingHandler implements MethodMappingHandler {
    private static final Logger LOG = Logger.getLogger(AbstractMethodMappingHandler.class);

    protected Map<String, HandleMethod> reqPathMapCache = new HashMap<>();
    protected AntPathMatcher antPathMatcher = new AntPathMatcher();

    /**
     * 获取需要映射的Service方法
     * @return
     */
    protected abstract Collection<HandleMethod> initHandleMethods();

    public void init() {
        Collection<HandleMethod> handleMethods = initHandleMethods();

        setReqPathMap(handleMethods);
    }

    /**
     * 初始化请求路径与方法的映射关系
     * @param handleMethods
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void setReqPathMap(Collection<HandleMethod> handleMethods) {
        for (HandleMethod handleMethod : handleMethods) {
            String method = handleMethod.getHttpMethod().toUpperCase();
            String reqPath = method + " " + handleMethod.getRequestPattern();

            if (reqPathMapCache.containsKey(reqPath))
                throw new RequestPathConflictException(reqPath);

            LOG.info("注册方法与请求路径的映射关系：{" + reqPath + "} - " + handleMethod);

            reqPathMapCache.put(reqPath, handleMethod);
        }
    }

    @Override
    public HandleMethod handle(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String method = request.getMethod().toUpperCase();

        // 跨域请求前需要遇到的协议
        if (method.equals("OPTIONS")) {
            method = request.getHeader("Access-Control-Request-Method");
        }

        String requestPath = method + " " + requestURI;

        // 尝试找直接映射路径
        if (reqPathMapCache.containsKey(requestPath)) {
            return reqPathMapCache.get(requestPath);
        }
        // Ant风格URL路径匹配
        else {
            return antPathMatch(reqPathMapCache.keySet(), requestPath);
        }
    }

    private HandleMethod antPathMatch(Collection<String> antPatterns, String requestPath) {
        for (String pattern : antPatterns) {
            if (antPathMatcher.match(pattern, requestPath))
                return reqPathMapCache.get(pattern);
        }
        return null;
    }
}
