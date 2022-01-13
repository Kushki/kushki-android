package com.kushkipagos.kushkidemo.contracts

import android.content.Context
import android.widget.Toast
import com.kushkipagos.exceptions.KushkiException
import com.kushkipagos.kushki.Kushki
import com.kushkipagos.kushki.KushkiEnvironment
import com.kushkipagos.kushki.SecureValidation
import com.kushkipagos.kushkidemo.helpers.CoroutineTask
import com.kushkipagos.models.AskQuestionnaire

abstract class AbstractRequestSecureValidationInfoAsyncTask:
    CoroutineTask<AskQuestionnaire, Void, SecureValidation> {

    protected var _kushki: Kushki
    private var _context: Context

    constructor(context: Context){
        _context = context
        _kushki = Kushki("c308a8af32114b8c97f8ba191149df9b", "COP", KushkiEnvironment.QA)
    }

    @Throws(KushkiException::class)
    abstract fun requestAskQuestionnaire(askQuestionnaire: AskQuestionnaire) : SecureValidation

    override fun doInBackground(vararg params: AskQuestionnaire?): SecureValidation {
        try {
            return requestAskQuestionnaire(params[0]!!);
        } catch (kushkiException: KushkiException) {
            throw RuntimeException(kushkiException);
        }
    }

    override fun onPostExecute(param: SecureValidation?) {
        val secureValidation = param!!
        if (secureValidation.isSuccessful) {
            showToast("QuestionnaireCode: ${secureValidation.questionnaireCode}")
            println(secureValidation.questions)
        } else {
            showToast("ERROR: ${secureValidation.code} - ${secureValidation.message}")
        }
    }

    fun showToast(text: String){
        val toast = Toast.makeText(_context, text, Toast.LENGTH_LONG)
        toast.show()
    }

}