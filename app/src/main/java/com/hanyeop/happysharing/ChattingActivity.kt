package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.adapter.ChatAdapter
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.ActivityChattingBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class ChattingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChattingBinding
    private var uId : String? = null
    private var otherUId : String? = null

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    // ListAdapter 선언
    private lateinit var chatAdapter: ChatAdapter

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityChattingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 아이템 불러오기
        val item = intent.getParcelableExtra<ItemDTO>("itemDTO")

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid
        otherUId = item!!.uId


        binding.apply {
            // 채팅창이 공백일 경우 send 버튼 비활성화, 아니면 활성화
            messageEditView.addTextChangedListener { text->
                sendButton.isEnabled = text.toString() != ""
            }

            // 유저 정보 불러옴
            fireStore.collection("users").document(item.uId!!).get()
                .addOnCompleteListener { documentSnapshot->

                    if(documentSnapshot.isSuccessful){
                        val userDTO = documentSnapshot.result.toObject(UserDTO::class.java)
                        // 리사이클러뷰 어댑터 연결
                        chatAdapter = ChatAdapter(uId.toString(),userDTO!!)
                        messageRecyclerView.adapter = chatAdapter
                    }
                }

            // 메시지 전송
            sendButton.setOnClickListener {
                val time = System.currentTimeMillis()
                val message = MessageDTO(uId,item!!.uId,messageEditView.text.toString(),time)
                messageEditView.setText("")

                firebaseViewModel.uploadChat(message)
            }
        }
    }
}