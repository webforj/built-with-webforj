package com.pingpal.views.response;

import java.net.http.HttpHeaders;

import com.pingpal.components.Placeholder;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;

public class ResponseHeaders extends Div {

    private Placeholder placeholder;
    private Paragraph text;

    public ResponseHeaders() {
        setWidth("100%");
        setHeight("100%");
        setStyle("overflow-x", "auto");
        setStyle("word-wrap", "break-word");

        placeholder = new Placeholder("No response headers available.");
        add(placeholder);

        text = new Paragraph();
        text.setVisible(false);
        add(text);
    }

    public void setData(HttpHeaders response) {
        StringBuilder content = new StringBuilder();
        
        response.map().forEach((name, values) -> {
            String value = String.join(", ", values);
            content.append(name + ": " + value + "<br>");
        });
        
        placeholder.setVisible(false);

        text.setHtml(content.toString());
        text.setVisible(true);
    }

    public void clear() {
        text.setText("");
        text.setVisible(false);
        placeholder.setVisible(true);
    }

}
