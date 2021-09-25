package com.hanyeop.happysharing.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.ItemObjectBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants
import com.hanyeop.happysharing.util.Utility

class ListAdapter(private val listener : OnItemClickListener)
    : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private var itemList: ArrayList<ItemDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()
    private var isFirst = true // 처음 실행여부 ( 한번도 실행되지 않음 = true )

    init {
        // 아이템 리스트 불러오기
        fireStore.collection("item").orderBy("timestamp",
            Query.Direction.DESCENDING).addSnapshotListener { querySnapshot, _ ->
            itemList.clear() // 리스트 초기화

            if(querySnapshot == null) return@addSnapshotListener

            // 리스트 불러오기
            for(snapshot in querySnapshot.documents){
                var item = snapshot.toObject(ItemDTO::class.java)
                itemList.add(item!!)
            }

            // 최초 실행 시 리스트 갱신
            if(isFirst){
                notifyDataSetChanged()
                isFirst = false
            }
        }
    }

    // 생성된 뷰 홀더에 값 지정
    inner class ListViewHolder(private val binding: ItemObjectBinding) : RecyclerView.ViewHolder(binding.root) {

        // 아이템 정보 바인딩
        fun bind(item : ItemDTO) {

            binding.apply {
                uiHide(binding)
                // 유저 정보 불러옴
                fireStore.collection("users").document(item.uId!!).get()
                    .addOnCompleteListener { documentSnapshot->

                        if(documentSnapshot.isSuccessful){
                            val userDTO = documentSnapshot.result.toObject(UserDTO::class.java)
                            userText.text = userDTO!!.userId
                            scoreNumberText.text = userDTO!!.score.toString()
                            shareNumberText.text = userDTO!!.sharing.toString()

                            titleText.text = item.title
                            categoryText.text = item.category
                            dateText.text = Utility.timeConverter(item.timestamp!!)
                            areaText.text = item.area

                            Glide.with(imageView.context)
                                .load(item.imageUri)
                                .placeholder(R.color.grey)
                                .into(imageView)

                            // 양도 완료된 상품 배경색깔
                            if(item.completed) {
                                cardView.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this.root.context,
                                            R.color.item_grey
                                        )
                                    )
                            }

                            // 완료되지 않은 상품 배경색깔
                            else{
                                cardView.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this.root.context,
                                            R.color.white
                                        )
                                    )
                            }

                            // 보여줄 준비가 되면
                            uiShow(binding,item.completed)

                            // 아이템 클릭 시
                           root.setOnClickListener {
                               listener.onItemClick(item,userDTO)
                            }
                        }
                    }
            }
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

    // 아이템 포지션 넘겨주기 위한 인터페이스
    interface OnItemClickListener {
        fun onItemClick(itemDTO: ItemDTO, userDTO: UserDTO)
    }

    private fun uiHide(binding :ItemObjectBinding){
        binding.userText.visibility = View.GONE
        binding.scoreNumberText.visibility = View.GONE
        binding.scoreText.visibility = View.GONE
        binding.shareNumberText.visibility = View.GONE
        binding.shareText.visibility = View.GONE
        binding.titleText.visibility = View.GONE
        binding.categoryText.visibility = View.GONE
        binding.dateText.visibility = View.GONE
        binding.areaText.visibility = View.GONE
        binding.imageView.visibility = View.GONE
        binding.completedText.visibility = View.GONE

        // 로딩바 보여주기
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun uiShow(binding :ItemObjectBinding, completed: Boolean){
        binding.userText.visibility = View.VISIBLE
        binding.scoreNumberText.visibility = View.VISIBLE
        binding.scoreText.visibility = View.VISIBLE
        binding.shareNumberText.visibility = View.VISIBLE
        binding.shareText.visibility = View.VISIBLE
        binding.titleText.visibility = View.VISIBLE
        binding.categoryText.visibility = View.VISIBLE
        binding.dateText.visibility = View.VISIBLE
        binding.areaText.visibility = View.VISIBLE
        binding.imageView.visibility = View.VISIBLE
        if(completed) binding.completedText.visibility = View.VISIBLE

        // 로딩바 숨기기
        binding.progressBar.visibility = View.GONE
    }
}