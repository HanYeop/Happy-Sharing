package com.hanyeop.happysharing.util

import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object{
        fun timeConverter(time: Long) :String{
            val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd")
            return simpleDataFormat.format(time)
        }
    }
}