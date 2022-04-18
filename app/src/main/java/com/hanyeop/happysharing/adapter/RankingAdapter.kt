package com.hanyeop.happysharing.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.ItemRankingBinding
import com.hanyeop.happysharing.model.UserDTO

class RankingAdapter : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    private var userList: ArrayList<UserDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        // 유저 정보 불러옴
        fireStore.collection("users").orderBy("score",Query.Direction.DESCENDING)
            .limit(5).get()
            .addOnCompleteListener { documentSnapshot ->

                if (documentSnapshot.isSuccessful) {
                    // 리스트 불러오기
                    for(snapshot in documentSnapshot.result){
                        var user = snapshot.toObject(UserDTO::class.java)
                        userList.add(user)
                    }
                }
                notifyDataSetChanged()
            }
    }

    inner class RankingViewHolder(private val binding: ItemRankingBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // 유저 정보 표시
        fun bind(userDTO: UserDTO,position: Int){
            binding.apply {
                userIdText.text = userDTO.userId.toString()
                scoreNumberText.text = userDTO.score.toString()
                shareNumberText.text = userDTO.sharing.toString()
                rankNumText.text = (position+1).toString()
                areaText2.text = userDTO.area.toString()

                Glide.with(profileImageView.context)
                    .load(userDTO.imageUri)
                    .placeholder(R.drawable.ic_baseline_person_24)
                    .apply(RequestOptions().circleCrop()).into(profileImageView)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RankingAdapter.RankingViewHolder {
        val binding = ItemRankingBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RankingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RankingAdapter.RankingViewHolder, position: Int) {
        holder.bind(userList[position],position)
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}