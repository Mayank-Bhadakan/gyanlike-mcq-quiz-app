package com.example.gyanlike

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.inflate
import com.example.gyanlike.Model.AddStdModel
import com.example.gyanlike.Model.SubjectModel
import com.example.gyanlike.Model.UserModel
import com.example.gyanlike.PreferenceHelper.set
import com.example.gyanlike.adapter.AddStdAdapter
import com.example.gyanlike.databinding.ActivityAdminStdBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.snapshots
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class AdminStd : CommonActivity()  {

    lateinit var binding: ActivityAdminStdBinding
    lateinit var db: FirebaseDatabase
    lateinit var storage: FirebaseStorage
    lateinit var addStdAdapter: AddStdAdapter
    lateinit var auth: FirebaseAuth
    lateinit var dbref: DatabaseReference
    var model = ArrayList<AddStdModel>()
    var subModel = ArrayList<SubjectModel>()

    var i:Int = 1
    lateinit var progress: ProgressDialog
    lateinit var stdRecyclerView: RecyclerView
    lateinit var itemStd: TextView
    lateinit var editText : EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStdBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        initView()

        progress = ProgressDialog(this)
        progress.setTitle("Uploading")
        progress.setMessage("please wait")
      //  progress.show()

        stdRecyclerView = findViewById(R.id.AStdList)
        stdRecyclerView.layoutManager = LinearLayoutManager(this)
        stdRecyclerView.setHasFixedSize(true)

        model = arrayListOf<AddStdModel>()
      //  addStdAdapter.addData(model)

    }

    @SuppressLint("InflateParams")
    private fun initView() {

        binding.btnASignOut.setOnClickListener {
            sharedPreferences[ADMIN_LOGIN] = false
            startActivity(Intent(this , LoginActivity::class.java))
            Toast.makeText(this , "Log Out successfully" , Toast.LENGTH_SHORT).show()
        }
        binding.btnAdSignOut.setOnClickListener {
            sharedPreferences[ADMIN_LOGIN] = false
            startActivity(Intent(this , LoginActivity::class.java))
            Toast.makeText(this , "Log Out successfully" , Toast.LENGTH_SHORT).show()
        }

        binding.btnaddStd.setOnClickListener {
            progress.show()

            val builder = AlertDialog.Builder(this)
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.item_add_std, null)
            val editText: EditText  = dialogLayout.findViewById(R.id.edtAStd)


            with(builder){
                setTitle("Enter Standard Name..!")

                setPositiveButton("Add"){ dialog, which ->
                 //   itemStd.text = editText.text.toString()

                    val name = editText.text.toString().trim()
                    val rendomKey = db.reference.push().key
                    val addStdModel = AddStdModel(stdName = name , id = rendomKey.toString())

                    db.getReference().child("mcq").child(rendomKey!!)
                        .setValue(addStdModel).addOnSuccessListener({
                            Toast.makeText(context,"Data Uploaded", Toast.LENGTH_SHORT).show()
                            progress.dismiss()
                        }).addOnFailureListener({
                            Toast.makeText(context, "Data not upload", Toast.LENGTH_SHORT).show()
                            progress.dismiss()
                        })
                }

                setNegativeButton("Cancel") { dialog , which ->
                    Log.d("Main" , "Negative Button Clicked")
                    progress.dismiss()
                }

                setView(dialogLayout)
                show()
            }
        }

        fetchData()
    }

    private fun fetchData() {
//        binding.AStdList.layoutManager = GridLayoutManager(this , 1, RecyclerView.VERTICAL, false)
//        addStdAdapter = AddStdAdapter(this, model, subModel)
//        binding.AStdList.adapter = addStdAdapter

        dbref = FirebaseDatabase.getInstance().getReference("mcq")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                model.clear()
                if (snapshot.exists()){
                    for (stdSnap in snapshot.children){
                        val stdData = stdSnap.getValue(AddStdModel::class.java)
                        model.add(stdData!!)
                    }
                    val mAdapter = AddStdAdapter(this@AdminStd , model)
                    stdRecyclerView.adapter = mAdapter

                    stdRecyclerView.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        }
}