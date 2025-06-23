package com.pingpal.components;

import com.webforj.component.Composite;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.text.Label;

public class Placeholder extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();

    public Placeholder(String text) {
        self.setWidth("100%");
        self.setHeight("100%");
        self.setDirection(FlexDirection.COLUMN);
        self.setJustifyContent(FlexJustifyContent.CENTER);
        self.setAlignment(FlexAlignment.CENTER);

        Label label = new Label(text);
        self.add(label);
    }

    public void setVisible(Boolean visible) {
        self.setStyle("display", visible ? "flex" : "none");
    }
    
}
