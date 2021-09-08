package com.hanyeop.happysharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.repository.FirebaseRepository

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : FirebaseRepository = FirebaseRepository()

    val userDTO = repository.userDTO

    // 프로필 불러오기
    fun profileLoad(uid : String){
        repository.profileLoad(uid)
    }

    // 프로필 수정하기
    fun profileEdit(uid: String, userDTO: UserDTO) {
        repository.profileEdit(uid, userDTO)
    }

    // 아이템 업로드하기
    fun uploadItem(time: Long, itemDTO: ItemDTO){
        repository.uploadItem(time, itemDTO)
    }
}