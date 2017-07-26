package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.validation.DataBinder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liyilin on 2017/7/14.
 */
public abstract class AbstractMethodArgumentResolver implements MethodArgumentResolver {

    private String defaultDatePattern;

    @Value("${mvc.argument.datepattern:'yyyy-MM-dd HH:mm:ss'}")
    public void setDefaultDatePattern(String defaultDatePattern) {
        this.defaultDatePattern = defaultDatePattern;
    }

    protected DataBinder getDataBinder(String datePattern) {
        if (datePattern == null)
            datePattern = this.defaultDatePattern;

        DateFormat dateFormat = new SimpleDateFormat(datePattern);

        DataBinder dataBinder = new DataBinder(null);
        dataBinder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
        return dataBinder;
    }

    protected DataBinder getDataBinder() {
        return getDataBinder(null);
    }

    protected Gson getGson(String datePattern) {
        if (datePattern == null)
            datePattern = this.defaultDatePattern;

        GsonBuilder builder = new GsonBuilder();
        builder.setDateFormat(datePattern);
        return builder.create();
    }

    protected Gson getGson() {
        return getGson(null);
    }
}
