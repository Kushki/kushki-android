package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.AskQuestionnaire;
import com.kushkipagos.android.SecureValidation;

abstract class AbstractRequestSecureValidationInfoAsyncTask extends AsyncTask<AskQuestionnaire, Void, SecureValidation> {

    protected final Kushki kushki;
    private final Context context;

    AbstractRequestSecureValidationInfoAsyncTask(Context context) {
        this.context = context;
        kushki = new Kushki("20000000107415376000", "COP", KushkiEnvironment.TESTING);
    }

    protected SecureValidation doInBackground(AskQuestionnaire... args) {
        try {
            return requestAskQuestionnaire(args[0]);
        } catch (KushkiException kushkiException) {
            throw new RuntimeException(kushkiException);
        }
    }

    protected void onPostExecute(SecureValidation secureValidation) {
        if (secureValidation.isSuccessful()) {
            showToast("QuestionnaireCode " + secureValidation.getQuestionnaireCode());
            System.out.println(secureValidation.getQuestions());
        } else {
            showToast("ERROR: " + secureValidation.getCode() + " " + secureValidation.getMessage());
        }
    }

    protected abstract SecureValidation requestAskQuestionnaire(AskQuestionnaire askQuestionnaire) throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}