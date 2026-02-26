package com.example.gyanlike

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.AddStdModel
import com.example.gyanlike.Model.StdModel
import com.example.gyanlike.Model.UserModel
import com.example.gyanlike.PreferenceHelper.set
import com.example.gyanlike.adapter.AddStdAdapter
import com.example.gyanlike.adapter.StdAdapter
import com.example.gyanlike.databinding.ActivityHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class HomeActivity : CommonActivity(){

    lateinit var binding: ActivityHomeBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    lateinit var stdArrayList: ArrayList<StdModel>
    var model = ArrayList<AddStdModel>()

    lateinit var dbref : DatabaseReference
    lateinit var stdRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        stdRecyclerView = findViewById(R.id.listStd)
        stdRecyclerView.layoutManager = LinearLayoutManager(this)
        stdRecyclerView.setHasFixedSize(true)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        stdArrayList = arrayListOf<StdModel>()
        model = arrayListOf<AddStdModel>()

        initView()
      //  getUserData()

        }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance().getReference("user")

        dbref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){

                        val user = userSnapshot.getValue(StdModel::class.java)
                        stdArrayList.add(user!!)
                    }

                    stdRecyclerView.adapter = StdAdapter(stdArrayList)

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initView() {

        binding.btnHLogOut.setOnClickListener {
            sharedPreferences[USER_LOGIN] = false
            startActivity(Intent(this , LoginActivity::class.java))
        }

        binding.btnHHLogOut.setOnClickListener {
            sharedPreferences[USER_LOGIN] = false
            startActivity(Intent(this , LoginActivity::class.java))
        }

        val uid = auth.currentUser?.uid

        db.collection("user").document(uid!!).collection("user-info").document("userdata").get().addOnSuccessListener { document->
            binding.txtName.text = document.get("name").toString()
        }

        binding.btnedtName.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.custom_edt_txt, null)
            val editText : EditText = dialogLayout.findViewById(R.id.et_edtText)

            with(builder){
                setTitle("Edit Your Name...!")

                setPositiveButton("OK"){ dialog, which ->
                    binding.txtName.text = editText.text.toString()

                    val name = binding.txtName.text.toString().trim()

                    val usermodel = UserModel(name = name)

                    db.collection("user").document(uid).collection("user-info").document("userdata").set(usermodel)
                        .addOnCompleteListener { doc ->
                            Toast.makeText(context , "Name Changed" , Toast.LENGTH_SHORT).show()
                        }
                }

                setNegativeButton("Cancel") { dialog , which ->
                    Log.d("Main" , "Negative Button Clicked")
                }

                setView(dialogLayout)
                show()
            }

        }

        fetchData()

    }

    private fun fetchData() {

        dbref = FirebaseDatabase.getInstance().getReference("mcq")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                model.clear()
                if (snapshot.exists()){
                    for (stdSnap in snapshot.children){
                        val stdData = stdSnap.getValue(AddStdModel::class.java)
                        model.add(stdData!!)
                    }
                    val mAdapter = AddStdAdapter(this@HomeActivity , model)
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
