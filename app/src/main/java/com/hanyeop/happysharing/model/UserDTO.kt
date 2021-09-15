package com.hanyeop.happysharing.model

data class UserDTO(
        var uId : String? = null,
        var userId : String? = null,
        var imageUri : String? = null,
        var score : Int = 0,
        var sharing : Int = 0
)