package com.midea.cmms.rest.proxy.server.mvc.argument;

import com.google.gson.*;
import com.midea.cmms.rest.proxy.server.WebRequest;
import com.midea.cmms.rest.proxy.server.mvc.method.HandleMethod;
import com.midea.cmms.rest.proxy.server.mvc.method.HttpParameterInfo;
import org.apache.log4j.Logger;
import org.springframework.core.Ordered;

import java.io.IOException;

/**
 * Created by liyilin on 2017/7/14.
 */
public class JsonBodyMethodArgumentResolver extends AbstractMethodArgumentResolver {
    private static Logger LOG = Logger.getLogger(JsonBodyMethodArgumentResolver.class);

    @Override
    public boolean support(WebRequest request, HandleMethod method, HttpParameterInfo param) {
        if (param.isPathVariable() || param.isRequestBody())
            return false;

        try {
            JsonElement jsonBody = request.getJsonBody();
            if (jsonBody == null) return false;

            if (jsonBody.isJsonArray()) {
                JsonArray jsonArray = jsonBody.getAsJsonArray();
                return jsonArray.size() > param.getIndex();
            } else if (jsonBody.isJsonObject()) {
                JsonObject jsonObject = jsonBody.getAsJsonObject();
                String paramName = param.getParamName();
                return jsonObject.has(paramName);
            }
            return false;
        } catch (IOException e) {
            LOG.error(e.getMessage());
            return false;
        }
    }

    @Override
    public Object resolve(WebRequest request, HandleMethod method, HttpParameterInfo param) throws Exception {
        String paramName = param.getParamName();
        JsonElement jsonBody = request.getJsonBody();
        Gson gson = getGson(param.getDateFormat());

        if (jsonBody.isJsonArray()) {
            JsonArray jsonArray = jsonBody.getAsJsonArray();
            JsonElement jsonElement = jsonArray.get(param.getIndex());
            return gson.fromJson(jsonElement.toString(), param.getType());
        } else if (jsonBody.isJsonObject()) {
            JsonObject jsonObject = jsonBody.getAsJsonObject();
            JsonElement jsonElement = jsonObject.get(paramName);
            return gson.fromJson(jsonElement.toString(), param.getType());
        }
        return null;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1;
    }
}
