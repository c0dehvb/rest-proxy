package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.midea.cmms.base.FUtil;
import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import com.midea.cmms.rest.proxy.server.sign.AppSignature;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.DataBinder;

/**
 * 用于处理返回AppSignature信息串的字符串参数
 */
public class AppSignatureMethodArgumentResolver extends AbstractMethodArgumentResolver {
    @Value("${mvc.argument.appSign.appId:appId}")
    private String PARAM_APP_ID;

    @Value("${mvc.argument.appSign.appKey:appKey}")
    private String PARAM_APP_KEY;

    @Value("${mvc.argument.appSign.tm:tm}")
    private String PARAM_TIMESTAMP;

    @Value("${mvc.argument.appSign.sign:sign}")
    private String PARAM_SIGNATURE;

    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        return param.getType() == String.class && param.isAppSign();
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        // 从表单参数重获取签名信息
        String appIdStr = request.getParameter(PARAM_APP_ID);
        String appKeyStr = request.getParameter(PARAM_APP_KEY);
        String tmStr = request.getParameter(PARAM_TIMESTAMP);
        String signStr = request.getParameter(PARAM_SIGNATURE);

        DataBinder dataBinder = getDataBinder();
        AppSignature appSign = new AppSignature();

        appSign.setAppId(dataBinder.convertIfNecessary(appIdStr, Long.class));
        appSign.setAppKey(appKeyStr);
        appSign.setTm(dataBinder.convertIfNecessary(tmStr, Long.class));
        appSign.setSignature(signStr);

        return FUtil.toJson(appSign);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
