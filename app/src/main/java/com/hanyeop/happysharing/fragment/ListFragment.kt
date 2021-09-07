package com.hanyeop.happysharing.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.FragmentListBinding


class ListFragment : Fragment(R.layout.fragment_list) {

    // 참조 관리
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

    lateinit var titleList : Array<String>
    lateinit var userList : Array<String>

    // ListAdapter 선언
    private lateinit var listAdapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentListBinding.bind(view)

        // 제목과 아이디 초기화
        titleList = resources.getStringArray(R.array.item_title)
        userList = resources.getStringArray(R.array.item_user)

        binding.apply {
            listAdapter = ListAdapter(titleList,userList)
            recyclerView.adapter = listAdapter
        }
    }
}