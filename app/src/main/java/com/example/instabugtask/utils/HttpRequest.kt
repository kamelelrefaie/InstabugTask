package com.example.instabugtask.utils

import android.util.Log
import com.example.instabugtask.data.model.HeaderRequest
import com.example.instabugtask.data.model.ResponseData
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder


var res_code = 0
var headers = ""
var queryBody = ""
var error = ""

val responseData = ResponseData("", "", "", "", "")

fun performCall(
    requestURL: String,
    requestType: String,
    postDataParams: ArrayList<HeaderRequest>?,
): ResponseData {
    Log.e("HTTP Request URL", requestURL!!)
    val url: URL
    var response = ""
    try {
        val conn: HttpURLConnection
        if (requestType == "GET") {
            url = URL(requestURL + "?" + postDataParams?.let { getDataString(it) })
            conn = url.openConnection() as HttpURLConnection
        } else {
            url = URL(requestURL)
            conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = requestType
            conn.readTimeout = 30000
            conn.connectTimeout = 60000
            conn.doOutput = true
            val os = conn.outputStream
            val writer = BufferedWriter(
                OutputStreamWriter(os, "UTF-8")
            )

            if (postDataParams != null && requestType == "POST") {
                writer.write(getDataString(postDataParams))
                writer.flush()
                writer.close()
                os.close()
            }

        }

        val responseCode = conn.responseCode
        Log.e("HTTP Response Code", Integer.toString(responseCode))
        responseData.responseCode = responseCode.toString()

        Log.e("Headers", conn.headerFields.toString())
        responseData.headers = conn.headerFields.toString()
        //check response code
        if (responseCode == 500) {
            responseData.error = "INTERNAL SERVER ERROR"

        } else if (responseCode == 404) {
            responseData.error = "NOT FOUND"
        } else if (responseCode in 400..499 && responseCode != 404) {
            responseData.error = "Client Error"

        } else if (responseCode > 500) {
            responseData.error = "server Error"
        }
        var line: String
        val br = BufferedReader(InputStreamReader(conn.inputStream))
        while (br.readLine().also { line = it } != null) {
            response += line
        }

    } catch (e: Exception) {
        e.printStackTrace()
    }

    responseData.output = response
    return responseData
}

private fun getDataString(params: ArrayList<HeaderRequest>): String {
    val result = StringBuilder()
    var first = true
    for (i in params) {
        if (first) first = false else result.append("&")
        result.append(URLEncoder.encode(i.key, "UTF-8"))
        result.append("=")
        Log.e("POST KEY VAL", i.key + "," + i.value)
        result.append(URLEncoder.encode(i.value, "UTF-8"))

    }
    Log.e("Request", result.toString())
    responseData.queryBody = result.toString()
    return result.toString()
}


/*
BufferedReader(InputStreamReader(httpURLConnection.inputStream,"utf-8")).readText()
 */

/*
String this.url = "http://myweb.com/public/resource/data/stream";
this.url += "?begin=1430295300000&end=1430297279988&id=140621
    &time=FIFTEEN_MINUTE";
this.urlObj = new URL(this.url);
 */


