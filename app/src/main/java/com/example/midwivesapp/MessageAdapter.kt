package com.example.midwivesapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

// Adapter for displaying messages in the chat
class MessageAdapter(private val MessageList: ArrayList<Message>, private val listener: Chat) :
    RecyclerView.Adapter<MessageAdapter.ChatViewHolder>() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.message_element, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val currentItem = MessageList[position]

        // Check the user type to determine the message layout
        if (currentItem.userType == "midwife") {
            holder.yourLayout.visibility = View.VISIBLE
            holder.motherLayout.visibility = View.GONE
            holder.yourTime.text = currentItem.time.toString()
            holder.yourMessage.text = currentItem.message.toString()
        } else if (currentItem.userType == "mother") {
            holder.motherLayout.visibility = View.VISIBLE
            holder.yourLayout.visibility = View.GONE
            holder.motherTime.text = currentItem.time.toString()
            holder.motherMessage.text = currentItem.message.toString()
        }
    }

    override fun getItemCount(): Int {
        return MessageList.size
    }

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val motherLayout: LinearLayout = itemView.findViewById(R.id.motherLayout)
        val yourLayout: LinearLayout = itemView.findViewById(R.id.yourLayout)
        val motherMessage: TextView = itemView.findViewById(R.id.motherMessage)
        val yourMessage: TextView = itemView.findViewById(R.id.yourMessage)
        val motherTime: TextView = itemView.findViewById(R.id.motherTime)
        val yourTime: TextView = itemView.findViewById(R.id.youTime)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}
