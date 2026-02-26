package com.example.gyanlike

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.AddStdModel
import com.example.gyanlike.Model.SubjectModel
import com.example.gyanlike.adapter.AddSubAdapter
import com.example.gyanlike.databinding.ActivityAdminSubjectBinding
import com.example.gyanlike.util.Pref
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class AdminSubjectActivity : CommonActivity() {

    lateinit var binding: ActivityAdminSubjectBinding
    lateinit var db : FirebaseDatabase
    lateinit var auth: FirebaseAuth
    lateinit var storage: FirebaseStorage
    lateinit var dbref : DatabaseReference
  // lateinit var stdModel: AddStdModel
    var stdModel = ArrayList<AddStdModel>()
    var subModel = ArrayList<SubjectModel>()

    lateinit var progress: ProgressDialog
    lateinit var subRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminSubjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        initView()

        progress = ProgressDialog(this)
        progress.setTitle("Uploading")
        progress.setMessage("Please wait")

        subRecyclerView = findViewById(R.id.ASubList)
        subRecyclerView.layoutManager = LinearLayoutManager(this)
        subRecyclerView.setHasFixedSize(true)

    }

    @SuppressLint("MissingInflatedId")
    private fun initView() {

        binding.customHeader.setTitle(R.string.standard)

        val id = intent.getStringExtra("ID")
        val std = intent.getStringExtra("STDID")
        binding.txtSubTitle.text = std

        if (Pref.getIntValue(Pref.PREF_USER_TYPE,2) == 2){
            binding.btnaddSub.visibility = View.GONE
            binding.addimg.visibility = View.GONE
        }

        binding.btnaddSub.setOnClickListener {
            progress.show()

            val builder = AlertDialog.Builder(this)
            val inflate: LayoutInflater = layoutInflater
            val dialogLayout: View = inflate.inflate(R.layout.item_add_sub, null)
            val edittext: EditText = dialogLayout.findViewById(R.id.edtASub)

            with(builder){
                setTitle("Enter Subject Name..!")

                setPositiveButton("Add"){dialog , which ->

                    val name = edittext.text.toString().trim()
                    val rendomkey = db.reference.push().key
                    val subjectModel = SubjectModel(subjectname = name , id = rendomkey.toString())

                    db.getReference().child("mcq").child(id.toString()).child("subject").child(rendomkey!!)
                        .setValue(subjectModel).addOnSuccessListener{
                                Toast.makeText(context, "Data Uploaded", Toast.LENGTH_SHORT).show()
                                progress.dismiss()
                        }
                        .addOnFailureListener{
                            Toast.makeText(context, "Data Not Uploaded", Toast.LENGTH_SHORT).show()
                            progress.dismiss()
                        }
                }

                setNegativeButton("Cancel"){ dialog, which ->
                    Log.d("Main", "Negative Button Clicked")
                    progress.dismiss()
                }

                setView(dialogLayout)
                show()
            }

        }

        fetchData()

    }

    private fun fetchData() {

        val id = intent.getStringExtra("ID")

        dbref = FirebaseDatabase.getInstance().getReference().child("mcq").child(id!!).child("subject")
        dbref.addValueEventListener(object : ValueEventListener{
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {
                subModel.clear()

                if (snapshot.exists()){
                    for (subSnap in snapshot.children) {
                        val subData = subSnap.getValue(SubjectModel::class.java)
                        subModel.add(subData!!)
                    }

                    val madapter = AddSubAdapter(this@AdminSubjectActivity, subModel )
                    subRecyclerView.adapter = madapter
                    madapter.notifyDataSetChanged()

                    subRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

}