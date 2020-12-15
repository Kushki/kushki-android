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

        return kushki.requestSecureValidation(new AskQuestionnaire(askQuestionnaire.toJsonObject().optString("secureServiceId"),
                askQuestionnaire.toJsonObject().optString("secureService"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("cityCode"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("stateCode"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("phone"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("expeditionDate"),
                askQuestionnaire.toJsonObject().optJSONObject("confrontaInfo").optJSONObject("confrontaBiometrics").optString("merchantId")
        ));

    }
}
