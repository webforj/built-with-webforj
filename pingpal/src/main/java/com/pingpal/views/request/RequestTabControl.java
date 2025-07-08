package com.pingpal.views.request;

import java.util.HashMap;

import com.pingpal.components.JsonEditor;
import com.pingpal.components.KeyValue;
import com.pingpal.components.TabControl;
import com.pingpal.models.RequestModel;

public class RequestTabControl extends TabControl {

    private KeyValue params;
    private RequestAuthentication authentication;
    private KeyValue headers;
    private JsonEditor body;

    public RequestTabControl() {
        params = new KeyValue();
        addTab("Params", params);

        authentication = new RequestAuthentication();
        addTab("Authentication", authentication);

        headers = new KeyValue();
        addTab("Headers", headers);

        body = new JsonEditor();
        addTab("Body", body);
    }

    public void setData(RequestModel model) {
        params.setData(model.getParams());
        authentication.setData(model.getAuthData());
        headers.setData(model.getHeaders());
        body.setData(model.getBody());
    }

    public HashMap<String, String> getAuthData() {
        return authentication.getData();
    }

    public HashMap<String, String> getParams() {
        return params.getData();
    }

    public HashMap<String, String> getHeaders() {
        return headers.getData();
    }

    public HashMap<String, Object> getBody() {
        return body.getData();
    }
    
}
