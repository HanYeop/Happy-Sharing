package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.adapter.ChatAdapter
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.ActivityChattingBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.NotificationBody
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class ChattingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChattingBinding
    private var uId : String? = null

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    // ListAdapter 선언
    private lateinit var chatAdapter: ChatAdapter

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    // 현재 유저 닉네임
    private var curUserId = ""

    // 상대방 토큰
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid
        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)

        // 유저 닉네임 동기화
        firebaseViewModel.userDTO.observe(this,{
            Log.d(TAG, "onViewCreated: 유저 정보 동기화됨")
            binding.apply {
                curUserId = it.userId.toString()
            }
        })

        // 상대방 Uid 불러오기
        val otherUId = intent.getStringExtra("otherUid").toString()


        binding.apply {
            // 채팅창이 공백일 경우 send 버튼 비활성화, 아니면 활성화
            messageEditView.addTextChangedListener { text->
                sendButton.isEnabled = text.toString() != ""
            }

            // 메시지 입력 시 리사이클러뷰 크기 조절
            messageRecyclerView.addOnLayoutChangeListener {
                    view, left, top, right, bottom, oldLeft, oldRight, oldTop, oldBottom ->
//                if (bottom > oldBottom && ::chatAdapter.isInitialized){
//                    messageRecyclerView.scrollToPosition(chatAdapter.itemCount-1)
//                }

                if(::chatAdapter.isInitialized){
                    messageRecyclerView.scrollToPosition(chatAdapter.itemCount-1)
                }
            }

            // 유저 정보 불러옴
            fireStore.collection("users").document(otherUId).get()
                .addOnCompleteListener { documentSnapshot->

                    if(documentSnapshot.isSuccessful){
                        val userDTO = documentSnapshot.result.toObject(UserDTO::class.java)
                        // 리사이클러뷰 어댑터 연결
                        chatAdapter = ChatAdapter(uId.toString(),userDTO!!)
                        messageRecyclerView.adapter = chatAdapter

                        userText.text = userDTO.userId
                        token = userDTO.token.toString()

                        // 채팅 맨 밑으로 스크롤
                        chatAdapter.check.observe(this@ChattingActivity){
                            messageRecyclerView.scrollToPosition(chatAdapter.itemCount-1)
                        }
                    }
                }

            // 메시지 전송
            sendButton.setOnClickListener {
                // 메세지 세팅
                val time = System.currentTimeMillis()
                val message = MessageDTO(uId,otherUId,messageEditView.text.toString(),time)
                // 메세지 전송
                firebaseViewModel.uploadChat(message)

                // FCM 전송하기
                val data = NotificationBody.NotificationData(getString(R.string.app_name)
                    ,curUserId,messageEditView.text.toString())
                val body = NotificationBody(token,data)
                firebaseViewModel.sendNotification(body)
                // 응답 여부
                firebaseViewModel.myResponse.observe(this@ChattingActivity){
                    Log.d(TAG, "onViewCreated: $it")
                }

                // 전송 후 에디트뷰 초기화
                messageEditView.setText("")
            }
        }
    }
}