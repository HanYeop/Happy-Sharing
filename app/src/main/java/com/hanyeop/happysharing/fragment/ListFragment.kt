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

    lateinit var nameList : Array<String>
    lateinit var descriptionList : Array<String>

    // ListAdapter 선언
    private lateinit var listAdapter: ListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentListBinding.bind(view)

        // 이름과 설명 초기화
        nameList = resources.getStringArray(R.array.item_name)
        descriptionList = resources.getStringArray(R.array.item_description)

        binding.apply {
            listAdapter = ListAdapter(nameList,descriptionList)
            recyclerView.adapter = listAdapter
        }
    }
}