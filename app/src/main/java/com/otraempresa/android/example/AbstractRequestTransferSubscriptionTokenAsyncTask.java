package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.Transaction;
import com.kushkipagos.android.TransferSubscriptions;

abstract class AbstractRequestTransferSubscriptionTokenAsyncTask extends AsyncTask<TransferSubscriptions, Void, Transaction> {

    protected final Kushki kushki;
    private final Context context;

    AbstractRequestTransferSubscriptionTokenAsyncTask(Context context) {
        this.context = context;
        kushki = new Kushki("20000000107415376000", "COP", KushkiEnvironment.TESTING);
    }

    @Override
    protected Transaction doInBackground(TransferSubscriptions... args) {
        try {
            return requestTransferToken(args[0]);
        } catch (KushkiException kushkiException) {
            throw new RuntimeException(kushkiException);
        }
    }

    @Override
    protected void onPostExecute(Transaction transaction) {
        if (transaction.isSuccessful()) {
            showToast(transaction.getToken());
        } else {
            showToast("ERROR: " + transaction.getCode() + " " + transaction.getMessage());
        }
    }

    protected abstract Transaction requestTransferToken(TransferSubscriptions card) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}