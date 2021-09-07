package com.hanyeop.happysharing.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanyeop.happysharing.databinding.ItemObjectBinding

class ListAdapter(val titleList : Array<String>, val userList : Array<String>)
    : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    // 생성된 뷰 홀더에 값 지정
    class ListViewHolder(val binding: ItemObjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(title: String, user: String) {
            // 뷰 홀더의 제목과 설명
            binding.titleText.text = title
            binding.userText.text = user

//            // 뷰 홀더 클릭시 디테일뷰로
//            binding.cardView.setOnClickListener {
//                val intent: Intent = Intent(it.context, DetailViewActivity::class.java)
//                intent.putExtra("currentName", name)
//                intent.putExtra("currentDes", des)
//                it.context.startActivity(intent)
//            }
        }
    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemObjectBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    // 뷰 홀더에 데이터 바인딩
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(titleList[position],userList[position])
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return titleList.size
    }
}