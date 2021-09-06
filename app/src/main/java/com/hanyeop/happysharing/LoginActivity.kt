package com.hanyeop.happysharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanyeop.happysharing.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // ActivityMainBinding 선언
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            // 다음화면 넘어가기 (테스트용)
            signinButton.setOnClickListener{
                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}