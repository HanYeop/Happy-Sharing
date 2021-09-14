package com.hanyeop.happysharing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanyeop.happysharing.databinding.ItemObjectBinding
import com.hanyeop.happysharing.model.ItemDTO

class ListAdapter(private val itemList: ArrayList<ItemDTO>)
    : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    // 생성된 뷰 홀더에 값 지정
    class ListViewHolder(private val binding: ItemObjectBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item : ItemDTO) {

            binding.apply {
                titleText.text = item.title
            }

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
        holder.bind(itemList[position])
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return itemList.size
    }
}