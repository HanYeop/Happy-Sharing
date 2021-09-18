package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.hanyeop.happysharing.databinding.ActivityDetailBinding
import com.hanyeop.happysharing.databinding.ActivityLoginBinding
import com.hanyeop.happysharing.fragment.DetailFragmentArgs
import com.hanyeop.happysharing.util.Constants.Companion.TAG

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val args by navArgs<DetailFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            textView2.text = args.itemDTO.title
        }

        Log.d(TAG, "onCreate: ${args.itemDTO} \n ${args.userDTO}")
    }
}