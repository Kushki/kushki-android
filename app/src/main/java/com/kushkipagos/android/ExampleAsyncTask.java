package com.kushkipagos.android;

import android.os.AsyncTask;
import android.widget.TextView;

import java.io.IOException;

class ExampleAsyncTask extends AsyncTask<String, Void, String> {

    private final KushkiOld kushkiOld;
    private final TextView textView;

    ExampleAsyncTask(TextView textView, KushkiOld kushkiOld) {
        this.textView = textView;
        this.kushkiOld = kushkiOld;
    }

    @Override
    protected String doInBackground(String... endpoints) {
        try {
            return kushkiOld.requestToken(endpoints[0]);
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
