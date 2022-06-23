package com.example.sekochat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox: EditText
    private lateinit var sendButton: Button
    private lateinit var messageList: ArrayList<Message>
    private lateinit var dbref: DatabaseReference
    private lateinit var messageAdapter: MessageAdapter

    var rRoom: String? = null
    var sRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
        dbref = FirebaseDatabase.getInstance("https://sekochat-e745c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        sRoom = receiverUid + senderUid
        rRoom = senderUid + receiverUid

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.rv_messages)
        messageBox = findViewById(R.id.etmessage)
        sendButton = findViewById(R.id.btn_send)
        messageList = ArrayList()
        messageAdapter  = MessageAdapter(this,messageList)

        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter  = messageAdapter

        dbref.child("chats").child(sRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){

                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)

                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {}

            })

        sendButton.setOnClickListener {

            val messageh = messageBox.text.toString()
            val messageObject = Message(messageh, senderUid)

                if(messageBox.text.toString().isNotEmpty()){
            dbref.child("chats").child(sRoom!!).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {

                    dbref.child("chats").child(rRoom!!).child("messages").push()
                        .setValue(messageObject)
                }
            messageBox.setText("")

           }
        }


    }
}