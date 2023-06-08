package com.resoluteai.qrscanner.models

data class QRCode(
    val data : String,
    val uid: String,
    val time:Long
){
    constructor():this("","",0)
}