package com.pingpal.views.request;

import java.util.HashMap;

import com.pingpal.components.Placeholder;
import com.pingpal.helpers.IAuthenticationType;
import com.pingpal.views.authentication.ApiKeyAuthentication;
import com.pingpal.views.authentication.BasicAuthentication;
import com.pingpal.views.authentication.BearerAuthentication;
import com.webforj.component.Expanse;
import com.webforj.component.html.elements.Div;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.list.ChoiceBox;
import com.webforj.component.list.event.ListSelectEvent;

public class RequestAuthentication extends Div {
    
    private FlexLayout right;
    private ChoiceBox authType;
    private Placeholder placeholder;
    private ApiKeyAuthentication apiKeyAuthentication;
    private BasicAuthentication basicAuthentication;
    private BearerAuthentication bearerAuthentication;
    private IAuthenticationType activeAuth;
    private HashMap<String, String> data;

    public RequestAuthentication() {
        setWidth("100%");
        setHeight("100%");

        FlexLayout layout = new FlexLayout();
        layout.setJustifyContent(FlexJustifyContent.BETWEEN);
        layout.setSpacing("10px");
        layout.setWidth("100%");
        layout.setHeight("100%");
        add(layout);

        Div left = new Div().setWidth("100%");
        left.setMaxWidth("400px");
        left.setHeight("100%");

        authType = new ChoiceBox("Auth Type");
        authType.add("NO_AUTH", "No Auth");
        authType.add("API_KEY", "API Key");
        authType.add("BASIC", "Basic Auth");
        authType.add("BEARER", "Bearer Token");
        authType.selectIndex(0);
        authType.setWidth("100%");
        authType.setExpanse(Expanse.LARGE);
        authType.onSelect(this::onTypeChange);
        left.add(authType);

        right = new FlexLayout();
        right.setDirection(FlexDirection.COLUMN);
        right.setSpacing("10px");
        right.setWidth("100%");
        right.setHeight("100%");
        layout.add(left, right);

        placeholder = new Placeholder("This request does not use any authentication.");
        right.add(placeholder);
    }

    private void onTypeChange(ListSelectEvent event) {
        if (activeAuth != null) activeAuth.setVisible(false);

        String type = event.getSelectedItem().getKey().toString();
        showAuthType(type);
    }

    private void showAuthType(String type) {
        if (activeAuth != null) activeAuth.setVisible(false);

        switch (type) {
            case "API_KEY":
                placeholder.setVisible(false);

                if (apiKeyAuthentication == null) {
                    apiKeyAuthentication = new ApiKeyAuthentication();
                    right.add(apiKeyAuthentication);
                }

                apiKeyAuthentication.setVisible(true);
                apiKeyAuthentication.setData(data);

                activeAuth = apiKeyAuthentication;
                break;
            case "BASIC":
                placeholder.setVisible(false);

                if (basicAuthentication == null) {
                    basicAuthentication = new BasicAuthentication();
                    right.add(basicAuthentication);
                }

                basicAuthentication.setVisible(true);
                basicAuthentication.setData(data);

                activeAuth = basicAuthentication;
                break;
            case "BEARER":
                placeholder.setVisible(false);

                if (bearerAuthentication == null) {
                    bearerAuthentication = new BearerAuthentication();
                    right.add(bearerAuthentication);
                }

                bearerAuthentication.setVisible(true);
                bearerAuthentication.setData(data);

                activeAuth = bearerAuthentication;
                break;
            default:
                placeholder.setVisible(true);
                activeAuth = null;
                break;
        }
    }

    public HashMap<String, String> getData() {
        if (activeAuth == null) return null;
        return activeAuth.getData();
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;

        if (data == null || !data.containsKey("type")) {
            authType.selectKey("NO_AUTH");
            showAuthType("NO_AUTH");
            return;
        }

        String type = data.get("type").toString();
        authType.selectKey(type);
        showAuthType(type);
    }

}
