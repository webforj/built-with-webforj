package com.pingpal.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.webforj.component.Expanse;
import com.webforj.component.field.TextField;
import com.webforj.component.html.elements.Div;
import com.webforj.component.icons.Icon;
import com.webforj.component.icons.IconButton;
import com.webforj.component.icons.TablerIcon;
import com.webforj.component.layout.flexlayout.FlexAlignment;
import com.webforj.component.layout.flexlayout.FlexDirection;
import com.webforj.component.layout.flexlayout.FlexJustifyContent;
import com.webforj.component.layout.flexlayout.FlexLayout;

public class KeyValue extends Div {

    private FlexLayout layout;
    private List<FlexLayout> rows = new ArrayList<FlexLayout>();
    private LinkedHashMap<String, TextField> keys = new LinkedHashMap<String, TextField>();
    private LinkedHashMap<String, TextField> values = new LinkedHashMap<String, TextField>();

    public KeyValue() {
        layout = new FlexLayout();
        layout.setDirection(FlexDirection.COLUMN);
        add(layout);
    }

    private void createRow() {
        createRow(null, null);
    }

    private void createRow(String keyText, String valueText) {
        String uuid = UUID.randomUUID().toString();

        FlexLayout row = new FlexLayout();
        row.setAlignment(FlexAlignment.CENTER);
        row.setJustifyContent(FlexJustifyContent.BETWEEN);
        rows.add(row);

        TextField key = new TextField();
        key.setPlaceholder("Key");
        key.setWidth("100%");
        key.setExpanse(Expanse.LARGE);
        if (keyText != null) key.setText(keyText);
        keys.put(uuid, key);

        TextField value = new TextField();
        value.setPlaceholder("Value");
        value.setWidth("100%");
        value.setExpanse(Expanse.LARGE);
        if (valueText != null) value.setText(valueText);
        values.put(uuid, value);

        Icon icon = TablerIcon.create("trash");
        IconButton button = new IconButton(icon);
        button.onClick(e -> deleteRow(row, uuid));

        row.add(key, value, button);

        layout.add(row);

        key.onBlur(e -> checkNewRow(key, value));
        value.onBlur(e -> checkNewRow(key, value));
    }

    private void deleteRow(FlexLayout row, String uuid) {
        if (keys.size() == 1) return;

        row.destroy();
        keys.remove(uuid);
        values.remove(uuid);
    }

    private void checkNewRow(TextField key, TextField value) {
        TextField lastKeyField = keys.lastEntry().getValue();
        TextField lastValueField = values.lastEntry().getValue();
        if (!lastKeyField.getValue().trim().isBlank() && !lastValueField.getValue().trim().isBlank()) {
            createRow();
            focus();
        }
    }

    public void focus() {
        if (keys.entrySet().size() == 0) return;
        keys.lastEntry().getValue().focus();
    }

    public HashMap<String, String> getData() {
        HashMap<String, String> params = new HashMap<String, String>();

        Iterator<String> it = keys.keySet().iterator();
        while (it.hasNext()) {
            String uuid = it.next();

            String key = keys.get(uuid).getText().trim();
            String value = values.get(uuid).getText().trim();
            if (key.isBlank() || value.isBlank()) continue;

            params.put(key, value);
        }

        return params;
    }

    private void clear() {
        for (FlexLayout row : rows) {
            row.destroy();
        }

        rows.clear();
        keys.clear();
        values.clear();
    }

    public void setData(HashMap<String, String> data) {
        clear();

        Iterator<String> it = data.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = data.get(key);
            if (key.isBlank() || value.isBlank()) continue;

            createRow(key, value);
        }

        if (data.keySet().size() == 0) {
            createRow();
            focus();
        }
    }
    
}
