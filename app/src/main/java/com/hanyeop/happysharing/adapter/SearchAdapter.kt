package com.hanyeop.happysharing.adapter

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
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility

class SearchAdapter(private val listener : OnItemClickListener,query: String,type :Int)
    : RecyclerView.Adapter<SearchAdapter.ListViewHolder>() {

    private var itemList: ArrayList<ItemDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        // 검색 일 때
        if(type == Constants.SEARCH) {
            // 아이템 리스트 불러오기
            fireStore.collection("item").orderBy(
                "timestamp",
                Query.Direction.DESCENDING
            ).get().addOnCompleteListener { documentSnapshot ->
                itemList.clear() // 리스트 초기화

                if (documentSnapshot.isSuccessful) {
                    // 리스트 불러오기
                    for (snapshot in documentSnapshot.result) {
                        if (snapshot.getString("title")!!.contains(query)) {
                            var item = snapshot.toObject(ItemDTO::class.java)
                            itemList.add(item)
                        }
                    }
                }
                notifyDataSetChanged()
            }
        }

        // 내 글 목록 일때
        else if(type == Constants.MY_ITEM){
            // 아이템 리스트 불러오기
            fireStore.collection("item")
                .whereEqualTo("uid",query)
                .get().addOnCompleteListener { documentSnapshot ->
                itemList.clear() // 리스트 초기화

                if (documentSnapshot.isSuccessful) {
                    // 리스트 불러오기
                    for (snapshot in documentSnapshot.result) {
                        var item = snapshot.toObject(ItemDTO::class.java)
                        itemList.add(item)
                        Log.d(TAG, "$item: ")
                    }
                }
                notifyDataSetChanged()
            }
        }

        // 카테고리별 조회 일 때
        else if(type == Constants.CATEGORY){
            fireStore.collection("item")
                .whereEqualTo("category",query)
                .get().addOnCompleteListener { documentSnapshot ->
                    itemList.clear() // 리스트 초기화

                    if (documentSnapshot.isSuccessful) {
                        // 리스트 불러오기
                        for (snapshot in documentSnapshot.result) {
                            var item = snapshot.toObject(ItemDTO::class.java)
                            itemList.add(item)
                            Log.d(TAG, "$item: ")
                        }
                    }
                    notifyDataSetChanged()
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

                            // 양도 완료된 상품
                            if(item.completed) {
                                cardView.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this.root.context,
                                            R.color.item_grey
                                        )
                                    )
                                completedText.visibility = View.VISIBLE
                            }

                            // 완료되지 않은 상품
                            else{
                                cardView.backgroundTintList =
                                    ColorStateList.valueOf(
                                        ContextCompat.getColor(
                                            this.root.context,
                                            R.color.white
                                        )
                                    )
                                completedText.visibility = View.GONE
                            }

                            // 보여줄 준비가 되면
                            uiShow(binding)

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

    private fun uiHide(binding : ItemObjectBinding){
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

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun uiShow(binding : ItemObjectBinding){
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

        binding.progressBar.visibility = View.GONE
    }
}