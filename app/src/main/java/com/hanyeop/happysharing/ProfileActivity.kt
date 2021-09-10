package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.databinding.ActivityMainBinding
import com.hanyeop.happysharing.databinding.ActivityProfileBinding
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class ProfileActivity : AppCompatActivity() {

    // ActivityMainBinding 선언
    private lateinit var binding : ActivityProfileBinding

    private var uId : String? = null
    private var userDTO = UserDTO()

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid

        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)
        firebaseViewModel.userDTO.observe(this,{
            userDTO = it
            binding.idEditView.setText(userDTO.userId.toString()) // 기존 닉네임 불러오기
        })

        // 프로필 편집
        binding.apply {

            // TODO : NULL 처리
            button.setOnClickListener {
                userDTO.userId = idEditView.text.toString() // 입력한 닉네임으로 변경

                firebaseViewModel.profileEdit(uId!!,userDTO)
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}