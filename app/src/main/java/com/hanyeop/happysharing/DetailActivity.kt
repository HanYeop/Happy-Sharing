package com.hanyeop.happysharing

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.hanyeop.happysharing.databinding.ActivityDetailBinding
import com.hanyeop.happysharing.databinding.ActivityLoginBinding
import com.hanyeop.happysharing.dialog.LoadingDialog
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility
import com.hanyeop.happysharing.viewmodel.FirebaseViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailBinding
    private val args by navArgs<DetailActivityArgs>()

    private lateinit var item : ItemDTO
    private lateinit var user: UserDTO

    // 로딩 다이얼로그
    private lateinit var dialog : Dialog

    // 뷰모델 연결
    private val firebaseViewModel : FirebaseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // args 불러오기
        item = args.itemDTO
        user = args.userDTO

        // 다이얼로그 초기화
        dialog = LoadingDialog(this)

        binding.apply {
            // 이미지 둥글게 처리
            imageView.clipToOutline = true

            // 아이템 정보 표시
            Glide.with(this@DetailActivity)
                .load(item.imageUri)
                .placeholder(R.color.grey)
                .into(imageView)
            titleText.text = item.title
            dateText.text = Utility.timeConverter(item.timestamp!!)
            categoryText.text = item.category
            areaText.text = item.area
            contentText.text = item.content

            // 유저 정보 표시
            Glide.with(this@DetailActivity)
                .load(user.imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop()).into(profileImageView)
            userIdText.text = user.userId
            scoreNumberText.text = user.score.toString()
            shareNumberText.text = user.sharing.toString()

            // 글 삭제
            deleteButton.setOnClickListener {
                deleteDialog(this@DetailActivity).show()
            }

            // 글 수정
            editButton.setOnClickListener {

            }

            // 채팅하기
            chatButton.setOnClickListener {
                val intent = Intent(this@DetailActivity,ChattingActivity::class.java)
                intent.putExtra("itemDTO",item)
                startActivity(intent)
            }
        }


    }

    private fun deleteItem(context: Context){
        FirebaseStorage.getInstance().reference.child("ItemImage")
            .child("${item.uId}_${item.timestamp}").delete()
            .addOnSuccessListener {
                // 이미지가 삭제되면 종료
                firebaseViewModel.deleteItem(item)
                Toast.makeText(context,"삭제 완료",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(context,"삭제 실패",Toast.LENGTH_SHORT).show()
                dialog.dismiss()
//                finish()
            }
    }

    // 삭제 다이얼로그
    private fun deleteDialog(context : Context) : AlertDialog.Builder{
        val deleteDialog = AlertDialog.Builder(this@DetailActivity)
        // TODO 아이콘 설정
        deleteDialog.setTitle("글 삭제").setMessage("글을 삭제할까요? 삭제한 내용은 복구할 수 없습니다.")
            .setIcon(R.drawable.ic_baseline_shopping_basket_24)
        deleteDialog.setPositiveButton("예") { _, _ ->
            deleteItem(context)
            dialog.show()
        }
        deleteDialog.setNegativeButton("아니오") { _, _ ->
        }
        return deleteDialog
    }
}