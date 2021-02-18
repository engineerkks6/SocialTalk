package com.ayush.socialtalk.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.socialtalk.AdapterClasses.UserAdapter
import com.ayush.socialtalk.ModelClasses.ChatList
import com.ayush.socialtalk.ModelClasses.Users
import com.ayush.socialtalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ChatsFragment : Fragment() {

    private lateinit var userAdapter : UserAdapter
    private var mUsers : List<Users>? = null
    private var usersChatList : List<ChatList>? = null

    lateinit var recycler_view_chatList : RecyclerView

    //Firebase
    private var firebaseUser : FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?
    {
        // Inflate the layout for this fragment
       val view = inflater.inflate(R.layout.fragment_chats, container, false)

        recycler_view_chatList = view.findViewById(R.id.recycler_view_chatList)
        recycler_view_chatList.setHasFixedSize(true)
        recycler_view_chatList.layoutManager = LinearLayoutManager(context)

        firebaseUser = FirebaseAuth.getInstance().currentUser

        usersChatList = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("ChatList").child(firebaseUser!!.uid)

        ref!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                (usersChatList as ArrayList).clear()

                for (dataSnapshot in p0.children )
                {
                    val chatList = dataSnapshot.getValue(ChatList::class.java)

                    (usersChatList as ArrayList).add(chatList!!)

                }
                retrieveChatList()

            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        return view
    }



    private fun retrieveChatList()
    {
        mUsers = ArrayList()

        val ref = FirebaseDatabase.getInstance().reference.child("Users")
        ref!!.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList).clear()

                for (dataSnapshot in p0.children)
                {
                    val user = dataSnapshot.getValue(Users::class.java)

                    for (eaachChatList in usersChatList!!)
                    {
                        if (user!!.getUID().equals(eaachChatList.getId()))
                        {
                            (mUsers as ArrayList).add(user!!)

                        }
                    }
                }


                if (context!=null)
                {
                    userAdapter = UserAdapter(context!!, (mUsers as ArrayList<Users>), true)
                    recycler_view_chatList.adapter = userAdapter
                }

            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })

    }

}
