package com.kushkipagos.android

import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException

internal class KushkiClient(private val environment: Environment, private val publicMerchantId: String) {

    @Throws(KushkiException::class)
    fun post(endpoint: String, requestBody: String): Transaction {
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

    @Throws(IOException::class)
    private fun prepareConnection(endpoint: String, requestBody: String): HttpURLConnection {
        val url = URL(environment.url + endpoint)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.setRequestProperty("public-merchant-id", publicMerchantId)
        connection.readTimeout = 10000
        connection.connectTimeout = 15000
        connection.doOutput = true
        val dataOutputStream = DataOutputStream(connection.outputStream)
        dataOutputStream.writeBytes(requestBody)
        dataOutputStream.flush()
        dataOutputStream.close()
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
}