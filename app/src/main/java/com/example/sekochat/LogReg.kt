package com.example.sekochat

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.util.*

class LogReg : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var emailr: EditText
    private lateinit var passwordr: EditText
    private lateinit var emaill: EditText
    private lateinit var passwordl: EditText
    private lateinit var userName: EditText
    private lateinit var dbref: DatabaseReference
    private lateinit var imgview: ImageView
    private lateinit var simg: Uri
    private lateinit var taskimg: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_reg)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        emailr = findViewById(R.id.etEmailRegister)
        passwordr = findViewById(R.id.etPasswordRegister)
        userName = findViewById(R.id.nameRegister)
        emaill = findViewById(R.id.etEmailLogin)
        passwordl = findViewById(R.id.etPasswordLogin)

    }

    fun btnreg(view: android.view.View) {
        registerUser()
    }

    fun btnlog(view: android.view.View) {
        loginUser()
    }

    fun btnupd(view: android.view.View) {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, 1)

    }

    private fun registerUser() {
        val emailrr = emailr.text.toString()
        val passwordrr = passwordr.text.toString()
        val userNamer = userName.text.toString()

        val reference = storage.reference.child("Profile").child(Date().time.toString())
        reference.putFile(simg).addOnCompleteListener{
            if(it.isSuccessful){
                reference.downloadUrl.addOnSuccessListener { task ->
                    uploadinfo(userNamer, emailrr,task.toString(),passwordrr)
                }
            }
        }

    }

    private fun uploadinfo(userNamer: String, emailrr: String, taskimg: String, passwordrr: String) {

        if (emailrr.isNotEmpty() && passwordrr.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(emailrr, passwordrr)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        addUsertodb(userNamer, emailrr, taskimg, auth.currentUser?.uid!!)
                        checkLoggedInState()
                    } else {
                        Toast.makeText(this@LogReg, "Authentication Failed", Toast.LENGTH_LONG)
                            .show()
                    }
                }
        } else {
            Toast.makeText(this@LogReg, "Enter Valid Details", Toast.LENGTH_LONG).show()
        }

    }


    private fun addUsertodb(name: String, email: String,image: String, uid: String) {
        dbref =
            FirebaseDatabase.getInstance("https://sekochat-e745c-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference()
        dbref.child("user").child(uid).setValue(User(name, email,image, uid))

    }

    private fun loginUser() {
        val emailll = emaill.text.toString()
        val passwordll = passwordl.text.toString()
        if (emailll.isNotEmpty() && passwordll.isNotEmpty()) {
            auth.signInWithEmailAndPassword(emailll, passwordll)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        checkLoggedInState()
                    } else {
                        Toast.makeText(this@LogReg, "User Does Not Exist", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            Toast.makeText(this@LogReg, "Enter Valid Details", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkLoggedInState() {
        if (auth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        checkLoggedInState()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        imgview = findViewById(R.id.imageView)

        if (data != null) {
            if (data.data != null) {
                simg = data.data!!
                imgview.setImageURI(simg)
            }
        }
    }


}