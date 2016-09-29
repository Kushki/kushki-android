package com.kushkipagos.android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class KushkiOld {

    public String requestToken(String endpoint) throws IOException {
        HttpURLConnection connection = prepareConnection(endpoint);
        connection.connect();
        int responseCode = connection.getResponseCode();
        String responseBody = parseResponseBody(getResponseInputStream(connection));
        return responseCode + " " + responseBody;
    }

    private HttpURLConnection prepareConnection(String endpoint) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("{\"test\": 1}");
        dataOutputStream.flush();
        dataOutputStream.close();
        return connection;
    }

    private InputStream getResponseInputStream(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
            return connection.getErrorStream();
        } else {
            return connection.getInputStream();
        }
    }

    private String parseResponseBody(InputStream inputStream) throws IOException {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        BufferedReader reader = new BufferedReader(inputStreamReader);
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }
}