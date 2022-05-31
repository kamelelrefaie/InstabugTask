package com.example.instabugtask.data.model

data class ResponseData(
    var headers: String,
    var queryBody: String,
    var responseCode: String,
    var output: String,
    var error: String
)
