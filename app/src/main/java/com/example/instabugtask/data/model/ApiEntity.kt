package com.example.instabugtask.data.model

data class ApiEntity(

    var headers: String,
    var queryBody: String,
    var responseCode: String,
    var output: String,
    var error: String,
    var requestType:String,
    var requestURL:String,
    var id:Int
)
