package com.hanyeop.happysharing.fragment

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.hanyeop.happysharing.LoginActivity
import com.hanyeop.happysharing.ProfileActivity
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.FragmentListBinding
import com.hanyeop.happysharing.databinding.FragmentMoreBinding
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG

class MoreFragment : Fragment(R.layout.fragment_more) {

    // 참조 관리
    private var _binding : FragmentMoreBinding? = null
    private val binding get() = _binding!!

    // Firebase
    private var auth : FirebaseAuth? = null
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    // 유저 이름
    private val name = MutableLiveData<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 뷰바인딩
        _binding = FragmentMoreBinding.bind(view)

        // firebase 초기화
        auth = FirebaseAuth.getInstance()
        uid = FirebaseAuth.getInstance().currentUser?.uid

        // firestore 초기화
        firestore = FirebaseFirestore.getInstance()

        // 프로필 불러오기
        profileLoad()

        binding.apply {
            // 로그아웃 버튼
            singOutButton.setOnClickListener {
                signOut()
            }

            // 프로필 버튼
            profileButton.setOnClickListener {
                profileEdit()
            }
        }

        // 유저 이름 동기화
        name.observe(viewLifecycleOwner,{
            binding.userIdText.text = it
        })
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

    // 프로필 편집
    private fun profileEdit(){
        val intent = Intent(context, ProfileActivity::class.java)
        startActivity(intent)
    }

    // 프로필 불러오기
    private fun profileLoad() {
        firestore?.collection("users")?.document(uid!!)
            ?.addSnapshotListener { documentSnapshot, _ ->
                if (documentSnapshot == null) return@addSnapshotListener

                var userDTO = documentSnapshot.toObject(UserDTO::class.java)
                if (userDTO?.userId != null) {
                    Log.d(TAG, "profileLoad: ${userDTO.userId}")
                    // 이름 변경
                    name.value = userDTO.userId.toString()
                }
            }
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}