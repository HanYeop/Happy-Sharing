package com.hanyeop.happysharing

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.hanyeop.happysharing.databinding.ActivityUploadBinding
import com.hanyeop.happysharing.dialog.LoadingDialog
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.util.Constants
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class UploadActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUploadBinding

    private var uId : String? = null
    private var itemDTO = ItemDTO()

    private var imageCheck = false // 이미지 등록 여부
    private lateinit var imageUri : Uri
    private var category : String? = null
    private lateinit var dialog : Dialog


    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // uid 불러오기
        uId = FirebaseAuth.getInstance().currentUser?.uid

        // 프로필 불러오기
        firebaseViewModel.profileLoad(uId!!)
        firebaseViewModel.userDTO.observe(this,{
            itemDTO.uId = it.uId
            binding.areaText.setText(it.area)
        })

        // 다이얼로그 초기화
        dialog = LoadingDialog(this)

        binding.apply {
            // 둥근 테두리
            itemImageView.clipToOutline = true

            // 카테고리 설정
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(adapterView: AdapterView<*>?) {}

                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    pos: Int,
                    id: Long
                ) {
                    when (pos) {
                        0 -> category = null
                        1 -> category = "전자기기"
                        2 -> category = "남성의류"
                        3 -> category = "여성의류"
                        4 -> category = "가구/생활가전"
                        5 -> category = "도서"
                        6 -> category = "음식"
                        7 -> category = "잡화"
                        8 -> category = "기타"
                    }
                }
            }

            // 이미지 등록 버튼
            addButton.setOnClickListener {
                var photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                this@UploadActivity.startActivityForResult(photoPickerIntent,
                    Constants.PICK_ITEM_IMAGE_FROM_ALBUM
                )
            }

            // 아이템 업로드
            okButton.setOnClickListener {

                val titleText = titleEditView.text.toString()
                val contextText = contentEditView.text.toString()
                val areaText = areaText.text.toString()

                // 필수 입력 조건 체크
                if(titleText.isEmpty()){
                    titleEditView.error = "제목을 입력해주세요."
                }
                else if(contextText.isEmpty()){
                    contentEditView.error = "글의 내용을 입력해주세요."
                }
                else if(!imageCheck){
                    Toast.makeText(this@UploadActivity, "이미지 추가는 필수입니다.", Toast.LENGTH_SHORT).show()
                }
                else if(category == null){
                    Toast.makeText(this@UploadActivity, "카테고리를 선택해주세요.", Toast.LENGTH_SHORT).show()
                }
                else if(areaText.isEmpty()){
                    Toast.makeText(this@UploadActivity, "지역을 선택해주세요.", Toast.LENGTH_SHORT).show()
                }

                // 조건을 모두 만족했을 때
                else{
                    dialog.show()
                    uploadItem(titleText,contextText,areaText)
                }
            }

            // 취소 버튼
            cancelButton.setOnClickListener {
                finish()
            }
        }
    }

    // 아이템 업로드하기
    private fun uploadItem(title: String, content: String, area: String){

        val time = System.currentTimeMillis()
        itemDTO.title = title
        itemDTO.content = content
        itemDTO.timestamp = time
        itemDTO.category = category
        itemDTO.area = area

        var storageRef =
            FirebaseStorage.getInstance().reference.child("ItemImage").child("${uId}_$time")
        storageRef.putFile(imageUri!!).continueWithTask { _: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            // 이미지가 업로드 되면 불러오고 종료
            itemDTO.imageUri = uri.toString()
            firebaseViewModel.uploadItem(itemDTO)
            Toast.makeText(this@UploadActivity, "추가가 완료되었습니다.", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 아이템 이미지 미리 보여주기
        if(requestCode == Constants.PICK_ITEM_IMAGE_FROM_ALBUM && resultCode == Activity.RESULT_OK){
            imageCheck = true

            imageUri = data?.data!!

            Log.d(Constants.TAG, "onActivityResult: $imageUri")

            Glide.with(this@UploadActivity).load(imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .fitCenter()
                .into(binding.itemImageView)
        }
    }
}
