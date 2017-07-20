package com.midea.cmms.rest.proxy.server;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by liyilin on 2017/7/11.
 */
public class WebResponse extends HttpServletResponseWrapper {

    public static final String DEFAULT_CHARSET = Charset.defaultCharset().displayName();

    public WebResponse(HttpServletResponse response) {
        super(response);
    }

    private void doWrite(String str) {
        try {
            getWriter().write(str);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void renderJson(String json, String charset) {
        setContentType("application/json;charset=" + charset);
        setCharacterEncoding(charset);
        doWrite(json);
    }

    public void renderJson(String json) {
        renderJson(json, DEFAULT_CHARSET);
    }

    public void renderText(String text, String charset) {
        setContentType("text/plain;charset=" + charset);
        setCharacterEncoding(charset);
        doWrite(text);
    }

    public void renderText(String text) {
        renderText(text, DEFAULT_CHARSET);
    }

    public void renderHtml(String html, String charset) {
        setContentType("text/html;charset=" + charset);
        setCharacterEncoding(charset);
        doWrite(html);
    }

    public void renderHtml(String html) {
        renderHtml(html, DEFAULT_CHARSET);
    }
}
