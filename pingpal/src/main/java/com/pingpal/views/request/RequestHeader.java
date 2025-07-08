package com.pingpal.views.request;

import java.util.function.Consumer;

import com.pingpal.models.RequestModel;
import com.pingpal.services.RequestService;
import com.webforj.component.Composite;
import com.webforj.component.button.Button;
import com.webforj.component.button.event.ButtonClickEvent;
import com.webforj.component.html.elements.Div;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;
import com.webforj.component.text.Label;

public class RequestHeader extends Composite<FlexLayout> {

    private FlexLayout self = getBoundComponent();
    private Label label;
    private Consumer<RequestModel> onSave;
    private RequestModel model;

    public RequestHeader() {
        self.setWidth("100%");
        self.setAlignment(FlexAlignment.CENTER);
        self.setJustifyContent(FlexJustifyContent.BETWEEN);

        Div left = new Div().setWidth("100%");
        label = new Label();
        left.add(label);

        FlexLayout right = new FlexLayout().setWidth("100%");
        right.setAlignment(FlexAlignment.CENTER);
        right.setJustifyContent(FlexJustifyContent.END);
        right.setSpacing("10px");

        Button save = new Button("Save");
        save.onClick(this::acceptSave);
        right.add(save);

        self.add(left, right);
    }

    private void acceptSave(ButtonClickEvent event) {
        onSave.accept(model);
    }

    public void onSave(Consumer<RequestModel> onSave) {
        this.onSave = onSave;
    }

    private void redraw() {
        label.setText(model.getName() != null ? model.getName() : "");
    }

    public void setData(RequestModel model) {
        this.model = model;
        redraw();
    }
    
}
