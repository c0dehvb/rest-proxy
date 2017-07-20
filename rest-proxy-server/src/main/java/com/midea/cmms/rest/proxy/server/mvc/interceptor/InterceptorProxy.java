package com.midea.cmms.rest.proxy.server.mvc.interceptor;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by liyilin on 2017/7/19.
 */
public class InterceptorProxy implements Interceptor {
    private static final Logger LOG = Logger.getLogger(InterceptorProxy.class);

    private List<Interceptor> interceptors;

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    @Override
    public void init(ServletContext servletContext) throws Exception {
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        Map<String, Interceptor> beansOfType = applicationContext.getBeansOfType(Interceptor.class);
        interceptors = new ArrayList<>(beansOfType.values());

        OrderComparator.sort(interceptors);

        for (Interceptor interceptor : interceptors) {
            interceptor.init(servletContext);
            LOG.info("初始化" + interceptor.getClass() + "成功");
        }
    }

    @Override
    public Object handle(WebRequest request, WebResponse response, HandleMethod handler, InterceptorChain chain) throws Exception {
        InnerInterceptorChain interceptorChain = new InnerInterceptorChain(interceptors.iterator());
        return interceptorChain.doChain(request, response, handler);
    }

    @Override
    public void destroy(ServletContext servletContext) throws Exception {
        for (Interceptor interceptor : interceptors) {
            interceptor.destroy(servletContext);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private static class InnerInterceptorChain implements InterceptorChain {
        private Iterator<Interceptor> iterator;

        public InnerInterceptorChain(Iterator<Interceptor> iterator) {
            this.iterator = iterator;
        }

        @Override
        public Object doChain(WebRequest request, WebResponse response, HandleMethod handler) throws Exception {
            if (iterator.hasNext()) {
                Interceptor next = iterator.next();
                return next.handle(request, response, handler, this);
            }
            return null;
        }
    }
}
