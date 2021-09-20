package com.hanyeop.happysharing

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
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
import com.hanyeop.happysharing.dialog.LoadingDialog
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.PICK_PROFILE_FROM_ALBUM
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding

    private var uId : String? = null
    private var userDTO = UserDTO()

    private var imageUri : Uri? = null
    private lateinit var dialog : Dialog

    private var delete = false // 이미지 삭제 여부

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

        dialog = LoadingDialog(this@ProfileActivity)

        // 프로필 편집
        binding.apply {

            // 프로필 이미지 수정 버튼
            profileChangeButton.setOnClickListener {
                var photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                this@ProfileActivity.startActivityForResult(photoPickerIntent,PICK_PROFILE_FROM_ALBUM)
            }

            // 프로필 이미지 초기화
            profileResetButton.setOnClickListener {
                userDTO.imageUri = "default"
                Glide.with(this@ProfileActivity).load("")
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .apply(RequestOptions().circleCrop()).into(binding.profileImageView)
                delete = true // 삭제
            }

            // 취소 버튼
            cancelButton.setOnClickListener {
                finish()
            }

            // 확인 버튼
            okButton.setOnClickListener {
                userDTO.userId = userIdEditView.text.toString() // 입력한 닉네임으로 변경

                when {
                    // 프로필 이미지 초기화 했을 때
                    delete -> {
                        dialog.show()
                        deleteImage()
                    }
                    // 이미지를 변경하지 않았을 때
                    imageUri == null -> {
                        firebaseViewModel.profileEdit(userDTO)
                        Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    // 이미지를 변경했을 때
                    imageUri != null -> {
                        dialog.show()
                        uploadImage()
                    }
                }
            }
        }
    }

    // 이미지 업로드
    private fun uploadImage() {
        var storageRef =
            FirebaseStorage.getInstance().reference.child("userProfileImages").child(uId!!)
        storageRef.putFile(imageUri!!).continueWithTask { _: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            // 이미지가 업로드 되면 불러오고 종료
            userDTO.imageUri = uri.toString()
            firebaseViewModel.profileEdit(userDTO)
            Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            finish()
        }
    }

    // 이미지 삭제
    private fun deleteImage(){
        FirebaseStorage.getInstance().reference.child("userProfileImages").child(uId!!).delete()
            .addOnSuccessListener {
                // 이미지가 삭제되면 종료
                firebaseViewModel.profileEdit(userDTO)
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this@ProfileActivity,"변경 완료",Toast.LENGTH_SHORT).show()
                firebaseViewModel.profileEdit(userDTO)
                dialog.dismiss()
                finish()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 프로필 사진 이미지 미리 보여주기
        if(requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            delete = false

            imageUri = data?.data!!

            Log.d(TAG, "onActivityResult: $imageUri")

            Glide.with(this@ProfileActivity).load(imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop()).into(binding.profileImageView)
            }
        }
    }