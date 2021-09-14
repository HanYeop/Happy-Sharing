package com.hanyeop.happysharing.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.FragmentListBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel


class ListFragment : Fragment(R.layout.fragment_list) {

    // 참조 관리
    private var _binding : FragmentListBinding? = null
    private val binding get() = _binding!!

//    lateinit var titleList : Array<String>
//    lateinit var userList : Array<String>

    // ListAdapter 선언
    private lateinit var listAdapter: ListAdapter

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()
    private var itemList = arrayListOf<ItemDTO>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentListBinding.bind(view)

//        // 제목과 아이디 초기화
//        titleList = resources.getStringArray(R.array.item_title)
//        userList = resources.getStringArray(R.array.item_user)
//
        itemList = firebaseViewModel.importItem()

        binding.apply {
            listAdapter = ListAdapter(itemList)
            recyclerView.adapter = listAdapter

            listAdapter.notifyDataSetChanged()

            button3.setOnClickListener {
                listAdapter.notifyDataSetChanged()
            }
        }
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
