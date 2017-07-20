package com.midea.cmms.rest.proxy.server.mvc.interceptor;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.exception.CorsException;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsProcessor;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.DefaultCorsProcessor;

import javax.servlet.ServletContext;
import java.io.IOException;

/**
 * 跨域拦截器
 *
 * <p>处理方法级别的跨域请求管理</p>
 *
 * Created by liyilin on 2017/7/19.
 */
public class CorsInterceptor implements Interceptor {
    private static final Logger LOG = Logger.getLogger(CorsInterceptor.class);

    private CorsProcessor processor = new DefaultCorsProcessor();

    @Override
    public void init(ServletContext servletContext) throws Exception {}

    @Override
    public Object handle(WebRequest request, WebResponse response, HandleMethod handler, InterceptorChain chain) throws Exception {
        ServerHttpRequest servletServerHttpRequest = new ServletServerHttpRequest(request);
        HttpHeaders headers = servletServerHttpRequest.getHeaders();
        if (CorsUtils.isCorsRequest(request)) {     // 判断是否跨域请求
            LOG.info("接收到源自 [" + headers.getOrigin() + "] 的跨域请求: " + request.getRequestURL());

            CorsConfiguration corsConfiguration = handler.getCorsConfig();
            if (corsConfiguration != null) {
                try {
                    boolean isValid = this.processor.processRequest(corsConfiguration, request, response);
                    if (isValid) {        // 判断是否通过跨域允许校验
                        // 如果是预请求，只需要简单响应即可，不需要执行方法
                        if (CorsUtils.isPreFlightRequest(request)) {
                            response.setStatus(204);        // 没有内容但成功的返回码
                            return null;
                        }
                        else
                            return chain.doChain(request, response, handler);
                    }
                } catch (IOException e) {
                    LOG.error("校验跨域请求失败: " + e.getMessage());
                    throw new CorsException(e.getMessage(), e);
                }
            }
        } else {
            return chain.doChain(request, response, handler);
        }
        throw new CorsException("方法 " + handler.getServiceClassName() + "." + handler.getMethod().getName()
                + " 没有来自 " + headers.getAccessControlRequestMethod() + " - " + request.getRequestURL().toString() + " 的跨域权限");
    }

    @Override
    public void destroy(ServletContext servletContext) throws Exception {}

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
