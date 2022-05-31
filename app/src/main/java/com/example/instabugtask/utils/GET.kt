package com.example.instabugtask.utils

import android.os.AsyncTask
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


public class HTTPReqTask : AsyncTask<Void?, Void?, Void?>() {
     override fun doInBackground(vararg params: Void?): Void? {
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL("https://reqres.in/api/users?page=2")
            urlConnection = url.openConnection() as HttpURLConnection
            val code: Int = urlConnection.getResponseCode()
            if (code != 200) {
                throw IOException("Invalid response from server: $code")
            }
            val rd = BufferedReader(
                InputStreamReader(
                    urlConnection.getInputStream()
                )
            )
            var line: String?
            while (rd.readLine().also { line = it } != null) {
                line?.let { Log.i("data", it) }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect()
            }
        }
        return null
    }
}