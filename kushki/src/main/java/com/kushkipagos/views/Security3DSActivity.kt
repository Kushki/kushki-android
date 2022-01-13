package com.kushkipagos.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kushkipagos.kushki.R
import com.kushkipagos.models.OTPValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.NullPointerException
import java.lang.StringBuilder

/**
 * Class for implement 3DS mock
 * The parameters that are received are defined below
 *
 * <ul>
 *     <li>merchat - string</li>
 *     <li>cardNumber - string</li>
 *     <li>totalAmount- string</li>
 * </ul>
 *
 * <h3>Merchant name (@merchant)</h3>
 * <p>this parameter must be declared with the name of the merchant</p>
 *
 * <h3>Card number (@cardNumber)</h3>
 * <p>This parameter must be declared with the card number masked, example: **** **** **** 1091</p>
 *
 * <h3>Total amount (@totalAmount)</h3>
 * <p>this parameter must be declared with total amount value and currency, example: USD 200.00</p>
 */
class Security3DSActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.security_3ds_layout)

        try {
            this.supportActionBar!!.hide()
        } catch (e: IOException) {
            println(e.message)
        }

        val btnCancel = findViewById<View>(R.id.btnCancel) as Button
        var btnSubmit = findViewById<View>(R.id.btnSubmit) as Button
        btnSubmit.isEnabled = false

        val txtMerchant = findViewById<View>(R.id.txtMerchant) as TextView
        val txtCardNumber = findViewById<View>(R.id.txtCardNumber) as TextView
        val txtTotalAmount = findViewById<View>(R.id.txtTotalAmount) as TextView
        val inCode = findViewById<View>(R.id.inCode) as EditText


        val bundle = intent.extras
        val merchant = bundle?.getString("merchant").toString()
        val cardNumber = bundle?.getString("cardNumber").toString()
        val totalAmount = bundle?.get("totalAmount").toString()
        val currency = bundle?.getString("currency").toString()

        txtMerchant.text = this.validateData(merchant, "Merchant Kushki Pruebas")
        txtCardNumber.text = this.maskCardNumber(cardNumber,"xxxx-xxxx-xxxx-####")
        txtTotalAmount.text = currency+ " " + this.validateData(totalAmount, "USD 200.00")

        btnCancel.setOnClickListener {
            finish()
        }

        btnSubmit.setOnClickListener {
            finish()
        }

        inCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                btnSubmit.isEnabled = inCode.text.length > 3
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
                // No operation
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                // No operation
            }
        });

    }

    private fun validateData(data: String, default: String): String {
        if (data.equals("null"))
            return default
        else
            return data
    }

    private fun maskCardNumber(cardNumber: String, mask: String): String? {
        var index = 0
        val maskedNumber = StringBuilder()
        for (i in 0 until mask.length) {
            val chararacter = mask[i]
            if (chararacter == '#') {
                maskedNumber.append(cardNumber[index])
                index++
            } else if (chararacter == 'x') {
                maskedNumber.append(chararacter)
                index++
            } else {
                maskedNumber.append(chararacter)
            }
        }
        return maskedNumber.toString()
    }

}