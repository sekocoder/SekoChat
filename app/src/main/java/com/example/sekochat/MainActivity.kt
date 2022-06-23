package com.example.sekochat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

     private lateinit var userRcv: RecyclerView
     private lateinit var userlist: ArrayList<User>
     private lateinit var adapterc: UserAdapter
     private lateinit var dbref: DatabaseReference
     private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        dbref = FirebaseDatabase.getInstance("https://sekochat-e745c-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference()

        userlist = ArrayList()
        adapterc = UserAdapter(this,userlist)
        userRcv = findViewById(R.id.rvlist)
        userRcv.layoutManager = LinearLayoutManager(this)
        userRcv.adapter = adapterc

        dbref.child("user").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userlist.clear()
                for(postSnapshot in snapshot.children){

                    val cuser = postSnapshot.getValue(User::class.java)
                    if(auth.currentUser?.uid != cuser?.uid)
                    {
                        userlist.add(cuser!!)
                    }

                    adapterc.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    fun logoutfn(view: android.view.View) {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this,LogReg::class.java)
        startActivity(intent)
        finish()
    }

}