package com.midea.cmms.rest.proxy.server.mvc.exception;

import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.WebResponse;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * 默认异常处理器
 */
public class DefaultHandleExceptionResolver implements HandleExceptionResolver {

    private static final Logger LOG = Logger.getLogger(DefaultHandleExceptionResolver.class);
    private static final String PAGE_PATTERN = "framework/error.html";
    private static String PAGE_CONTENT = null;

    static {
        try {
            /*
                获取错误页面模板
             */
            ClassPathResource classPathResource = new ClassPathResource(PAGE_PATTERN);
            InputStream inputStream = classPathResource.getInputStream();
            PAGE_CONTENT = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean support(WebRequest webRequest, Throwable throwable) {
        return true;
    }

    /**
     * 将错误信息填充到错误页面模板
     * @param webRequest
     * @param webResponse
     * @param throwable
     */
    @Override
    public void handle(WebRequest webRequest, WebResponse webResponse, Throwable throwable) {
        LOG.info("[" + webResponse.getStatus() + "] " + throwable.toString());

        int status = webResponse.getStatus();
        String page = PAGE_CONTENT.replace("${errorCode}", status+"");
        page = page.replace("${errorType}", throwable.getClass().getName());
        String message = throwable.getMessage();
        page = page.replace("${errorMsg}", message == null ? "" : message);
        webResponse.renderHtml(page);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
