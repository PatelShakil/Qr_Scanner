package com.resoluteai.qrscanner.models

data class User(
    val uid : String = "",
    val name : String,
    val email: String,
    val password: String
)