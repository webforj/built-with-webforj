package com.pingpal.helpers;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

import com.pingpal.views.Console;
import com.webforj.component.optiondialog.OptionDialog;
import com.webforj.environment.ObjectTable;

import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;

public class RequestHandler {
    
    private HttpClient client = HttpClient.newHttpClient();
    private String method = "GET", endpoint, body;
    private HashMap<String, String> authentication, params, headers;
    private Boolean consoleLogging = true;

    public HttpResponse<String> send() throws Exception {
        Console console = (Console) ObjectTable.get("CONSOLE");

        String fullUrl = endpoint;
        if (method.equalsIgnoreCase("GET") && params != null && !params.isEmpty()) fullUrl += "?" + getParamString(params);

        HttpResponse<String> response = null;

        try {
            Builder requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(new URI(fullUrl));
            
            if (headers != null) {
                for (Map.Entry<String, String> header : headers.entrySet()) {
                    requestBuilder.header(header.getKey(), header.getValue());
                }
            }
            
            if (authentication != null) {
                String type = authentication.get("type");
                switch (type) {
                    case "API_KEY":
                        String key = authentication.get("key");
                        String value = authentication.get("value");
                        requestBuilder.header(key, value);
                        break;
                    case "BASIC":
                        String username = authentication.get("username");
                        String password = authentication.get("password");
                        String credentials = username + ":" + password;
                        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes());
                        requestBuilder.header("Authorization", "Basic " + encoded);
                        break;
                    case "BEARER":
                        String token = authentication.get("token");
                        requestBuilder.header("Authorization", "Bearer " + token);
                        break;
                }
            }

            if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
                requestBuilder.method(method, BodyPublishers.ofString(body));
            } else {
                requestBuilder.method(method, BodyPublishers.noBody());
            }

            requestBuilder.timeout(Duration.ofSeconds(10));
            
            HttpRequest request = requestBuilder.build();

            Instant start = Instant.now();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);
            String prettyTime = DurationFormatter.format(duration);

            String prettyStatus = StatusCodeFormatter.format(response.statusCode());

            byte[] bodyBytes = response.body().getBytes(StandardCharsets.UTF_8);
            int byteLength = bodyBytes.length;
            String prettyBytes = BytesFormatter.format(byteLength);

            if (consoleLogging && console != null) console.print(method + " " + fullUrl + " | " + prettyStatus + " | " + prettyTime + " | " + prettyBytes);
        } catch (Exception e) {
            if (consoleLogging && console != null) console.print(method + " " + fullUrl + " | Error: " + e.toString());
            throw e;
        }

        return response;
    }

    private String getParamString(HashMap<String, String> params) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            joiner.add(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8) + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return joiner.toString();
    }

    public RequestHandler setMethod(String method) {
        this.method = method;
        return this;
    }

    public RequestHandler setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public RequestHandler setAuthenticationData(HashMap<String, String> data) {
        this.authentication = data;
        return this;
    }

    public RequestHandler setParams(HashMap<String, String> params) {
        this.params = params;
        return this;
    }

    public RequestHandler setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public RequestHandler setBody(String body) {
        this.body = body;
        return this;
    }

    public RequestHandler setConsoleLogging(Boolean enabled) {
        this.consoleLogging = enabled;
        return this;
    }

}
