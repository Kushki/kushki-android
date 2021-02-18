package com.kushkipagos.android

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException


internal class KushkiClient(private val environment: Environment, private val publicMerchantId: String , private val regional: Boolean) {

    constructor(environment: Environment, publicMerchantId: String) :
            this(environment, publicMerchantId, false)

    private val kushkiJsonBuilder: KushkiJsonBuilder = KushkiJsonBuilder()

    @Throws(KushkiException::class)
    fun getScienceSession(card: Card):SiftScienceObject{
        var bin:String = card.toJsonObject().getString("number").toString().substring(0, 4)
        var lastDigits:String = card.toJsonObject().getString("number").toString().substring(card.toJsonObject().length() - 4, card.toJsonObject().length())
        return createSiftScienceSession(bin, lastDigits, publicMerchantId)
    }

    @Throws(KushkiException::class)
    fun createSiftScienceSession(bin: String, lastDigits: String, merchantId: String):SiftScienceObject{
        var userId:String =""
        var sessionId:String=""
        var uuid: UUID = UUID.randomUUID()
        if(merchantId==null|| merchantId ==""){
            userId = ""
            sessionId =""

        }else {
            userId = merchantId+bin+lastDigits
            sessionId =uuid.toString()
        }

        return SiftScienceObject(kushkiJsonBuilder.buildJson(userId, sessionId))
    }

    @Throws(KushkiException::class)
    fun post(endpoint: String, requestBody: String): Transaction {
        System.out.println("request--Body")
        System.out.println(requestBody)
        try {
            val connection = prepareConnection(endpoint, requestBody)
            return Transaction(parseResponse(connection))
        } catch (e: Exception) {
            when(e) {
                is BadPaddingException, is IllegalBlockSizeException, is NoSuchAlgorithmException,
                is NoSuchPaddingException, is InvalidKeyException, is InvalidKeySpecException,
                is IOException -> {
                    throw KushkiException(e)
                }
                else -> throw e
            }
        }
    }

    @Throws(KushkiException::class)
    fun post_secure(endpoint: String, requestBody: String): SecureValidation {
        System.out.println("request--Body")
        System.out.println(requestBody)
        try {
            val connection = prepareConnection(endpoint, requestBody)
            return SecureValidation(parseResponse(connection))
        } catch (e: Exception) {
            when(e) {
                is BadPaddingException, is IllegalBlockSizeException, is NoSuchAlgorithmException,
                is NoSuchPaddingException, is InvalidKeyException, is InvalidKeySpecException,
                is IOException -> {
                    throw KushkiException(e)
                }
                else -> throw e
            }
        }
    }


    @Throws(KushkiException::class)
    fun get (endpoint: String):BankList{
        try {
            val connection = prepareGetConnection(endpoint)
            return BankList(parseResponse(connection))
        } catch (e: Exception) {
            when(e) {
                is BadPaddingException, is IllegalBlockSizeException, is NoSuchAlgorithmException,
                is NoSuchPaddingException, is InvalidKeyException, is InvalidKeySpecException,
                is IOException -> {
                    throw KushkiException(e)
                }
                else -> throw e
            }
        }

    }

    @Throws(KushkiException::class)
    fun get_bin (endpoint: String):BinInfo{
        try {
            val connection = prepareGetConnection(endpoint)
            return BinInfo(parseResponse(connection))
        } catch (e: Exception) {
            when(e) {
                is BadPaddingException, is IllegalBlockSizeException, is NoSuchAlgorithmException,
                is NoSuchPaddingException, is InvalidKeyException, is InvalidKeySpecException,
                is IOException -> {
                    throw KushkiException(e)
                }
                else -> throw e
            }
        }

    }


    @Throws(IOException::class)
    private fun prepareConnection(endpoint: String, requestBody: String): HttpURLConnection {
        var urlDestination:String = environment.url

        if(regional) {

            when (environment)
            {
                KushkiEnvironment.PRODUCTION ->  urlDestination = KushkiEnvironment.PRODUCTION_REGIONAL.url
                KushkiEnvironment.TESTING ->   urlDestination = KushkiEnvironment.UAT_REGIONAL.url
            }
        }

        val url = URL(urlDestination + endpoint)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("public-merchant-id", publicMerchantId)
        connection.readTimeout = 25000
        connection.connectTimeout = 30000
        connection.doOutput = true
        val dataOutputStream = DataOutputStream(connection.outputStream)
        dataOutputStream.writeBytes(requestBody)
        dataOutputStream.flush()
        dataOutputStream.close()
        return connection
    }

    @Throws(IOException::class)
    private fun prepareGetConnection(endpoint: String):HttpURLConnection{
        var urlDestination:String = environment.url

        if(regional) {

            when (environment)
            {
                KushkiEnvironment.PRODUCTION ->  urlDestination = KushkiEnvironment.PRODUCTION_REGIONAL.url
                KushkiEnvironment.TESTING ->   urlDestination = KushkiEnvironment.UAT_REGIONAL.url
            }
        }

        val url = URL(urlDestination + endpoint)

        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("Public-Merchant-Id", publicMerchantId)
        connection.readTimeout = 25000
        connection.connectTimeout = 30000
        return connection
    }

    @Throws(IOException::class)
    private fun parseResponse(connection: HttpURLConnection): String {
        val responseInputStream = getResponseInputStream(connection)
        val reader = BufferedReader(InputStreamReader(responseInputStream, "UTF-8"))
        val stringBuilder = StringBuilder()
        reader.forEachLine {
            stringBuilder.append(it)
        }
        System.out.println(stringBuilder.toString())
        return stringBuilder.toString()
    }

    @Throws(IOException::class)
    private fun getResponseInputStream(connection: HttpURLConnection): InputStream {
        if (connection.responseCode >= HttpURLConnection.HTTP_BAD_REQUEST) {
            return connection.errorStream
        } else {
            return connection.inputStream
        }
    }

    @Throws(IOException::class)
    fun get_merchant_settings(endpoint: String):MerchantSettings{
        try {
            val connection = prepareGetConnection(endpoint)
            return MerchantSettings(parseResponse(connection))
        }catch (e: Exception) {
            when(e){
                is BadPaddingException, is IllegalBlockSizeException, is NoSuchAlgorithmException,
                is NoSuchPaddingException, is InvalidKeyException, is InvalidKeySpecException,
                is IOException -> {
                    throw KushkiException(e)
                }
                else -> throw e
            }
        }
    }
}