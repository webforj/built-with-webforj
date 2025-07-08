package com.pingpal.views;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.pingpal.components.Placeholder;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;
import com.webforj.environment.ObjectTable;

public class Console extends Div {

    private Placeholder placeholder;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Console() {
        setWidth("100%");
        setHeight("100%");
        setStyle("overflow-x", "auto");
        setStyle("word-wrap", "break-word");

        placeholder = new Placeholder("Your console is empty.");
        add(placeholder);

        ObjectTable.put("CONSOLE", this);
    }

    public void print(String message) {
        placeholder.setVisible(false);
        
        String timestamp = LocalDateTime.now().format(formatter);

        Paragraph text = new Paragraph();
        text.setHtml(timestamp + " | " + message);
        add(text);
    }

}
