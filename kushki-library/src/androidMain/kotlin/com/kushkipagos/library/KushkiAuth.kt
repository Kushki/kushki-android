package com.kushkipagos.library

import android.content.Context
import android.util.Log
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthChannelEventName
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttribute
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSessionResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.AmplifyConfiguration
import com.amplifyframework.core.InitializationStatus
import com.amplifyframework.hub.HubChannel
import com.amplifyframework.hub.HubEvent
import org.json.JSONObject
import java.net.URL
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.atomic.AtomicReference

class KushkiAuth(context: Context?=null) {
    private val jwtToken = AtomicReference("")
    private var context: Context? = context
    private var email: String? = null
    private var user_name: String? = null
    private var conSuccessFunction: ((String) -> Unit)? = null

    init {
        initAmpliFy()
        listenToAuthEvents()
    }

    @Throws(AmplifyException::class)
    fun requestKpayInit(email: String?, name: String?, saveTokenFunction: (token: String) -> Unit, onFail: (error: String) -> Unit) {
        this.email = email
        this.user_name = name

        this.conSuccessFunction = saveTokenFunction;
        checkAuthSession(saveTokenFunction, onFail)
    }

    fun confirmSignUp(codeFromEmail: String?) {
        Amplify.Auth.confirmSignUp(
            email!!,
            codeFromEmail!!,
            { result: AuthSignUpResult ->
                Log.i(
                    "AuthQuickstart",
                    if (result.isSignUpComplete) "Confirm signUp succeeded" else "Confirm sign up not complete"
                )
            }
        ) { error: AuthException -> Log.e("AuthQuickstart", error.toString()) }
    }

    fun confirmSignIn(codeFromEmail: String, onSuccess: (token: String) -> Unit, onFail: (error: String) -> Unit) {
        Amplify.Auth.confirmSignIn(
            codeFromEmail,
            { result: AuthSignInResult ->
                Log.i(
                    "AuthQuickstart",
                    if (result.isSignInComplete) "Confirm signUp succeeded" else "Confirm sign in not complete"
                )
                Amplify.Auth.fetchAuthSession(
                    {result ->
                        val cognitoAuthSession = result as AWSCognitoAuthSession
                        when (cognitoAuthSession.identityId.type) {
                            AuthSessionResult.Type.SUCCESS -> {
                                val userPoolTokens = cognitoAuthSession.userPoolTokens.value
                                if (userPoolTokens != null) {
                                    onSuccess(userPoolTokens.accessToken)
                                };
                                jwtToken.lazySet(userPoolTokens!!.accessToken)
                            }
                            AuthSessionResult.Type.FAILURE -> {
                                onFail("Fail to fetch user data");

                            }

                        }
                        Log.i("", "")
                    },
                    {error ->
                        onFail(error.toString());

                    })
            }
        ) { error: AuthException -> Log.e("AuthQuickstart", error.toString()) }
    }

    private fun listenToAuthEvents() {
        Amplify.Hub.subscribe(
            HubChannel.AUTH
        ) { hubEvent: HubEvent<*> ->
            if (hubEvent.name == InitializationStatus.SUCCEEDED.toString()) {
                Log.i("AuthQuickstart", "Auth successfully initialized")
            } else if (hubEvent.name == InitializationStatus.FAILED.toString()) {
                Log.i("AuthQuickstart", "Auth failed to succeed")
            } else {
                when (AuthChannelEventName.valueOf(hubEvent.name)) {
                    AuthChannelEventName.SIGNED_IN -> {

                        Log.i("AuthQuickstart", "Auth just became signed in.")
                    }

                    AuthChannelEventName.SIGNED_OUT -> Log.i(
                        "AuthQuickstart",
                        "Auth just became signed out."
                    )
                    AuthChannelEventName.SESSION_EXPIRED -> Log.i(
                        "AuthQuickstart",
                        "Auth session just expired."
                    )
                    else -> Log.w(
                        "AuthQuickstart",
                        "Unhandled Auth Event: " + AuthChannelEventName.valueOf(hubEvent.name)
                    )
                }
            }
        }
    }

