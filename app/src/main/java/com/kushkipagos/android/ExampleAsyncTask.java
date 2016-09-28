package com.kushkipagos.android;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;

class ExampleAsyncTask extends AsyncTask<String, Void, String> {

    private Kushki kushki;
    private TextView textView;

    public ExampleAsyncTask(TextView textView, Kushki kushki) {
        this.textView = textView;
        this.kushki = kushki;
    }

    @Override
    protected String doInBackground(String... endpoints) {
        try {
            return kushki.requestToken(endpoints[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return "ERROR!";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        textView.setText(result);
    }
}
