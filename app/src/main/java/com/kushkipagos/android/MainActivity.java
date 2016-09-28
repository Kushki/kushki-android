package com.kushkipagos.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    public static final String MESSAGE = "Eum ipsum eum minima non quasi quos ut aut. Praesentium sint nisi illo et est id reprehenderit. Harum ducimus aperiam ut quod numquam. Ut ipsam nulla ratione.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        printAurusResponse();
    }

    private void printAurusResponse() {
        try {
            URL url = new URL("https://ping.aurusinc.com/kushki/api/v1/tokens");
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

            connection.connect();

            String line;
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), "UTF-8"));
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            int responseCode = connection.getResponseCode();
            String response = stringBuilder.toString();
            TextView textField = (TextView) findViewById(R.id.txtEncryptedMessage);
            textField.setText(responseCode + " " + response);

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private void printEncryptedMessage() {
        TextView textField = (TextView) findViewById(R.id.txtEncryptedMessage);
        try {
            AurusEncryption aurusEncryption = new AurusEncryption();
            textField.setText(aurusEncryption.encryptMessageChunk(MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
