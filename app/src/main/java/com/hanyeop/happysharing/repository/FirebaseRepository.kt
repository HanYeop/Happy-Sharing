package com.hanyeop.happysharing.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants

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
            }
    }

    // 프로필 수정하기
    fun profileEdit(uid : String, userDTO: UserDTO) {
        fireStore.collection("users").document(uid).set(userDTO)
    }

    // 아이템 업로드하기
    fun uploadItem(time: Long, itemDTO: ItemDTO){
        fireStore.collection("item").document(time.toString()).set(itemDTO)
    }

    // 아이템 리스트 불러오기
    fun importItem(): ArrayList<ItemDTO>{
        val itemList = arrayListOf<ItemDTO>()

        fireStore.collection("item").orderBy("timestamp",
            Query.Direction.DESCENDING).addSnapshotListener { querySnapshot, _ ->
            itemList.clear() // 리스트 초기화

            if(querySnapshot == null) return@addSnapshotListener

            // 리스트 불러오기
            for(snapshot in querySnapshot.documents){
                var item = snapshot.toObject(ItemDTO::class.java)
                itemList.add(item!!)
            }
        }
        return itemList
    }
}