package com.otraempresa.android.example;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.kushkipagos.android.Kushki;
import com.kushkipagos.android.KushkiEnvironment;
import com.kushkipagos.android.KushkiException;
import com.kushkipagos.android.MerchantSettings;

abstract public class AbstractRequestMerchantSettingsTask extends AsyncTask<String, Void, MerchantSettings> {

    protected final Kushki kushkiMerchantSettings;
    private final Context context;

    AbstractRequestMerchantSettingsTask(Context context){
        this.context = context;
        kushkiMerchantSettings = new Kushki("10000002036955013614148494909956", "USD", KushkiEnvironment.TESTING);
    }

    @Override
    protected MerchantSettings doInBackground(String... params){
        try{
            return requestMerchantSettings();
        } catch (KushkiException kushkiException) {
            throw new RuntimeException(kushkiException);
        }
    }

    @Override
    protected void onPostExecute(MerchantSettings merchantSettingsInfo){
        if(merchantSettingsInfo.getMerchant_name().equals("")){
            showToast("Code" + merchantSettingsInfo.getCode() +"\nMessage: "+merchantSettingsInfo.getMessage());
        }else{
            showToast("Merchant name: "+merchantSettingsInfo.getMerchant_name()
                    +"\nCountry: "+merchantSettingsInfo.getCountry()
                    +"\nProcessor name: "+merchantSettingsInfo.getProcessor_name()
                    +"\nprodBaconKey: "+merchantSettingsInfo.getProdBaconKey()
                    +"\nsandboxBaconKey: "+merchantSettingsInfo.getSandboxBaconKey()
                    +"\nprocessors: "+merchantSettingsInfo.getProcessors());
        }

    }

    protected abstract MerchantSettings requestMerchantSettings() throws KushkiException;

    private void showToast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }

}
