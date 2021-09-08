package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.databinding.ActivityUploadBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class UploadActivity : AppCompatActivity() {

    // ActivityUploadBinding 선언
    private lateinit var binding : ActivityUploadBinding

    private var uId : String? = null
    private var itemDTO = ItemDTO()

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid

        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)
        firebaseViewModel.userDTO.observe(this,{
            binding.userIdText.text = it.userId.toString() // 닉네임 불러오기

            itemDTO.uId = it.uId
            itemDTO.userId = it.userId
        })

        binding.apply {
            // 아이템 업로드
            button2.setOnClickListener {
                val time = System.currentTimeMillis()
                itemDTO.imageUrl = ""
                itemDTO.title = titleEditView.text.toString()
                itemDTO.content = contentEditView.text.toString()
                itemDTO.timestamp = time

                firebaseViewModel.uploadItem(time, itemDTO)
                Toast.makeText(this@UploadActivity, "추가완료", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}