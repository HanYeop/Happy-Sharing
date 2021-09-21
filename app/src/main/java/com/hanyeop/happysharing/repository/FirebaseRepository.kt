package com.hanyeop.happysharing.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG

class FirebaseRepository() {

    val userDTO = MutableLiveData<UserDTO>() // 유저 정보

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    // 프로필 불러오기
    fun profileLoad(uid : String) {
        fireStore.collection("users").document(uid)
            .addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot == null) return@addSnapshotListener

                val userDTO = documentSnapshot.toObject(UserDTO::class.java)
                if (userDTO?.userId != null) {
                    this.userDTO.value = userDTO!!
                }

                else if(userDTO?.userId == null){
                    Log.d(TAG, "아이디가 존재하지 않음")
                    val newUserDTO = UserDTO(uid,"사용자","default",0,0)
                    fireStore.collection("users").document(uid).set(newUserDTO)
                }
            }
    }

    // 프로필 수정하기
    fun profileEdit(userDTO: UserDTO) {
        fireStore.collection("users").document(userDTO.uId!!).set(userDTO)
    }

    // 아이템 업로드하기
    fun uploadItem(itemDTO: ItemDTO){
        fireStore.collection("item").document(itemDTO.timestamp.toString()).set(itemDTO)
    }

    // 아이템 삭제하기
    fun deleteItem(itemDTO: ItemDTO){
        fireStore.collection("item").document(itemDTO.timestamp.toString()).delete()
    }

    // 메시지 전송하기
    fun uploadChat(messageDTO: MessageDTO){
        fireStore.collection("chat")
            .document(messageDTO.fromUid.toString())
            .collection(messageDTO.toUid.toString())
            .document(messageDTO.timestamp.toString())
            .set(messageDTO)

        fireStore.collection("chat")
            .document(messageDTO.toUid.toString())
            .collection(messageDTO.fromUid.toString())
            .document(messageDTO.timestamp.toString())
            .set(messageDTO)
    }
}