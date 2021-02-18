package com.otraempresa.android.example;

import android.content.Context;

import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.MerchantSettings;

public class RequestMerchantSettingsTask extends AbstractRequestMerchantSettingsTask {
    RequestMerchantSettingsTask(Context context){
        super(context);
    }

    @Override
    protected MerchantSettings requestMerchantSettings() throws KushkiException{
        return kushkiMerchantSettings.requestMerchantSettings();
    }
}

