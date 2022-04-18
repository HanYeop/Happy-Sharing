package com.hanyeop.happysharing

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.adapter.SearchAdapter
import com.hanyeop.happysharing.databinding.ActivityMyItemBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants

class MyItemActivity : AppCompatActivity(), SearchAdapter.OnItemClickListener  {

    private lateinit var binding : ActivityMyItemBinding
    private var uId : String? = null

    // ListAdapter 선언
    private lateinit var listAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityMyItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid.toString()

        binding.apply {
            // 리사이클러뷰 어댑터 연결
            listAdapter = SearchAdapter(this@MyItemActivity,uId!!,Constants.MY_ITEM)
            recyclerView.adapter = listAdapter

            finishButton.setOnClickListener {
                finish()
            }
        }
    }

    override fun onItemClick(itemDTO: ItemDTO, userDTO: UserDTO) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("itemDTO",itemDTO)
        intent.putExtra("userDTO",userDTO)
        startActivity(intent)
    }
}