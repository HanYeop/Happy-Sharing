package com.hanyeop.happysharing.adapter

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.ItemMychatBinding
import com.hanyeop.happysharing.model.ItemDTO

import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG
import com.hanyeop.happysharing.util.Utility

class ChatAdapter(private val currentUid: String, private val otherUser: UserDTO)
    : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // 메시지 리스트
    private var chatList : ArrayList<MessageDTO> = arrayListOf()
    var check = MutableLiveData<ArrayList<MessageDTO>>() // 최신 메시지 확인

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        // 채팅 불러오기
        fireStore.collection("chat")
            .document(currentUid).collection(otherUser.uId.toString())
            .orderBy("timestamp",
            Query.Direction.ASCENDING).addSnapshotListener { querySnapshot, _ ->

                if(querySnapshot == null) return@addSnapshotListener

                // 문서 수신
                for (doc in querySnapshot.documentChanges) {
                    // 문서가 추가될 경우 리사이클러뷰에 추가
                    if (doc.type == DocumentChange.Type.ADDED) {
                        var message = doc.document.toObject(MessageDTO::class.java)
                        chatList.add(message)
                    }
                }
                notifyDataSetChanged()
                check.value = chatList
            }
    }

    inner class ChatViewHolder(private val binding: ItemMychatBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // 채팅 정보 바인딩
        fun bind(message : MessageDTO) {
            binding.apply {

                // 내가 한 채팅
                if(message.fromUid == currentUid){
                    myChat(binding,message)
                }

                // 상대방 채팅
                else if(message.fromUid == otherUser.uId.toString()){
                    otherChat(binding,message)
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val binding = ItemMychatBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ChatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    // 내 채팅 표시
    private fun myChat(binding: ItemMychatBinding, message: MessageDTO){
        binding.apply {
            // 내 채팅 바인딩
            myMessageText.text = message.content
            myTimeText.text = Utility.chatTimeConverter(message.timestamp!!)

            // 내 채팅 보이기
            myMessageText.visibility = View.VISIBLE
            myTimeText.visibility = View.VISIBLE

            // 상대방 채팅 가리기
            userIdText.visibility = View.GONE
            otherMessageText.visibility = View.GONE
            otherTimeText.visibility = View.GONE
            profileImageView.visibility = View.GONE
        }
    }

    // 상대방 채팅 표시
    private fun otherChat(binding: ItemMychatBinding, message: MessageDTO){
        binding.apply {
            // 상대방 채팅 바인딩
            userIdText.text = otherUser.userId
            otherMessageText.text = message.content
            otherTimeText.text = Utility.chatTimeConverter(message.timestamp!!)
            Glide.with(profileImageView.context)
                .load(otherUser.imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop())
                .into(profileImageView)

            // 상대방 채팅 보이기
            userIdText.visibility = View.VISIBLE
            otherMessageText.visibility = View.VISIBLE
            otherTimeText.visibility = View.VISIBLE
            profileImageView.visibility = View.VISIBLE

            // 내 채팅 가리기
            myMessageText.visibility = View.GONE
            myTimeText.visibility = View.GONE
        }
    }
}