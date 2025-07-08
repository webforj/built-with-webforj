package com.pingpal.models;

import java.util.HashMap;

public class RequestModel {
    
    private String id;
    private String name;
    private String method;
    private String url;
    private HashMap<String, String> params;
    private HashMap<String, String> headers;
    private HashMap<String, String> authData;
    private HashMap<String, Object> body;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setAuthData(HashMap<String, String> authData) {
        this.authData = authData;
    }

    public HashMap<String, String> getAuthData() {
        return authData;
    }

    public void setBody(HashMap<String, Object> body) {
        this.body = body;
    }

    public HashMap<String, Object> getBody() {
        return body;
    }

    public static RequestModel create(String name) {
        RequestModel request = new RequestModel();
        request.setName(name);
        return request;
    }

    @Override
    public String toString() {
        return "RequestModel | ID: " + id + " | Name: " + name;
    }

}
