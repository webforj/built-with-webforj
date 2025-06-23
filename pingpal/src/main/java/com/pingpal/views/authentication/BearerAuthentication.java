package com.pingpal.views.authentication;

import java.util.HashMap;

import com.pingpal.helpers.IAuthenticationType;
import com.webforj.component.Composite;
import com.webforj.component.Expanse;
import com.webforj.component.field.TextField;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class BearerAuthentication extends Composite<FlexLayout> implements IAuthenticationType {

    private FlexLayout self = getBoundComponent();
    private TextField token;

    public BearerAuthentication() {
        self.setWidth("100%");
        self.setHeight("100%");
        self.setDirection(FlexDirection.COLUMN);
        self.setSpacing("10px");

        token = new TextField("Token");
        token.setExpanse(Expanse.LARGE);
        self.add(token);
    }

    @Override
    public HashMap<String, String> getData() {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("type", "BEARER");
        data.put("token", token.getText().trim());
        return data;
    }

    public void setData(HashMap<String, String> data) {
        if (data != null && data.containsKey("token")) {
            token.setText(data.get("token").toString());
        } else {
            token.setText("");
        }
    }

    @Override
    public void setVisible(Boolean visible) {
        self.setVisible(visible);
    }
    
}
