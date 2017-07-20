package com.midea.cmms.rest.proxy.server.mvc.view;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.exception.ViewResolveException;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by liyilin on 2017/7/4.
 */
public class ViewResolverManager {

    private List<ViewResolver> viewResolvers;

    public List<ViewResolver> getViewResolvers() {
        return viewResolvers;
    }

    public void setViewResolvers(List<ViewResolver> viewResolvers) {
        this.viewResolvers = viewResolvers;
    }

    public void render(WebRequest request, WebResponse response, Method method, Object result) throws ViewResolveException {
        if (response.getStatus() == 204) return;        // 没有内容返回

        for (ViewResolver viewResolver : viewResolvers) {
            if (viewResolver.support(request, method, result))
                try {
                    viewResolver.render(request, response, method, result);
                } catch (Exception e) {
                    throw new ViewResolveException(e);
                }
        }
    }
}
