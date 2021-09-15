package com.hanyeop.happysharing

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hanyeop.happysharing.databinding.ActivityMainBinding
import com.hanyeop.happysharing.databinding.ActivityProfileBinding
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.PICK_PROFILE_FROM_ALBUM
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class ProfileActivity : AppCompatActivity() {

    // ActivityMainBinding 선언
    private lateinit var binding : ActivityProfileBinding

    private var uId : String? = null
    private var userDTO = UserDTO()

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid

        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)
        firebaseViewModel.userDTO.observe(this,{
            userDTO = it
            binding.apply {
                // 기존 프로필 불러오기
                userIdEditView.setText(it.userId.toString())
                Glide.with(this@ProfileActivity).load(it.imageUri)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .apply(RequestOptions().circleCrop()).into(profileImageView)
            }
        })

        // 프로필 편집
        binding.apply {

            // 프로필 이미지 수정 버튼
            profileChangeButton.setOnClickListener {
                var photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                this@ProfileActivity.startActivityForResult(photoPickerIntent,PICK_PROFILE_FROM_ALBUM)
            }

            // TODO 프로필 초기화
            profileResetButton.setOnClickListener {

            }

            // 취소 버튼
            cancelButton.setOnClickListener {
                finish()
            }

            // 확인 버튼
            okButton.setOnClickListener {
                userDTO.userId = userIdEditView.text.toString() // 입력한 닉네임으로 변경

                firebaseViewModel.profileEdit(userDTO)
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()

                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 프로필 사진 이미지 파이어베이스에 올리기
        if(requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            var imageUri = data?.data

            Log.d(TAG, "onActivityResult: $imageUri")

            Glide.with(this@ProfileActivity).load(imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop()).into(binding.profileImageView)

            var storageRef = FirebaseStorage.getInstance().reference.child("userProfileImages").child(uId!!)
            storageRef.putFile(imageUri!!).continueWithTask { _: Task<UploadTask.TaskSnapshot> ->
                return@continueWithTask storageRef.downloadUrl
            }.addOnSuccessListener { uri ->
                userDTO.imageUri = uri.toString()

                Log.d(TAG, "onActivityResult: ${uri.toString()}")

                firebaseViewModel.profileEdit(userDTO)

//                Glide.with(this@ProfileActivity).load(uri.toString())
//                    .placeholder(R.drawable.ic_baseline_person_24)
//                    .apply(RequestOptions().circleCrop()).into(binding.profileImageView)

//                var map = HashMap<String,Any>()
//                map["image"] = uri.toString()
//                FirebaseFirestore.getInstance().collection("profileImages").document(uId!!).set(map)
            }
        }
    }
}