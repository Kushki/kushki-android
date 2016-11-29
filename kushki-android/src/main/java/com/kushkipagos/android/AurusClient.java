package com.kushkipagos.android;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

class AurusClient {

    private final Environment environment;
    private final AurusEncryption aurusEncryption;

    AurusClient(Environment environment, AurusEncryption aurusEncryption) {
        this.environment = environment;
        this.aurusEncryption = aurusEncryption;
    }

    Transaction post(String endpoint, String requestBody) throws KushkiException {
        try {
            String encryptedRequestBody = aurusEncryption.encryptMessageChunk(requestBody);
            HttpURLConnection connection = prepareConnection(endpoint, encryptedRequestBody);
            return new Transaction(parseResponse(connection));
        } catch (BadPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                NoSuchPaddingException | InvalidKeyException | InvalidKeySpecException |
                IOException e) {
            throw new KushkiException(e);
        }
    }

    private HttpURLConnection prepareConnection(String endpoint, String requestBody) throws IOException {
        URL url = new URL(environment.getUrl() + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoOutput(true);
        DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
        dataOutputStream.writeBytes("{\"request\": \"" + requestBody + "\"}");
        dataOutputStream.flush();
        dataOutputStream.close();
        return connection;
    }

    private String parseResponse(HttpURLConnection connection) throws IOException {
        InputStream responseInputStream = getResponseInputStream(connection);
        BufferedReader reader = new BufferedReader(new InputStreamReader(responseInputStream, "UTF-8"));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        return stringBuilder.toString();
    }

    private InputStream getResponseInputStream(HttpURLConnection connection) throws IOException {
        if (connection.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
            return connection.getErrorStream();
        } else {
            return connection.getInputStream();
        }
    }
}
