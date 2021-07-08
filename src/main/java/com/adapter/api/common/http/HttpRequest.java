package com.adapter.api.common.http;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URI;

public class HttpRequest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private HttpRequestBase httpRequestBase;
    private HttpClient client;


    private HttpRequest() {
        client = HttpClientBuilder.create().build();
    }

    public static HttpRequest of(){
        return new HttpRequest();
    }

    public HttpRequest post(){
        httpRequestBase = new HttpPost();
        return this;
    }

    public HttpRequest post(URI uri){
        httpRequestBase = new HttpPost(uri);
        return this;
    }

    public HttpRequest get(){
        httpRequestBase = new HttpGet();
        return this;
    }

    public HttpRequest get(URI uri){
        httpRequestBase = new HttpGet(uri);
        return this;
    }

    public HttpRequest setUri(URI uri){
        httpRequestBase.setURI(uri);
        return this;
    }

    public HttpRequest addHeader(String key, String value){
        httpRequestBase.addHeader(key, value);
        return this;
    }

    public HttpRequest addHeader(Header header){
        httpRequestBase.addHeader(header);
        return this;
    }

    public HttpRequest setEntity(HttpEntity entity){
        if(httpRequestBase.getMethod().equalsIgnoreCase(HttpMethod.POST.name())){
            ((HttpPost) httpRequestBase).setEntity(entity);
        }else{
            log.error("this HttpMethod can't execute setEntity. [{}]",httpRequestBase.getMethod());
        }
        return this;
    }

    public HttpResponse execute() throws IOException {
        return client.execute(httpRequestBase);
    }
}
