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
    private var userId : String? = null
    private var imageUri : String? = null
    private var score : Int? = null

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
        firebaseViewModel.userId.observe(this,{
            userId = it
            binding.idEditView.setText(userId.toString())
        })
        firebaseViewModel.imageUri.observe(this,{
            imageUri = it
        })
        firebaseViewModel.score.observe(this,{
            score = it
        })

        // 프로필 편집
        binding.apply {

            // TODO : NULL 처리
            button.setOnClickListener {
                var userDTO = UserDTO()
                userDTO.uId = uId
                userDTO.userId = idEditView.text.toString()
                userDTO.imageUri = imageUri!!
                userDTO.score = score!!

                firebaseViewModel.profileEdit(uId!!,userDTO)
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}