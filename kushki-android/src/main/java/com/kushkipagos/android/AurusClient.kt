package com.kushkipagos.android

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

internal class AurusClient(private val environment: Environment) {

    @Throws(KushkiException::class)

    private fun prepareConnection(endpoint: String, requestBody: String): HttpURLConnection {
        val url = URL(environment.url + endpoint)
            val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
        connection.readTimeout = 10000
        connection.connectTimeout = 15000
        connection.doOutput = true
        val dataOutputStream = DataOutputStream(connection.outputStream)
        dataOutputStream.writeBytes("{\"request\": \"$requestBody\"}")
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
