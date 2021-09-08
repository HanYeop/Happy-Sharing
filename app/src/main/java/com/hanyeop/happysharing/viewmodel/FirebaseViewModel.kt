package com.hanyeop.happysharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.repository.FirebaseRepository

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : FirebaseRepository = FirebaseRepository()
    val uId = repository.uId
    val userId = repository.userId
    val imageUri = repository.imageUri
    val score = repository.score

    // 프로필 불러오기
    fun profileLoad(uid : String){
        repository.profileLoad(uid)
    }

    // 프로필 수정하기
    fun profileEdit(uid: String, userDTO: UserDTO) {
        repository.profileEdit(uid, userDTO)
    }
}