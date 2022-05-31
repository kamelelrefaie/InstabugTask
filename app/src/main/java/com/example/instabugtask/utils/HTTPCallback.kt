package com.example.instabugtask.utils

public interface HTTPCallback  {
    fun processFinish(output: String,responseCode: Int,queryBody:String,headers:String)
    fun processFailed(output: String,responseCode: Int,queryBody:String,headers:String,error: String)
}