    @Throws(AmplifyException::class)
    fun initAmpliFy() {
        val configFromKushki = URL("https://user-personal-wallet-amplify-config.s3.amazonaws.com/amplifyconfiguration.json").readText()
        System.out.println(configFromKushki.toString())

        val configurationJson = JSONObject(configFromKushki)

        val configuration = AmplifyConfiguration.fromJson(configurationJson)
        Amplify.addPlugin(AWSCognitoAuthPlugin())
        Amplify.configure(configuration, context!!)
    }

    fun checkAuthSession(saveTokenFunction: (token: String) -> Unit, onFail: (error: String) -> Unit) {
        Amplify.Auth.fetchAuthSession(
            { result ->
                Log.i("AmplifyQuickstart", result.toString())
                val cognitoAuthSession = result as AWSCognitoAuthSession
                when (cognitoAuthSession.identityId.type) {
                    AuthSessionResult.Type.SUCCESS -> {
                        Log.i(
                            "AuthQuickStart",
                            "IdentityId: " + cognitoAuthSession.identityId.value
                        )
                        val userPoolTokens = cognitoAuthSession.userPoolTokens.value
                        if (userPoolTokens != null) {
                            saveTokenFunction(userPoolTokens.accessToken)
                        };
                        jwtToken.lazySet(userPoolTokens!!.accessToken)
                    }
                    AuthSessionResult.Type.FAILURE -> {
                        // paswordless
                        Log.i(
                            "AuthQuickStart",
                            "IdentityId not present because: " + cognitoAuthSession.identityId.error.toString()
                        )
                        singUpANewUser(saveTokenFunction, onFail)
                    }
                }
            }
        ) { error ->
            Log.e("AmplifyQuickstart", error.toString())
            onFail(error.toString())
        }
    }

    fun singUpANewUser(saveTokenFunction: (token: String) -> Unit, onFail: (error: String) -> Unit) {
        val array = ByteArray(30)
        Random().nextBytes(array)
        val userAttributes: LinkedList<AuthUserAttribute> = LinkedList()
        val givenNameAtribute: AuthUserAttribute =
            AuthUserAttribute(AuthUserAttributeKey.givenName(), this.user_name)
        val preferedNameAtribute: AuthUserAttribute =
            AuthUserAttribute(AuthUserAttributeKey.email(), this.email)

        userAttributes.add(givenNameAtribute)
        userAttributes.add(preferedNameAtribute)

        val randomPassword = String(array, Charset.forName("UTF-8"))
        val options = AuthSignUpOptions.builder().userAttributes(userAttributes).build()

        Amplify.Auth.signUp(
            email!!,
            randomPassword,
            options,
            { result: AuthSignUpResult ->
                Log.i("AuthQuickStart", "Result: $result")
                singInAnUser(saveTokenFunction, onFail)

            },
            { error: AuthException? -> singInAnUser(saveTokenFunction, onFail) }
        )
    }



    fun singInAnUser(onSuccess: (token: String) -> Unit, onFail: (error: String) -> Unit ) {
        Amplify.Auth.signIn(
            email,
            null,
            { result: AuthSignInResult ->
                if (result.isSignInComplete) {
                    Amplify.Auth.fetchAuthSession({
                            result ->
                        val cognitoAuthSession = result as AWSCognitoAuthSession
                        when (cognitoAuthSession.identityId.type) {
                            AuthSessionResult.Type.SUCCESS -> {
                                val userPoolTokens = cognitoAuthSession.userPoolTokens.value
                                if (userPoolTokens != null) {
                                    onSuccess(userPoolTokens.accessToken)
                                };
                                jwtToken.lazySet(userPoolTokens!!.accessToken)
                            }
                            AuthSessionResult.Type.FAILURE -> {
                                onFail("Error while signIn of new user")
                            }

                        }

                    }, {error -> {
                        onFail(error.toString())

                    }})
                }
                Log.i(
                    "AuthQuickstart",
                    if (result.isSignInComplete) "Sign in succeeded" else "Sign in not complete"
                )
            }
        ) { error: AuthException -> Log.e("AuthQuickstart", error.toString()) }
    }


    fun getUserTokenReference(): AtomicReference<String> {
        return this.jwtToken;
    }
}