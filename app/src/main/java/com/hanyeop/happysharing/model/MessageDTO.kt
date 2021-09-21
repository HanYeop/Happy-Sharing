package com.hanyeop.happysharing.model

data class MessageDTO(
    var fromUid : String? = null,
    var toUid : String? = null,
    var content : String? = null,
    var timestamp : Long? = null,
)
