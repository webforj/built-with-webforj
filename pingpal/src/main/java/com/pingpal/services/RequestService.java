package com.pingpal.services;

import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.Instant;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.pingpal.helpers.Env;
import com.pingpal.helpers.RequestHandler;
import com.pingpal.models.RequestModel;
import com.webforj.component.optiondialog.OptionDialog;

public class RequestService {
    
    private String token;
    private Instant tokenExpiry;

    public List<RequestModel> get() {
        try {
            final String BASE_URL = Env.get("PINGPAL_URL");
            
            if (BASE_URL == null || BASE_URL.isEmpty()) {
                OptionDialog.showMessageDialog("PINGPAL_URL environment variable is not set. Please check your .env file.");
                return null;
            }

            RequestHandler request = new RequestHandler()
                .setMethod("GET")
                .setEndpoint(BASE_URL + "/requests")
                .setConsoleLogging(false);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + getToken());
            request.setHeaders(headers);

            // App.console().log("Endpoint: " + BASE_URL + "/requests | Token: " + getToken(), true);
            HttpResponse<String> response = request.send();
            // App.console().log("Response: " + response.statusCode() + " " + response.body(), true);

            Type listType = new TypeToken<List<RequestModel>>() {}.getType();
            return new Gson().fromJson(response.body(), listType);
        } catch (Exception e) {
            String errorMessage = "Failed to fetch requests: " + e.getMessage();
            if (e.getMessage() != null && e.getMessage().contains("Connection refused")) {
                errorMessage = "Cannot connect to API at " + Env.get("PINGPAL_URL") + ". Please ensure the API server is running.";
            }
            OptionDialog.showMessageDialog(errorMessage);
        }

        return null;
    }

    public RequestModel add(RequestModel model) {
        try {
            final String BASE_URL = Env.get("PINGPAL_URL");

            RequestHandler request = new RequestHandler()
                .setMethod("POST")
                .setEndpoint(BASE_URL + "/requests")
                .setConsoleLogging(false);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + getToken());
            request.setHeaders(headers);

            Gson gson = new GsonBuilder().serializeNulls().create();
            String body = gson.toJson(model);
            request.setBody(body);

            HttpResponse<String> response = request.send();
            return new Gson().fromJson(response.body(), RequestModel.class);
        } catch (Exception e) {
            OptionDialog.showMessageDialog(e.toString());
        }

        return null;
    }
    
    public RequestModel update(RequestModel model) {
        try {
            final String BASE_URL = Env.get("PINGPAL_URL");

            RequestHandler request = new RequestHandler()
                .setMethod("PUT")
                .setEndpoint(BASE_URL + "/requests/" + model.getId())
                .setConsoleLogging(false);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + getToken());
            request.setHeaders(headers);

            Gson gson = new GsonBuilder().serializeNulls().create();
            String body = gson.toJson(model);
            request.setBody(body);

            HttpResponse<String> response = request.send();
            return new Gson().fromJson(response.body(), RequestModel.class);
        } catch (Exception e) {
            OptionDialog.showMessageDialog(e.toString());
        }

        return null;
    }

    public RequestModel delete(String id) {
        try {
            final String BASE_URL = Env.get("PINGPAL_URL");

            RequestHandler request = new RequestHandler()
                .setMethod("DELETE")
                .setEndpoint(BASE_URL + "/requests/" + id)
                .setConsoleLogging(false);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + getToken());
            request.setHeaders(headers);

            request.send();
        } catch (Exception e) {
            OptionDialog.showMessageDialog(e.toString());
        }

        return null;
    }

    public RequestModel getById(String id) {
        try {
            final String BASE_URL = Env.get("PINGPAL_URL");

            RequestHandler request = new RequestHandler()
                .setMethod("GET")
                .setEndpoint(BASE_URL + "/requests/" + id)
                .setConsoleLogging(false);

            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put("Accept", "application/json");
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + getToken());
            request.setHeaders(headers);

            HttpResponse<String> response = request.send();
            RequestModel model = new Gson().fromJson(response.body(), RequestModel.class);
            
            return model;
        } catch (Exception e) {
            OptionDialog.showMessageDialog(e.toString());
        }

        return null;
    }

    private String getToken() throws Exception {
        if (token != null && tokenExpiry != null && Instant.now().isBefore(tokenExpiry.minusSeconds(30))) {
            return token;
        }

        final String BASE_URL = Env.get("PINGPAL_URL");
        final String username = Env.get("PINGPAL_USERNAME");
        final String password = Env.get("PINGPAL_PASSWORD");
        
        Map<String, String> bodyMap = new HashMap<>();
        bodyMap.put("username", username);
        bodyMap.put("password", password);

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(bodyMap);

        RequestHandler request = new RequestHandler()
            .setMethod("POST")
            .setEndpoint(BASE_URL + "/auth/login")
            .setConsoleLogging(false)
            .setBody(jsonBody);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json");
        request.setHeaders(headers);

        // App.console().log("Endpoint: " + BASE_URL + "/auth/login", true);
        HttpResponse<String> response = request.send();
        // App.console().log("Response: " + response.statusCode() + " " + response.body(), true);

        if (response.statusCode() != 200) throw new RuntimeException("Login failed: " + response.body());

        Map<String, Object> result = gson.fromJson(response.body(), Map.class);
        String newToken = (String) result.get("token");

        if (newToken == null) throw new RuntimeException("No token in login response");

        this.token = newToken;
        this.tokenExpiry = extractExpiryFromJwt(newToken);
        
        return this.token;
    }

    private Instant extractExpiryFromJwt(String jwt) {
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length != 3) throw new IllegalArgumentException("Invalid JWT");

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<String, Object> payload = new Gson().fromJson(payloadJson, Map.class);
            Double exp = (Double) payload.get("exp");

            return Instant.ofEpochSecond(exp.longValue());
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JWT expiry", e);
        }
    }

}
