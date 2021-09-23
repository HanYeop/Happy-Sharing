package com.hanyeop.happysharing.model

data class QuizDTO(
    var content: String? = null,
    var correct: Boolean = true,
    var explain: String? = null,
    var quizNumber: Int? = null
)
