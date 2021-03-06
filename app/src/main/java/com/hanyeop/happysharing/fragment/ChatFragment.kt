package com.hanyeop.happysharing.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.hanyeop.happysharing.ChattingActivity
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ChatListAdapter
import com.hanyeop.happysharing.databinding.FragmentChatBinding

class ChatFragment : Fragment(R.layout.fragment_chat), ChatListAdapter.OnChatClickListener {
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

        // 툴바 텍스트 변경
        val toolbar : androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.title = "대화 목록"

        binding.apply {
            // 리사이클러뷰 어댑터 연결
            chatListAdapter = ChatListAdapter(uId.toString(),this@ChatFragment)
            chatListRecyclerView.adapter = chatListAdapter
            chatListRecyclerView.addItemDecoration(DividerItemDecoration(view.context,1))
        }
    }

    // 채팅 클릭 시
    override fun onChatClick(userUid: String) {
        val intent = Intent(requireContext(), ChattingActivity::class.java)
        intent.putExtra("otherUid",userUid)
        startActivity(intent)
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 목록 갱신 위함
    override fun onResume() {
        super.onResume()
        chatListAdapter.notifyDataSetChanged()
    }
}