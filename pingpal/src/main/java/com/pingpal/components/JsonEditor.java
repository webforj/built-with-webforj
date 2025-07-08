package com.pingpal.components;

import java.util.HashMap;
import java.util.UUID;

import com.google.gson.Gson;
import com.webforj.annotation.JavaScript;
import com.webforj.annotation.StyleSheet;
import com.webforj.component.html.elements.Div;

@StyleSheet("https://cdn.jsdelivr.net/npm/jsoneditor@10.2.0/dist/jsoneditor.min.css")
@JavaScript("https://cdn.jsdelivr.net/npm/jsoneditor@10.2.0/dist/jsoneditor.min.js")
public class JsonEditor extends Div {

    private final String uuid = UUID.randomUUID().toString();
    private String activeJson = "{}";

    public JsonEditor() {
        setWidth("100%");
        setHeight("100%");
        setAttribute("id", "editor_" + uuid);
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        String script = """
            function waitForScript(src) {
                return new Promise((res, rej) => {
                    let s = [...document.scripts].find(x => x.src === src);
                    if (s) return s.readyState === 'complete' || s.dataset.loaded ? res() : s.addEventListener('load', res);
                    s = document.head.appendChild(Object.assign(document.createElement('script'), {src, async: true}));
                    s.onload = () => { s.dataset.loaded = 1; res(); };
                    s.onerror = rej;
                });
            }

            waitForScript('https://cdn.jsdelivr.net/npm/jsoneditor@10.2.0/dist/jsoneditor.min.js').then(() => {
                const options = {"mode": "code"}
                const editor = new JSONEditor(component, options);
                document.getElementById('editor_%s')._jsoneditor = editor;
            });
        """;
        getElement().executeJs(script.formatted(uuid));

        applyJson();
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Object> getData() {
        String script = "try { document.getElementById('editor_" + uuid + "')._jsoneditor.get(); } catch (err) { '' }";
        String json = (String) getElement().executeJs(script);
        
        if (json == null || json.isEmpty()) {
            return new HashMap<>();
        }

        return new Gson().fromJson(json, HashMap.class);
    }

    public void setData(HashMap<String, Object> data) {
        String json = new Gson().toJson(data);
        
        if (json == null || json.isBlank() || json.equals("null")) {
            json = "{}";
        } else {
            json = json.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n").replace("\r", "\\r");
        }

        activeJson = json;
        if (isAttached()) applyJson();
    }

    private void applyJson() {
        String script = "const editor = document.getElementById('editor_" + uuid + "')._jsoneditor;";
        script += "editor.set(JSON.parse(\"" + activeJson + "\"));";
        getElement().executeJs(script);
    }

}
