package com.adapter.api.common.util.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;

public class HttpUtils {

    private static Gson gson = new GsonBuilder().create();

    public static <T> T extractEntity(HttpResponse resp, Class<T> clazz) throws IOException {
        return gson.fromJson(respEntityToJson(resp), clazz);
    }

    public static <T> T extractEntity(HttpResponse resp, Type type) throws IOException {
        return gson.fromJson(respEntityToJson(resp), type);
    }

    public static boolean is2xxSuccessful(HttpResponse resp) {
        int statusCode = resp.getStatusLine().getStatusCode();
        return statusCode >= 200 && statusCode < 300;
    }

    private static String respEntityToJson(HttpResponse resp) throws IOException {
        return EntityUtils.toString(resp.getEntity());
    }
}
