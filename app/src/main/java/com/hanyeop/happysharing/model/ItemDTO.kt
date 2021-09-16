package com.hanyeop.happysharing.model

data class ItemDTO(
    var uId : String? = null,
    var imageUrl : String? = null,
    var title : String? = null,
    var content : String? = null,
    var timestamp : Long? = null,
    var category : String? = null,
    var area : String? = null
)
