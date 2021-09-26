package com.hanyeop.happysharing.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.hanyeop.happysharing.api.RetrofitInstance
import com.hanyeop.happysharing.model.*
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import okhttp3.ResponseBody
import retrofit2.Response

class FirebaseRepository() {

    val userDTO = MutableLiveData<UserDTO>() // 유저 정보
    val currentQuiz = MutableLiveData<QuizDTO>() // 퀴즈 정보
    val myResponse : MutableLiveData<Response<ResponseBody>> = MutableLiveData() // 메세지 수신 정보

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    // 프로필 불러오기
    fun profileLoad(uid : String) {
        // FCM 불러오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task->
            // 실패
            if (!task.isSuccessful) {
                Log.d(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            // 받아온 새로운 토큰
            val token = task.result

            // 프로필 불러오기
            fireStore.collection("users").document(uid)
                .addSnapshotListener { documentSnapshot, _ ->
                    if (documentSnapshot == null) return@addSnapshotListener

                    val userDTO = documentSnapshot.toObject(UserDTO::class.java)
                    if (userDTO?.userId != null) {

                        // 토큰이 변경되었을 경우 갱신
                        if(userDTO.token != token){
                            Log.d(TAG, "profileLoad: 토큰 변경되었음.")
                            val newUserDTO = UserDTO(userDTO.uId,userDTO.userId,
                                userDTO.imageUri,userDTO.score,userDTO.sharing,userDTO.area,token)
                            fireStore.collection("users").document(uid).set(newUserDTO)

                            // 유저정보 라이브데이터 변경하기
                            this.userDTO.value = newUserDTO
                        }

                        // 아니면 그냥 불러옴
                        else {
                            Log.d(TAG, "profileLoad: 이미 동일한 토큰이 존재함.")
                            this.userDTO.value = userDTO!!
                        }
                    }

                    // 아이디 최초 생성 시
                    else if(userDTO?.userId == null){
                        Log.d(TAG, "아이디가 존재하지 않음")
                        val newUserDTO = UserDTO(uid,"사용자","default",0,0,"지역",token)
                        fireStore.collection("users").document(uid).set(newUserDTO)

                        this.userDTO.value = newUserDTO
                    }
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

        // 채팅 저장
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


        // 채팅방 리스트 저장
        var name = ""
        if(messageDTO.fromUid.toString() < messageDTO.toUid.toString()){
            name = "${messageDTO.fromUid}_${messageDTO.toUid.toString()}"
        }
        else if(messageDTO.fromUid.toString() > messageDTO.toUid.toString()){
            name = "${messageDTO.toUid}_${messageDTO.fromUid.toString()}"
        }
        val chatPerson = arrayListOf(messageDTO.fromUid.toString(),messageDTO.toUid.toString())
        val chatList = ChatListDTO(messageDTO.fromUid,messageDTO.toUid
            ,messageDTO.content,messageDTO.timestamp,chatPerson)
        fireStore.collection("chatList")
            .document(name).set(chatList)
    }

    // 퀴즈 불러오기
    fun quizLoad(num: Int) {
        fireStore.collection("quiz").whereEqualTo("quizNumber", num)
            .get().addOnCompleteListener { documentSnapshot ->

                if (documentSnapshot.isSuccessful) {
                    // 리스트 불러오기
                    for (snapshot in documentSnapshot.result) {
                        var quiz = snapshot.toObject(QuizDTO::class.java)
                        currentQuiz.value = quiz
                    }
                }
            }
    }

    // 푸시 메세지 전송
    suspend fun sendNotification(notification: NotificationBody) {
        myResponse.value = RetrofitInstance.api.sendNotification(notification)
    }
}