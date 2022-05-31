package com.example.instabugtask.utils

import android.os.AsyncTask
import android.util.Log
import com.example.instabugtask.model.HeaderRequest
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection


class HTTPRequest(
    private val requestURL: String,
    private val requestType: String,
    private val postDataParams: ArrayList<HeaderRequest>? = null,
    asyncResponse: HTTPCallback?
) :
    AsyncTask<String?, Void?, String>() {

    var delegate: HTTPCallback? = null //Call back interface
    var res_code = 0
    var headers = ""
    var queryBody = ""
    var error=""
    override fun doInBackground(vararg params: String?): String {
        return performCall(requestURL, requestType, postDataParams)
    }

    override fun onPostExecute(result: String) {
      //  super.onPostExecute(result);
        if (res_code == 200) {
            delegate!!.processFinish(result, res_code, queryBody, headers)
        } else {
            delegate!!.processFailed(result, res_code, queryBody, headers,error)
        }
    }

    fun performCall(
        requestURL: String,
        requestType: String,
        postDataParams: ArrayList<HeaderRequest>?
    ): String {
        Log.e("HTTP Request URL", requestURL!!)
        val url: URL
        var response = ""
        try {
            val conn: HttpURLConnection
            if (requestType == "GET") {
                url = URL(requestURL+"?"+ postDataParams?.let { getDataString(it) })
               conn  = url.openConnection() as HttpURLConnection
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
            res_code = responseCode
            Log.e("Headers", conn.headerFields.toString())
            headers = conn.headerFields.toString()
            //check response code
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                var line: String
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                while (br.readLine().also { line = it } != null) {
                    response += line
                }
            } else if (responseCode == 500) {
                var line: String
                val br = BufferedReader(InputStreamReader(conn.inputStream))
                while (br.readLine().also { line = it } != null) {
                    response += line
                }
            }else{
                var line: String
                val br = BufferedReader(InputStreamReader(conn.errorStream))
                while (br.readLine().also { line = it } != null) {
                    response += line
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            error+=e.toString()
        }

        return response
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
        queryBody = result.toString()
        return result.toString()
    }

    init {
        delegate = asyncResponse //Assigning call back interfacethrough constructor
    }
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


