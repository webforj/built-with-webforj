package com.pingpal.views.response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.pingpal.components.Placeholder;
import com.webforj.component.html.elements.Div;
import com.webforj.component.html.elements.Paragraph;

public class ResponseBody extends Div {

    private Paragraph paragraph;
    private Placeholder placeholder;
    private Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();

    public ResponseBody() {
        setWidth("100%");
        setHeight("100%");
        setStyle("overflow-x", "auto");
        setStyle("word-wrap", "break-word");

        paragraph = new Paragraph();
        paragraph.setVisible(false);

        placeholder = new Placeholder("No response body available.");
        add(paragraph, placeholder);
    }

    public void setData(String response) {
        placeholder.setVisible(false);
        paragraph.setVisible(true);

        try {
            JsonElement jsonElement = JsonParser.parseString(response);
            String formattedJson = gson.toJson(jsonElement);
            
            if (formattedJson.equals("null")) {
                paragraph.setHtml("");
            } else {
                paragraph.setHtml("<pre>" + formattedJson + "</pre>");
            }
        } catch (Exception e) {
            paragraph.setHtml(escapeHtml(response));
        }
    }

    public void clear() {
        paragraph.setText("");
        paragraph.setVisible(false);
        placeholder.setVisible(true);
    }

    public String escapeHtml(String input) {
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
    
}