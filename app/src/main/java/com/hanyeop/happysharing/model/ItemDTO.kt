package com.hanyeop.happysharing.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemDTO(
    var uId : String? = null,
    var imageUri : String? = null,
    var title : String? = null,
    var content : String? = null,
    var timestamp : Long? = null,
    var category : String? = null,
    var area : String? = null,
    var completed : Boolean = false
)  : Parcelable
