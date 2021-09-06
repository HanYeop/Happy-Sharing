package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hanyeop.happysharing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // ActivityMainBinding 선언
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            // 툴바 지정
            setSupportActionBar(toolbar)
            toolbar.title = "안녕하세요"

            // 바텀네비게이션 음영 삭제
            bottomNavigationView.background = null
        }
    }
}