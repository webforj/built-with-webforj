package com.pingpal.views.response;

import com.pingpal.helpers.StatusCodeFormatter;
import com.webforj.component.html.elements.Div;
import com.webforj.component.text.Label;

public class ResponseStatusCode extends Div {

    private Label statusCode;

    public ResponseStatusCode() {
        addClassName("response-status-code");

        statusCode = new Label("-").addClassName("response-status-code-label");
        add(statusCode);
    }

    public void setData(int code) {
        statusCode.setText(StatusCodeFormatter.format(code));

        if (code < 300) {
            setStyle("background-color", "green");
            setStyle("border-color", "green");
            statusCode.setStyle("color", "white");
        } else {
            setStyle("background-color", "red");
            setStyle("border-color", "red");
            statusCode.setStyle("color", "white");
        }
    }

    public void setError() {
        statusCode.setText("Error");
        setStyle("background-color", "red");
    }

    public void clear() {
        statusCode.setText("-");
        statusCode.setStyle("color", "initial");

        setStyle("background-color", "initial");
        setStyle("border-color", "#d7d7d7");
    }
    
}
