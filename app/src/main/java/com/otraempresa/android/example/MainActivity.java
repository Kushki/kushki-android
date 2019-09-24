package com.otraempresa.android.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.kushkipagos.android.Amount;
import com.kushkipagos.android.Card;
import com.kushkipagos.android.Transfer;
import com.kushkipagos.android.TransferSubscriptions;
import com.kushkipagos.android.AskQuestionnaire;

import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText emailText = (EditText) findViewById(R.id.emailText);
        final Spinner userType = (Spinner) findViewById(R.id.user_type);
        final Spinner documentType = (Spinner) findViewById(R.id.document_type);
        final Spinner documentType2 = (Spinner) findViewById(R.id.document_type2);
        final EditText reference = (EditText) findViewById(R.id.reference);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText description = (EditText) findViewById(R.id.description);
        final EditText documentNumber_2 = (EditText) findViewById(R.id.documentNumber_2);
        final EditText firstName = (EditText) findViewById(R.id.firstName);
        final EditText lastName = (EditText) findViewById(R.id.lastName);
        final EditText accountNumber = (EditText) findViewById(R.id.accountNumber);



        Button transactionButton = (Button) findViewById(R.id.transactionButton);
        transactionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RequestTransactionTokenAsyncTask(getApplicationContext()).execute(buildCard());
            }
        });

        Button subscriptionButton = (Button) findViewById(R.id.sendSubscriptionButton);
        subscriptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RequestSubscriptionTokenAsyncTask(getApplicationContext()).execute(buildCard());
            }
        });

        Button cardAsyncButton = (Button) findViewById(R.id.sendCardAsyncButton);
        cardAsyncButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RequestCardAsyncTokenAsyncTask(getApplicationContext()).execute(emailText.getText().toString());
            }
        });
        Button transferButton = (Button) findViewById(R.id.sendTransferTokenButton);
        transferButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new RequestTransferTokenAsyncTask(getApplicationContext()).execute(
                        new Transfer(new Amount(12.2,0.0,1.2),"www.kushki.com",
                                mapUser(userType.getSelectedItem().toString()),documentType.getSelectedItem().toString(),
                                reference.getText().toString(),email.getText().toString(),"CLP",description.getText().toString())
                );
            }
        });
        Button transferSubscriptionButton = (Button)  findViewById(R.id.sendTransferSubscriptionTokenButton);
        transferSubscriptionButton.setOnClickListener(
                new View.OnClickListener() {
            public void onClick(View v) {
                new RequestTransferSubscriptionTokenAsyncTask(getApplicationContext()).execute(
                       new TransferSubscriptions(documentNumber_2.getText().toString(),
                                "1",firstName.getText().toString(),lastName.getText().toString(),accountNumber.getText().toString(),
                                documentType2.getSelectedItem().toString(),"123",12,email.getText().toString(),
                               "CLP"
                       )
                );
            }
        });

        Button SecureValidationInfoButton = (Button)  findViewById(R.id.sendSecureValidationButton);
        SecureValidationInfoButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try{
                            new RequestSecureValidationInfoAsyncTask(getApplicationContext()).execute(
                                    buildRequestSecure()
                            );
                        }
                        catch (InterruptedException | ExecutionException e){
                            System.out.println(e);
                        }
                    }
                }
        );

    }

    private  AskQuestionnaire buildRequestSecure() throws InterruptedException, ExecutionException {
        Spinner documentType2 = (Spinner) findViewById(R.id.document_type2);
        EditText documentNumber_2 = (EditText) findViewById(R.id.documentNumber_2);
        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastName);
        EditText accountNumber = (EditText) findViewById(R.id.accountNumber);
        EditText email = (EditText) findViewById(R.id.email);
        EditText expeditionDocumentDate = (EditText) findViewById(R.id.expeditionDocumentDate);
        EditText cityCode = (EditText) findViewById (R.id.cityCode);
        EditText stateCode = (EditText) findViewById (R.id.stateCode);
        EditText phone = (EditText) findViewById (R.id.phone);

        RequestTransferSubscriptionTokenAsyncTask transferSubscription = new RequestTransferSubscriptionTokenAsyncTask(getApplicationContext());

         transferSubscription.execute(
                new TransferSubscriptions(documentNumber_2.getText().toString(),
                        "1",firstName.getText().toString(),lastName.getText().toString(),accountNumber.getText().toString(),
                        documentType2.getSelectedItem().toString(),"01",12,email.getText().toString(),
                        "CLP"
                )
        );

        String secureService = transferSubscription.get().getSecureService();
        String secureId = transferSubscription.get().getSecureId();

        return new AskQuestionnaire(secureId,secureService,"1",stateCode.getText().toString(),
                phone.getText().toString(),"15/12/1958");

    }

    private Card buildCard() {
        EditText nameText = (EditText) findViewById(R.id.nameText);
        EditText numberText = (EditText) findViewById(R.id.numberText);
        EditText monthText = (EditText) findViewById(R.id.monthText);
        EditText yearText = (EditText) findViewById(R.id.yearText);
        EditText cvvText = (EditText) findViewById(R.id.cvvText);
        return new Card(nameText.getText().toString(), numberText.getText().toString(),
                cvvText.getText().toString(), monthText.getText().toString(), yearText.getText().toString());
    }




    private String mapUser(String usertType){
        if (usertType.equals("Natural") )
            return "0";
        else
            return "1";
    }
}
