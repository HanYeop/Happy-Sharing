package com.hanyeop.happysharing.fragment

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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
import com.hanyeop.happysharing.repository.FirebaseRepository
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class MoreFragment : Fragment(R.layout.fragment_more) {

    // 참조 관리
    private var _binding : FragmentMoreBinding? = null
    private val binding get() = _binding!!

    // Firebase
    private var auth : FirebaseAuth? = null
    private var uId : String? = null

    // 현재 유저
    private var curUser = UserDTO()

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: 실행")

        // 뷰바인딩
        _binding = FragmentMoreBinding.bind(view)

        // firebase 초기화
        auth = FirebaseAuth.getInstance()
        uId = FirebaseAuth.getInstance().currentUser?.uid

        // 툴바 텍스트 변경
        val toolbar : androidx.appcompat.widget.Toolbar = requireActivity().findViewById(R.id.toolbar)
        toolbar.title = "더 보기"

        binding.apply {
            // 로그아웃 버튼
            singOutButton.setOnClickListener {
                signOut()
            }

            // 프로필 버튼
            profileButton.setOnClickListener {
                profileEdit()
            }

            // 퀴즈 버튼
            quizButton.setOnClickListener {
                val action = MoreFragmentDirections.actionMoreFragmentToQuizActivity(curUser)
                findNavController().navigate(action)
            }

            // 내 글 버튼
            myItemButton.setOnClickListener {
                val action = MoreFragmentDirections.actionMoreFragmentToMyItemActivity()
                findNavController().navigate(action)
            }
        }

        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)

        // 유저 정보 동기화
        firebaseViewModel.userDTO.observe(viewLifecycleOwner,{
            Log.d(TAG, "onViewCreated: 유저 정보 동기화됨")
            binding.apply {
                curUser = it

                userIdText.text = it.userId
                scoreNumberText.text = it.score.toString()
                shareNumberText.text = it.sharing.toString()
                areaText.text = it.area
                Glide.with(requireActivity()).load(it.imageUri)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .apply(RequestOptions().circleCrop()).into(userImageView)
            }
        })
    }

    // 로그아웃
    private fun signOut(){
        auth?.signOut() // 로컬 로그아웃
        // 구글 클라이언트 로그아웃
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(),gso)
        googleSignInClient.signOut().addOnCompleteListener {
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            ActivityCompat.finishAffinity(requireActivity())
            startActivity(intent)
            Toast.makeText(requireContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 프로필 편집
    private fun profileEdit(){
        val intent = Intent(context, ProfileActivity::class.java)
        startActivity(intent)
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}