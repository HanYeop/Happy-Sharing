package com.hanyeop.happysharing.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hanyeop.happysharing.databinding.ItemChatlistBinding
import com.hanyeop.happysharing.model.ItemDTO
import com.hanyeop.happysharing.model.MessageDTO
import com.hanyeop.happysharing.util.Constants.Companion.TAG

class ChatListAdapter(private val uId: String)
    : RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder>(){

    // 채팅방 리스트
    private var chatList : ArrayList<MessageDTO> = arrayListOf()

    // Firestore 초기화
    private val fireStore = FirebaseFirestore.getInstance()

    init {
        // 채팅 리스트 불러오기

//        fireStore.collection("item").orderBy("timestamp",
//            Query.Direction.DESCENDING).addSnapshotListener { querySnapshot, _ ->
//            itemList.clear() // 리스트 초기화
//
//            if(querySnapshot == null) return@addSnapshotListener
//
//            // 리스트 불러오기
//            for(snapshot in querySnapshot.documents){
//                var item = snapshot.toObject(ItemDTO::class.java)
//                itemList.add(item!!)
//            }

            fireStore.collection("chat")
            .document(uId).collection("yoDpuMi3MIUaCVPcRIeqxUf8tY73")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, _ ->

            if(querySnapshot == null) return@addSnapshotListener

            // 문서 수신
            for (doc in querySnapshot.documentChanges) {
                // 문서가 추가될 경우 리사이클러 뷰에 추가
                if (doc.type == DocumentChange.Type.ADDED) {
                    Log.d(TAG, "$doc: ")
                    var message = doc.document.toObject(MessageDTO::class.java)
                    chatList.add(message)
                }
            }

            notifyDataSetChanged()
            Log.d(TAG, ": $chatList")
            Log.d(TAG, ": $uId")
        }
    }

    inner class ChatListViewHolder(private val binding: ItemChatlistBinding) : RecyclerView.ViewHolder(binding.root){

        // 채팅 정보 바인딩
        fun bind(message : MessageDTO) {
            binding.apply {

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