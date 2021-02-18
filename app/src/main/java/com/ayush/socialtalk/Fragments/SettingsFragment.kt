package com.ayush.socialtalk.Fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.ayush.socialtalk.ModelClasses.Users
import com.ayush.socialtalk.R
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_settings.view.*

class SettingsFragment : Fragment() {

    var userReference : DatabaseReference? = null
    var firebaseUser : FirebaseUser? =  null
    private val RequestCode = 438;
    private var imageUri : Uri? = null
    private var  storageRef : StorageReference? =null
    private var coverChecker:String? =  ""
    private var socialChecker:String? =  ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_settings, container, false)


        //Firebase
        firebaseUser = FirebaseAuth.getInstance().currentUser
        userReference = FirebaseDatabase.getInstance().reference.child("Users").child(firebaseUser!!.uid)
        storageRef  = FirebaseStorage.getInstance().reference.child("User Images")

        userReference!!.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot)
            {
                if (p0.exists())
                {
                    val user :  Users? = p0.getValue(Users::class.java)

                   if (context!=null)
                   {
                       view.username_settings.text = user!!.getUserName()
                       Picasso.get().load(user.getProfile()).into(view.profile_image_settings)
                       Picasso.get().load(user.getCover()).into(view.cover_image_settings)
                   }
                }
            }

            override fun onCancelled(p0: DatabaseError)
            {

            }
        })

        //When Click On Profile Image in Setting
        view.profile_image_settings.setOnClickListener{
            pickImage()
        }
        //When Click On Cover Image in Setting
        view.cover_image_settings.setOnClickListener{
            coverChecker = "cover"
            pickImage()
        }
        //When Click On facebook link in Setting
        view.set_facebook.setOnClickListener{
             socialChecker = "facebook"
             setSocialLinks()
        }

        //When Click On Instagram Gram link in Setting
        view.set_instagram.setOnClickListener{
            socialChecker = "instagram"
            setSocialLinks()
        }
        //When Click On Website link in Setting
        view.set_website.setOnClickListener{
            socialChecker = "website"
            setSocialLinks()
        }

        return view
    }//end On Create View

    private fun setSocialLinks()
    {
        val builder:AlertDialog.Builder =
            AlertDialog.Builder(requireContext(),R.style.Theme_AppCompat_DayNight_Dialog_Alert)

        if (socialChecker == "facebook")
        {
            builder.setTitle("Write Username: ")
        }
        if (socialChecker == "instagram")
        {
            builder.setTitle("Write Username: ")
        }
        if (socialChecker == "website")
        {
            builder.setTitle("Write Username: ")
        }

        val editText = EditText(context)

        if (socialChecker == "facebook")
        {
            editText.hint = "e.g : ayush3456.."
        }
        if (socialChecker == "instagram")
        {
            editText.hint = "e.g : vivek3456.."
        }
        if (socialChecker == "website")
        {
            editText.hint = "e.g : twinkle3456.."
        }


        builder.setView(editText)

        builder.setPositiveButton("Create",DialogInterface.OnClickListener {
                dialog, which ->

            val str = editText.text.toString()

            if (str == "")
            {
                Toast.makeText(context,"Please write somtheing...",Toast.LENGTH_LONG).show()

            }
            else
            {
                saveSocialLinks(str)
            }
        })


        builder.setNegativeButton("Cancel",DialogInterface.OnClickListener {
                dialog, which ->
                dialog.cancel()
        })

        builder.show()
    }

    private fun saveSocialLinks(str: String)
    {
        val mapSocial = HashMap<String,Any>()

        when(socialChecker)
        {
            "facebook" ->
            {
                mapSocial["facebook"] = "https://m.facebook.com/$str"

            }
            "instagram" ->
            {
                mapSocial["instagram"] = "https://m.instagram.com/$str"

            }
            "website" ->
            {
                mapSocial["website"] = "https://m.linkedin.com/$str"

            }
        }

         userReference!!.updateChildren(mapSocial).addOnCompleteListener { task ->

             if (task.isSuccessful)
             {
                 Toast.makeText(context,"Updated Successfully.",Toast.LENGTH_LONG).show()

             }
         }

    }


    private fun pickImage()
    {
         val intent = Intent()
        intent.type = "image/*"
        intent.action  = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,RequestCode)
    }

    //Now Get The Image From The Gallery We Have Built In Finction like

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RequestCode && resultCode == Activity.RESULT_OK && data!!.data != null)
        {
            imageUri = data.data
            Toast.makeText(context,"Uploading....",Toast.LENGTH_LONG).show()
            uploadImageToDataBase()
        }
    }

    private fun uploadImageToDataBase()
    {
        val progressBar = ProgressDialog(context)
        progressBar.setMessage("Image is uploading, please wait....")
        progressBar.show()

        if (imageUri!=null)
        {
            val fileRef = storageRef!!.child(System.currentTimeMillis().toString()  + ".jpg")

            var uploadTask:StorageTask<*>
            uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.continueWithTask (Continuation<UploadTask.TaskSnapshot,Task<Uri>>{ task ->

                if (!task.isSuccessful)
                {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation fileRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful)
                {
                    val downloadUrl = task.result
                        val url = downloadUrl.toString()

                        if (coverChecker == "cover")
                        {
                            val mapCoverImg = HashMap<String,Any>()
                            mapCoverImg["cover"] = url
                            userReference!!.updateChildren(mapCoverImg)
                            coverChecker = ""
                        }
                        else
                        {
                            val mapProfileImg = HashMap<String,Any>()
                            mapProfileImg["profile"] = url
                        userReference!!.updateChildren(mapProfileImg)
                        coverChecker = ""

                    }
                    progressBar.dismiss()

                }
            }
        }
    }
}