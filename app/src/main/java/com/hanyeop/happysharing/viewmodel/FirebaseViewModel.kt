package com.hanyeop.happysharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.api.RetrofitInstance
import com.hanyeop.happysharing.model.*
import com.hanyeop.happysharing.repository.FirebaseRepository
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Response

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : FirebaseRepository = FirebaseRepository()
    val userDTO = repository.userDTO
    val currentQuiz = repository.currentQuiz
    val myResponse = repository.myResponse

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

    // 푸시 메세지 전송
    fun sendNotification(notification: NotificationBody) {
        viewModelScope.launch {
            repository.sendNotification(notification)
        }
    }
}