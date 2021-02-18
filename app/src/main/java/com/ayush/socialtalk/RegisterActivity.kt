package com.ayush.socialtalk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity()
{
    //firebase
    private lateinit var mAuth:FirebaseAuth
    private lateinit var refUsers:DatabaseReference
    private var firebaseUserId : String = ""



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val toolbar: Toolbar = findViewById(R.id.toolbar_register)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "Account Create"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {

            val intent  = Intent(this@RegisterActivity,WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
        //firebase
        mAuth = FirebaseAuth.getInstance()

        //When Click On  Register BTn
        register_btn.setOnClickListener {
            registerUser()
        }

    }



    //function
    private fun registerUser()
    {
        //Get Value From the Fields
        val username : String = username_register.text.toString()
        val email : String = email_register.text.toString()
        val password : String = password_register.text.toString()

        if (username == "")
        {
            Toast.makeText(this@RegisterActivity,"Please write username.",Toast.LENGTH_LONG).show()
        }
        else if (email == "")
        {
            Toast.makeText(this@RegisterActivity,"Please write email.",Toast.LENGTH_LONG).show()
        }
        else if (password == "")
        {
            Toast.makeText(this@RegisterActivity,"Please write password.",Toast.LENGTH_LONG).show()
        }
        else
        {
            mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener {task ->
                    if (task.isSuccessful)
                    {
                        firebaseUserId =  mAuth.currentUser!!.uid
                        refUsers  = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUserId)

                        val userHashMap = HashMap<String,Any>()
                        userHashMap["uid"] = firebaseUserId
                        userHashMap["username"] = username
                        userHashMap["profile"] = "https://firebasestorage.googleapis.com/v0/b/social-talk-b25d2.appspot.com/o/profile_image.png?alt=media&token=1fc37308-16a5-497c-ab1d-a5fc79aa9cb0"
                        userHashMap["cover"] = "https://firebasestorage.googleapis.com/v0/b/social-talk-b25d2.appspot.com/o/cover.jpg?alt=media&token=3cc04bda-abb5-4b3f-bdca-02f81c09fd6f"

                        userHashMap["status"] = "Offline"
                        userHashMap["search"] = username.toLowerCase()
                        userHashMap["facebook"] = "https://m.facebook.com"
                        userHashMap["instagram"] = "https://m.instagram.com"
                        userHashMap["website"] = "https://m.linkedin.com"

                        refUsers.updateChildren(userHashMap)
                            .addOnCompleteListener{task ->
                                if (task.isSuccessful)
                                {
                                    val intent  = Intent(this@RegisterActivity,MainActivity::class.java)
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    startActivity(intent)
                                    finish()
                                }
                            }

                    }
                    else
                    {

                        Toast.makeText(this@RegisterActivity,"Error Message : "+task.exception!!.message.toString(),Toast.LENGTH_LONG).show()

                    }

            }
        }
    }
}