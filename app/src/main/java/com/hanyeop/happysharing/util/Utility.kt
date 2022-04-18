package com.hanyeop.happysharing.util

import java.text.SimpleDateFormat

class Utility {

    companion object{
        fun timeConverter(time: Long) :String{
            val currentTime = System.currentTimeMillis()
            var diffTime = (currentTime - time) / 60000

            return when {
                diffTime < 1 -> {
                    "방금 전"
                }
                diffTime < 60 -> {
                    "${diffTime}분 전"
                }
                diffTime/60 < 24 -> {
                    "${diffTime/60}시간 전"
                }
                else -> {
                    val simpleDataFormat = SimpleDateFormat("yyyy-MM-dd")
                    simpleDataFormat.format(time)
                }
            }
        }

        fun chatTimeConverter(time: Long) :String{
            val simpleDataFormat = SimpleDateFormat("MM-dd HH:mm")
            return simpleDataFormat.format(time)
        }
    }
}