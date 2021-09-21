package com.hanyeop.happysharing.util

import java.text.SimpleDateFormat
import java.util.*

class Utility {

    companion object{
        fun timeConverter(time: Long) :String{
            val currentTime = System.currentTimeMillis()
            var diffTime = (currentTime - time) / 60000

            if(diffTime < 60){
                return "$diffTime 분 전"
            }
            else if(diffTime/60 < 24){
                return "${diffTime/60} 시간 전"
            }

            val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd")
            return simpleDataFormat.format(time)
        }
    }
}