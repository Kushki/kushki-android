package com.kushkipagos.android;

import android.support.annotation.VisibleForTesting;

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

import javax.crypto.NoSuchPaddingException;

public class Kushki {

    private String publicMerchantId;
    private String currency;
    private KushkiEnvironment environment;
    private AurusEncryption aurusEncryption;

    // TODO: Check what to do about throwing all those exceptions
    public Kushki(String publicMerchantId, String currency, KushkiEnvironment environment) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IOException {
        new Kushki(publicMerchantId, currency, environment, new AurusEncryption());
    }

    @VisibleForTesting
    Kushki(String publicMerchantId, String currency, KushkiEnvironment environment, AurusEncryption aurusEncryption) {
        this.publicMerchantId = publicMerchantId;
        this.currency = currency;
        this.environment = environment;
        this.aurusEncryption = aurusEncryption;
    }

    public Transaction requestToken(Card card, double totalAmount) throws IOException {
        HttpURLConnection connection = prepareConnection(environment.getUrl() + "/tokens");
        InputStream responseInputStream = getResponseInputStream(connection);
        return new Transaction(parseResponseBody(responseInputStream));
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
