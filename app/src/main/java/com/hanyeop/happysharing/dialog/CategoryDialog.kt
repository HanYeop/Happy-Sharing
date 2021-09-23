package com.hanyeop.happysharing.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.adapter.ListAdapter
import com.hanyeop.happysharing.databinding.DialogCategoryBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO

class CategoryDialog(context: Context,
                     private val listener : OnCategorySelectedListener) : Dialog(context) {

    private lateinit var binding : DialogCategoryBinding

    // 현재 카테고리
    private var category : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 뷰바인딩
        binding = DialogCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // 크기 조절
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        binding.apply {

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

            // 확인 버튼
            okButton.setOnClickListener {
                if(category!= null) {
                    listener.onCategorySelected(category!!)
                    dismiss()
                }
            }

            // 취소 버튼
            cancleButton.setOnClickListener {
                dismiss()
            }
        }
    }

    // 선택된 값 액티비티로 넘겨줌
    interface OnCategorySelectedListener{
        fun onCategorySelected(category: String)
    }
}