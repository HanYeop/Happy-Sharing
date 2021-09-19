package com.hanyeop.happysharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hanyeop.happysharing.databinding.ActivityDetailBinding
import com.hanyeop.happysharing.databinding.ActivityLoginBinding
import com.hanyeop.happysharing.fragment.DetailFragmentArgs
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val args by navArgs<DetailFragmentArgs>()

    private lateinit var item : ItemDTO
    private lateinit var user: UserDTO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        item = args.itemDTO
        user = args.userDTO

        binding.apply {
            imageView.clipToOutline = true

            // 아이템 정보 표시
            Glide.with(this@DetailActivity)
                .load(item.imageUri)
                .placeholder(R.color.grey)
                .into(imageView)
            titleText.text = item.title
            dateText.text = Utility.timeConverter(item.timestamp!!)
            categoryText.text = item.category
            areaText.text = item.area
            contentText.text = item.content


            // 유저 정보 표시
            Glide.with(this@DetailActivity)
                .load(user.imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop()).into(profileImageView)
            userIdText.text = user.userId
            scoreNumberText.text = user.score.toString()
            shareNumberText.text = user.sharing.toString()
        }

        Log.d(TAG, "onCreate: ${args.itemDTO} \n ${args.userDTO}")
    }
}