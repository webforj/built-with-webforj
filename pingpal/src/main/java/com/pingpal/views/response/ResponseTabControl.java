package com.pingpal.views.response;

import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.pingpal.components.TabControl;
import com.pingpal.views.Console;

public class ResponseTabControl extends TabControl {

    private ResponseBody responseBody;
    private ResponseHeaders responseHeaders;
    private ResponseStatusCode responseCode;
    private ResponseDuration responseDuration;
    private ResponseBytes responseBytes;
    private Console console;

    public ResponseTabControl() {
        responseBody = new ResponseBody();
        addTab("Response", responseBody);

        responseHeaders = new ResponseHeaders();
        addTab("Headers", responseHeaders);

        console = new Console();
        addTab("Console", console);

        responseCode = new ResponseStatusCode();
        responseDuration = new ResponseDuration();
        responseBytes = new ResponseBytes();
        addExtraContent(responseCode, responseDuration, responseBytes);
    }

    public void setResponse(HttpResponse<String> response, Duration duration) {
        responseBody.setData(response.body());
        
        responseCode.setData(response.statusCode());
        responseDuration.setData(duration);

        byte[] bodyBytes = response.body().getBytes(StandardCharsets.UTF_8);
        int byteLength = bodyBytes.length;
        responseBytes.setData(byteLength);
        
        responseHeaders.setData(response.headers());
    }

    public void setError(String errorMessage) {
        responseBody.setData(errorMessage);
        responseCode.setError();
        responseDuration.setData(Duration.ofMillis(0));
        responseBytes.setData(0);
    }

    public void clear() {
        responseBody.clear();
        responseCode.clear();
        responseDuration.clear();
        responseBytes.clear();
        responseHeaders.clear();
    }
    
}
