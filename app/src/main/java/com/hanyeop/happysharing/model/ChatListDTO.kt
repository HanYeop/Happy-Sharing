package com.hanyeop.happysharing.model

data class ChatListDTO(
    var fromUid : String? = null,
    var toUid : String? = null,
    var content : String? = null,
    var timestamp : Long? = null,
    var chatPerson : ArrayList<String>? = null
)
