package com.kushkipagos.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String MESSAGE = "Eum ipsum eum minima non quasi quos ut aut. Praesentium sint nisi illo et est id reprehenderit. Harum ducimus aperiam ut quod numquam. Ut ipsam nulla ratione.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Kushki kushki = new Kushki();
        TextView textField = (TextView) findViewById(R.id.txtEncryptedMessage);
        ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask(textField, kushki);
        exampleAsyncTask.execute("https://ping.aurusinc.com/kushki/api/v1/tokens");
    }

    private void printEncryptedMessage(TextView textField) {
        try {
            AurusEncryption aurusEncryption = new AurusEncryption();
            textField.setText(aurusEncryption.encryptMessageChunk(MESSAGE));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
