package com.hanyeop.happysharing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.navArgs
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.adapter.SearchAdapter
import com.hanyeop.happysharing.databinding.ActivitySearchBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO

class SearchActivity : AppCompatActivity(), SearchAdapter.OnItemClickListener{

    private lateinit var binding : ActivitySearchBinding

    private val args by navArgs<SearchActivityArgs>()

    // ListAdapter 선언
    private lateinit var listAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val query = args.query

        // 뷰바인딩
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            // 리사이클러뷰 어댑터 연결
            listAdapter = SearchAdapter(this@SearchActivity,query)
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