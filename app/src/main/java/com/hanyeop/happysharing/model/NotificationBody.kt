package com.hanyeop.happysharing.model

data class NotificationBody(
    val to: String,
    val data: NotificationData
) {
    data class NotificationData(
        val title: String,
        val userId : String,
        val message: String
    )
}
