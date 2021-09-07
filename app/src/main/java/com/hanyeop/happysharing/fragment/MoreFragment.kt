package com.hanyeop.happysharing.fragment

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hanyeop.happysharing.LoginActivity
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.FragmentListBinding
import com.hanyeop.happysharing.databinding.FragmentMoreBinding

class MoreFragment : Fragment(R.layout.fragment_more) {

    // 참조 관리
    private var _binding : FragmentMoreBinding? = null
    private val binding get() = _binding!!

    // FirebaseAuth
    private var auth : FirebaseAuth? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentMoreBinding.bind(view)

        // 계정 동기화
        auth = Firebase.auth

        binding.apply {
            // 로그아웃 버튼
            singOutButton.setOnClickListener {
                signOut()
            }
        }
    }

    // 로그아웃
    private fun signOut(){
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        ActivityCompat.finishAffinity(requireActivity())
        startActivity(intent)
        auth?.signOut()
        Toast.makeText(requireContext(), "로그아웃됨", Toast.LENGTH_SHORT).show()
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}