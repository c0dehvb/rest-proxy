package com.midea.cmms.rest.proxy.server.sign;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import com.midea.cmms.rest.proxy.server.mvc.interceptor.Interceptor;
import com.midea.cmms.rest.proxy.server.mvc.interceptor.InterceptorChain;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.DataBinder;

import javax.servlet.ServletContext;
import java.lang.invoke.MethodHandles;

/**
 * 签名认真拦截器。某些方法必须经过签名认证的话，必须先通过签名认证才能执行方法。
 */
public class AppSignInterceptor implements Interceptor {
    private static final Logger LOG = Logger.getLogger(MethodHandles.lookup().lookupClass());

    @Value("${mvc.argument.appSign.appId:appId}")
    private String PARAM_APP_ID;

    @Value("${mvc.argument.appSign.appKey:appKey}")
    private String PARAM_APP_KEY;

    @Value("${mvc.argument.appSign.tm:tm}")
    private String PARAM_TIMESTAMP;

    @Value("${mvc.argument.appSign.sign:sign}")
    private String PARAM_SIGNATURE;

    private AppSignChecker appSignChecker;

    @Override
    public void init(ServletContext servletContext) throws Exception {
    }

    @Override
    public Object handle(WebRequest request, WebResponse response, HandleMethod handler, InterceptorChain chain) throws Exception {
        // 如果该方法需要进行方法签名校验
        if (handler.isSign()) {
            LOG.info("开始签名校验: " + handler.toString());
            AppSignature appSign = getAppSignatureByRequest(request);
            LOG.info("获取签名信息: " + appSign);
            appSignChecker.checkSignature(appSign.getAppKey(), appSign.getTm(), appSign.getSignature());
        }
        return chain.doChain(request, response, handler);
    }

    @Override
    public void destroy(ServletContext servletContext) throws Exception {
    }

    @Override
    public int getOrder() {
        return 0;
    }

    private AppSignature getAppSignatureByRequest(WebRequest request) {
        String appIdStr = request.getParameter(PARAM_APP_ID);
        String appKeyStr = request.getParameter(PARAM_APP_KEY);
        String tmStr = request.getParameter(PARAM_TIMESTAMP);
        String signStr = request.getParameter(PARAM_SIGNATURE);

        DataBinder dataBinder = new DataBinder(null);
        AppSignature appSign = new AppSignature();

        appSign.setAppId(dataBinder.convertIfNecessary(appIdStr, Long.class));
        appSign.setAppKey(appKeyStr);
        appSign.setTm(dataBinder.convertIfNecessary(tmStr, Long.class));
        appSign.setSignature(signStr);

        return appSign;
    }

    public void setAppSignChecker(AppSignChecker appSignChecker) {
        this.appSignChecker = appSignChecker;
    }
}
