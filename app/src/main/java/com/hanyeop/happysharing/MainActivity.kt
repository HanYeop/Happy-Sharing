package com.hanyeop.happysharing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.hanyeop.happysharing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    // NavController 선언
    private lateinit var navController: NavController

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

            // 바텀네비게이션뷰 <-> 네비게이션 연결
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.findNavController()
            bottomNavigationView.setupWithNavController(navController)

            // 물건 추가 버튼
            addButton.setOnClickListener {
                val intent = Intent(this@MainActivity,UploadActivity::class.java)
                startActivity(intent)
            }
        }
    }
}