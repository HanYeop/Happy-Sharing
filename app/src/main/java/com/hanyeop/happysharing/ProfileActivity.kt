package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hanyeop.happysharing.databinding.ActivityMainBinding
import com.hanyeop.happysharing.databinding.ActivityProfileBinding
import com.hanyeop.happysharing.model.UserDTO

class ProfileActivity : AppCompatActivity() {

    // ActivityMainBinding 선언
    private lateinit var binding : ActivityProfileBinding

    private var auth : FirebaseAuth? = null
    private var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // 프로필 편집
        binding.apply {
            button.setOnClickListener {
                var userDTO = UserDTO()
                userDTO.uid = auth?.currentUser?.uid
                userDTO.userId = idEditView.text.toString()
                userDTO.imageUri = ""
                userDTO.score = 0

                firestore?.collection("users")?.document(auth!!.currentUser!!.uid)?.set(userDTO)
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }
}