package com.hanyeop.happysharing.fragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ChatListAdapter
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.FragmentChatBinding
import com.hanyeop.happysharing.databinding.FragmentListBinding

class ChatFragment : Fragment(R.layout.fragment_chat) {
    private var uId : String? = null

    // 참조 관리
    private var _binding : FragmentChatBinding? = null
    private val binding get() = _binding!!

    // ChatListAdapter 선언
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentChatBinding.bind(view)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid

        binding.apply {
            // 리사이클러뷰 어댑터 연결
            chatListAdapter = ChatListAdapter(uId.toString())
            chatListRecyclerView.adapter = chatListAdapter
        }
    }


    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}