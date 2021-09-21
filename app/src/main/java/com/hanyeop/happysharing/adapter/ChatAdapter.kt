package com.hanyeop.happysharing.adapter

import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.R
import com.hanyeop.happysharing.databinding.ItemMychatBinding
import com.hanyeop.happysharing.model.ItemDTO

import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.model.UserDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG

class ChatAdapter(private val currentUid: String, private val otherUser: UserDTO)
    : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    // 메시지 리스트
    private var chatList : ArrayList<MessageDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        // 아이템 리스트 불러오기
        fireStore.collection("chat").document(currentUid)
            .collection(otherUser.uId.toString()).orderBy("timestamp",
            Query.Direction.ASCENDING).addSnapshotListener { querySnapshot, _ ->
                chatList.clear() // 리스트 초기화

                if(querySnapshot == null) return@addSnapshotListener

                // 리스트 불러오기
                for(snapshot in querySnapshot.documents){
                    var message = snapshot.toObject(MessageDTO::class.java)
                    chatList.add(message!!)
                }
                notifyDataSetChanged()
                Log.d(TAG, "$chatList ")
            }
    }

    inner class ChatViewHolder(private val binding: ItemMychatBinding)
        : RecyclerView.ViewHolder(binding.root) {

        // 아이템 정보 바인딩
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

    fun myChat(binding: ItemMychatBinding, message: MessageDTO){
        binding.apply {
            myMessageText.text = message.content
            myTimeText.text = message.timestamp.toString()

            myMessageText.visibility = View.VISIBLE
            myTimeText.visibility = View.VISIBLE
        }
    }

    fun otherChat(binding: ItemMychatBinding, message: MessageDTO){
        binding.apply {
            userIdText.text = otherUser.userId
            otherMessageText.text = message.content
            otherTimeText.text = message.timestamp.toString()
            Glide.with(profileImageView.context)
                .load(otherUser.imageUri)
                .placeholder(R.drawable.ic_baseline_person_24)
                .apply(RequestOptions().circleCrop())
                .into(profileImageView)

            userIdText.visibility = View.VISIBLE
            otherMessageText.visibility = View.VISIBLE
            otherTimeText.visibility = View.VISIBLE
            profileImageView.visibility = View.VISIBLE
        }
    }
}