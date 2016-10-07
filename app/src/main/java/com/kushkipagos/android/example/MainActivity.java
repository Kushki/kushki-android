package com.kushkipagos.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kushkipagos.android.Card;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.sendButton);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText nameText = (EditText) findViewById(R.id.nameText);
                EditText numberText = (EditText) findViewById(R.id.numberText);
                EditText monthText = (EditText) findViewById(R.id.monthText);
                EditText yearText = (EditText) findViewById(R.id.yearText);
                EditText cvvText = (EditText) findViewById(R.id.cvvText);
                Card card = new Card(nameText.getText().toString(), numberText.getText().toString(),
                        cvvText.getText().toString(), monthText.getText().toString(), yearText.getText().toString());
                ExampleAsyncTask exampleAsyncTask = new ExampleAsyncTask(getApplicationContext(), card);
                exampleAsyncTask.execute();
            }
        });
    }
}
