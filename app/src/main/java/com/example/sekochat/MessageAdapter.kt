package com.example.sekochat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messagelist: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ircv = 1
    val isnt = 2

    override fun getItemViewType(position: Int): Int {

        val currentmessage = messagelist[position]

        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentmessage.senderid)) {
            return isnt
        } else {
            return ircv
        }


    }

    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val senttext: TextView = itemView.findViewById(R.id.smessage)

    }

    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val receivetext: TextView = itemView.findViewById(R.id.rmessage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve, parent, false)
            return ReceiveViewHolder(view)
        } else {
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent, parent, false)
            return SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentmessage = messagelist[position]

        if (holder.javaClass == SentViewHolder::class.java) {
            val viewHolder = holder as SentViewHolder
            holder.senttext.text = currentmessage.message
        } else {
            val viewHolder = holder as ReceiveViewHolder
            holder.receivetext.text = currentmessage.message

        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }
}