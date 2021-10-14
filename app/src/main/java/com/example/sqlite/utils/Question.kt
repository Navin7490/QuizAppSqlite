package com.example.sqlite.utils

data class Question(
    var question:String? = null,
    var option1:String? = null,
    var option2:String? = null,
    var option3:String? = null,
    var answerNr:Int?=0
)