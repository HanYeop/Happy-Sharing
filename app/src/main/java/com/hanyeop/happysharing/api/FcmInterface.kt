package com.hanyeop.happysharing.api

import com.hanyeop.happysharing.model.NotificationBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface FcmInterface {
    @POST("fcm/send")
    suspend fun sendNotification(
        @Body notification: NotificationBody
    ) : Response<ResponseBody>
}