package com.kushkipagos.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textField = (TextView) findViewById(R.id.txtEncryptedMessage);
        ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask(textField);
        exampleAsyncTask.execute("https://ping.aurusinc.com/kushki/api/v1/tokens");
    }
}
