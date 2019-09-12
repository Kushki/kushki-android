package com.otraempresa.android.example;

import android.content.Context;


import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.AskQuestionnaire;
import com.kushkipagos.android.SecureValidation;


class RequestSecureValidationInfoAsyncTask extends AbstractRequestSecureValidationInfoAsyncTask {

     RequestSecureValidationInfoAsyncTask(Context context) {
        super(context);
    }

    @Override
    protected SecureValidation requestAskQuestionnaire(AskQuestionnaire askQuestionnaire) throws KushkiException {

        return kushki.transferSubscriptionSecure(new AskQuestionnaire(askQuestionnaire.toJsonObject().optString("secureServiceId"),
                askQuestionnaire.toJsonObject().optString("secureService"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("cytyCode"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("stateCode"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("phone"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("expeditionDate")
        ));

    }
}