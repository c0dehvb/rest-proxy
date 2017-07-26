package com.midea.cmms.rest.proxy.server.mvc.view;

import com.google.gson.Gson;
import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;

import java.lang.reflect.Method;

/**
 * 把方法运行结果以Json串的形式响应
 */
public class GsonViewResolver implements ViewResolver {

    @Override
    public boolean support(WebRequest request, Method method, Object result) {
        // 目前只支持Json格式返回视图
        return true;
    }

    @Override
    public void render(WebRequest request, WebResponse response, Method method, Object result) throws Exception {
        Gson gson = new Gson();
        String json = gson.toJson(result);
        response.renderJson(json);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
