package com.example.instabugtask.data.local

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.instabugtask.data.model.ApiEntity
import java.lang.Exception

class DBHelper(
    context: Context?,
    name: String = "api.db",
    factory: SQLiteDatabase.CursorFactory?,
    version: Int = 1
) : SQLiteOpenHelper(context, name, factory, version) {
    private val reqType = "requestType"
    override fun onCreate(DB: SQLiteDatabase?) {
        DB?.execSQL("CREATE TABLE ApiTable(headers TEXT,query_body TEXT,response_code TEXT,output TEXT,error TEXT,$reqType TEXT,request_url TEXT,id INTEGER  PRIMARY KEY AUTOINCREMENT)")
    }

    override fun onUpgrade(DB: SQLiteDatabase?, p1: Int, p2: Int) {
        DB?.execSQL("DROP TABLE IF EXISTS ApiTable")
    }

    fun insertApiTableData(
        headers: String,
        query_body: String,
        response_code: String,
        output: String,
        error: String,
        requestType: String,
        requestURL: String
    ): Boolean {
        val sqlDB = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("headers", headers)
        contentValues.put("query_body", query_body)
        contentValues.put("response_code", response_code)
        contentValues.put("output", output)
        contentValues.put("error", error)
        contentValues.put(reqType, requestType)
        contentValues.put("request_url", requestURL)

        val result = sqlDB.insert("ApiTable", null, contentValues)
        return result != -1L

    }


    @SuppressLint("Range")
    fun getRequests(_requestType: String): ArrayList<ApiEntity> {
        val requestList = ArrayList<ApiEntity>()
        //WHERE requestType=$requestType
        val selectQuery = """SELECT * FROM ApiTable WHERE $reqType like '%$_requestType%' """
        val db = this.readableDatabase
        val cursor: Cursor?
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            return ArrayList()
        }

        if (cursor.moveToFirst()) {
            do {
                val apiEntity = ApiEntity("", "", "", "", "", "", "", 0)
                apiEntity.headers = cursor.getString(cursor.getColumnIndex("headers"))
                apiEntity.queryBody = cursor.getString(cursor.getColumnIndex("query_body"))
                apiEntity.responseCode = cursor.getString(cursor.getColumnIndex("response_code"))
                apiEntity.output = cursor.getString(cursor.getColumnIndex("output"))
                apiEntity.error = cursor.getString(cursor.getColumnIndex("error"))
                apiEntity.requestType = cursor.getString(cursor.getColumnIndex("requestType"))
                apiEntity.requestURL = cursor.getString(cursor.getColumnIndex("request_url"))
                apiEntity.id = cursor.getInt(cursor.getColumnIndex("id"))
                requestList.add(apiEntity)
            } while (cursor.moveToNext())
        }

        return requestList
    }

}