package com.hanyeop.happysharing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDTO(
        var uId : String? = null,
        var userId : String? = null,
        var imageUri : String? = null,
        var score : Int = 0,
        var sharing : Int = 0
) : Parcelable