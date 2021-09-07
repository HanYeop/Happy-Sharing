package com.hanyeop.happysharing.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants

class FirebaseRepository() {

    // 유저 이름
    val name = MutableLiveData<String>()
    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    // 프로필 불러오기
    fun profileLoad(uid : String) {
        fireStore?.collection("users")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot == null) return@addSnapshotListener

                var userDTO = documentSnapshot.toObject(UserDTO::class.java)
                if (userDTO?.userId != null) {
                    Log.d(Constants.TAG, "profileLoad: ${userDTO.userId}")
                    // 이름 변경
                    name.value = userDTO.userId.toString()
                }
            }
    }
}