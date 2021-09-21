package com.hanyeop.happysharing.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.ItemChatlistBinding
import com.hanyeop.happysharing.model.ChatListDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility

class ChatListAdapter(private val uId: String)
    : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>(){

    // 채팅방 리스트
    private var chatList : ArrayList<ChatListDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
            fireStore.collection("chatList")
                .whereArrayContains("chatPerson",uId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->

            if(querySnapshot == null) return@addSnapshotListener

            // 문서 수신
            for (doc in querySnapshot.documentChanges) {
                // 문서가 추가될 경우 리사이클러 뷰에 추가
                if (doc.type == DocumentChange.Type.ADDED) {
                    Log.d(TAG, "$doc: ")
                    var message = doc.document.toObject(ChatListDTO::class.java)
                    chatList.add(message)
                }
            }

            notifyDataSetChanged()
        }
    }

    inner class ChatListViewHolder(private val binding: ItemChatlistBinding) : RecyclerView.ViewHolder(binding.root){

        // 채팅 정보 바인딩
        fun bind(message : ChatListDTO) {

            binding.apply {
                if(message.fromUid == uId){
                    // 유저 정보 불러옴
                    fireStore.collection("users").document(message.toUid!!).get()
                        .addOnCompleteListener { documentSnapshot->

                            if(documentSnapshot.isSuccessful){
                                val userDTO = documentSnapshot.result.toObject(UserDTO::class.java)
                                userIdText.text = userDTO!!.userId

                                Glide.with(profileImageView.context)
                                    .load(userDTO.imageUri)
                                    .placeholder(R.drawable.ic_baseline_person_24)
                                    .apply(RequestOptions().circleCrop())
                                    .into(profileImageView)

//                                // 아이템 클릭 시
//                                root.setOnClickListener {
//                                    listener.onItemClick(item,userDTO)
//                                }
                            }
                        }
                }

                else if(message.toUid == uId){
                    // 유저 정보 불러옴
                    fireStore.collection("users").document(message.fromUid!!).get()
                        .addOnCompleteListener { documentSnapshot->

                            if(documentSnapshot.isSuccessful){
                                val userDTO = documentSnapshot.result.toObject(UserDTO::class.java)
                                userIdText.text = userDTO!!.userId

                                Glide.with(profileImageView.context)
                                    .load(userDTO.imageUri)
                                    .placeholder(R.drawable.ic_baseline_person_24)
                                    .apply(RequestOptions().circleCrop())
                                    .into(profileImageView)

//                                // 아이템 클릭 시
//                                root.setOnClickListener {
//                                    listener.onItemClick(item,userDTO)
//                                }
                            }
                        }
                }

                contentText.text = message.content
                timeText.text = Utility.chatTimeConverter(message.timestamp!!)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChatListAdapter.ChatListViewHolder {
        val binding = ItemChatlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatListAdapter.ChatListViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }
}