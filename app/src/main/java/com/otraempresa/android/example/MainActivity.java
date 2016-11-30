package com.otraempresa.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.kushkipagos.android.Card;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.otraempresa.android.example.R.layout.activity_main);
        Button button = (Button) findViewById(com.otraempresa.android.example.R.id.sendButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(com.otraempresa.android.example.R.id.nameText);
                EditText numberText = (EditText) findViewById(com.otraempresa.android.example.R.id.numberText);
                EditText monthText = (EditText) findViewById(com.otraempresa.android.example.R.id.monthText);
                EditText yearText = (EditText) findViewById(com.otraempresa.android.example.R.id.yearText);
                EditText cvvText = (EditText) findViewById(com.otraempresa.android.example.R.id.cvvText);
                Card card = new Card(nameText.getText().toString(), numberText.getText().toString(),
                        cvvText.getText().toString(), monthText.getText().toString(), yearText.getText().toString());
                ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask(getApplicationContext());
                exampleAsyncTask.execute(card);
            }
        });
    }
}
