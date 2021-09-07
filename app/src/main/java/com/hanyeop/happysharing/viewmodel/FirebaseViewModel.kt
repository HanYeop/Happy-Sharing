package com.hanyeop.happysharing.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.hanyeop.happysharing.repository.FirebaseRepository

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    private val repository : FirebaseRepository = FirebaseRepository()
    val name = repository.name

    // 프로필 불러오기
    fun profileLoad(uid : String){
        repository.profileLoad(uid)
    }
}