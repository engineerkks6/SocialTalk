package com.ayush.socialtalk.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayush.socialtalk.AdapterClasses.UserAdapter
import com.ayush.socialtalk.ModelClasses.Users
import com.ayush.socialtalk.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : Fragment() {

    private lateinit var userAdapter : UserAdapter
    private var mUsers : List<Users>? = null
    private var recyclerView : RecyclerView? = null
    private var searchEditText : EditText? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       val view : View  = inflater.inflate(R.layout.fragment_search, container, false)

        recyclerView = view.findViewById(R.id.search_list)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.layoutManager = LinearLayoutManager(context)

        searchEditText = view.findViewById(R.id.searchuserET)

        mUsers = ArrayList()
        //Function Called
        retrieveAllUsers()


        //When Click On Search Fields
        searchEditText!!.addTextChangedListener(object : TextWatcher
        {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
            {

            }

            override fun onTextChanged(cs: CharSequence?, p1: Int, p2: Int, p3: Int)
            {
                 searchForUsers(cs.toString().toLowerCase())
            }

            override fun afterTextChanged(p0: Editable?)
            {

            }

        })

        return view
    }//end on created view



    private fun retrieveAllUsers()
    {
        //Firebase
          var  firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
          val  refUsers = FirebaseDatabase.getInstance().reference.child("Users")

        refUsers.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList<Users>).clear()

                if (searchEditText!!.text.toString() == "")
                {
                    for (snapshot in p0.children)
                    {
                        val user : Users? = snapshot.getValue(Users::class.java)
                        if (!(user!!.getUID().equals(firebaseUserID)))
                        {
                            (mUsers as ArrayList<Users>).add(user)
                        }
                    }


                    if (context!=null)
                    {
                        userAdapter = UserAdapter(context!!, mUsers!!, false)
                        recyclerView!!.adapter = userAdapter
                    }
                }


            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })

    }


    private fun searchForUsers(str:String)
    {
        //Firebase
        var  firebaseUserID = FirebaseAuth.getInstance().currentUser!!.uid
        val  queryUsers = FirebaseDatabase.getInstance().reference
            .child("Users").orderByChild("search")
            .startAt(str)
            .endAt(str + "\uf8ff")

        queryUsers.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                (mUsers as ArrayList<Users>).clear()
                for (snapshot in p0.children)
                {
                    val user : Users? = snapshot.getValue(Users::class.java)
                    if (!(user!!.getUID().equals(firebaseUserID)))
                    {
                        (mUsers as ArrayList<Users>).add(user)
                    }
                }
                if(context!=null)
                {
                    userAdapter = UserAdapter(context!!,mUsers!!,false)
                    recyclerView!!.adapter = userAdapter
                }




            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}