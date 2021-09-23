package com.hanyeop.happysharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.QuizDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.repository.FirebaseRepository

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : FirebaseRepository = FirebaseRepository()
    val userDTO = repository.userDTO
    val currentQuiz = repository.currentQuiz

    // 프로필 불러오기
    fun profileLoad(uid : String){
        repository.profileLoad(uid)
    }

    // 프로필 수정하기
    fun profileEdit(userDTO: UserDTO) {
        repository.profileEdit(userDTO)
    }

    // 아이템 업로드하기
    fun uploadItem(itemDTO: ItemDTO){
        repository.uploadItem(itemDTO)
    }

    // 아이템 삭제하기
    fun deleteItem(itemDTO: ItemDTO){
        repository.deleteItem(itemDTO)
    }

    // 메시지 전송하기
    fun uploadChat(messageDTO: MessageDTO){
        repository.uploadChat(messageDTO)
    }

    // 퀴즈 불러오기
    fun quizLoad(num: Int) {
        repository.quizLoad(num)
    }
}