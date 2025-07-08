package com.pingpal.views.response;

import com.pingpal.helpers.BytesFormatter;
import com.webforj.component.html.elements.Div;
import com.webforj.component.text.Label;

public class ResponseBytes extends Div {

    private Label label;

    public ResponseBytes() {
        addClassName("response-bytes");

        label = new Label("-").addClassName("response-bytes-label");
        add(label);
    }

    public void setData(int bytes) {
        label.setText(BytesFormatter.format(bytes));
    }

    public void clear() {
        label.setText("-");
    }
    
}