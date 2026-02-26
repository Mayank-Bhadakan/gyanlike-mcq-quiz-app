package com.example.gyanlike

import android.app.AlertDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.ChapterModel
import com.example.gyanlike.adapter.AddChapterAdapter
import com.example.gyanlike.databinding.ActivityAdminChapterBinding
import com.example.gyanlike.util.Pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminChapterActivity : CommonActivity() {

    lateinit var binding: ActivityAdminChapterBinding
    lateinit var db : FirebaseDatabase
    lateinit var dbref: DatabaseReference
    lateinit var progress: ProgressDialog
    lateinit var chRecyclerView: RecyclerView

    var chModel = ArrayList<ChapterModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminChapterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()

        initView()

        progress = ProgressDialog(this)
        progress.setTitle("Uploading")
        progress.setMessage("Please wait")

        chRecyclerView = findViewById(R.id.AChList)
        chRecyclerView.layoutManager = LinearLayoutManager(this)
        chRecyclerView.setHasFixedSize(true)

    }

    private fun initView() {

        binding.customHeader.setTitle(R.string.standard)

        val id = intent.getStringExtra("SUBID")
        val subName = intent.getStringExtra("STDSUB")
        binding.txtsubtitle.text = subName

        if (Pref.getIntValue(Pref.PREF_USER_TYPE,2) == 2){
            binding.btnaddCh.visibility = View.GONE
            binding.addimg.visibility = View.GONE
        }

        binding.btnaddCh.setOnClickListener {
            progress.show()

            val builder = AlertDialog.Builder(this)
            val inflate: LayoutInflater = layoutInflater
            val dialogLayout: View = inflate.inflate(R.layout.item_add_chapter , null)
            val editText: EditText = dialogLayout.findViewById(R.id.edtAChap)

            with(builder){

                setTitle("Enter Chapter Number..!")

                setPositiveButton("Add"){dialog , which ->

                    val name = editText.text.toString().trim()
                    val rendomKeygenerate = db.reference.push().key
                    val chapterModel = ChapterModel(chapterName = name , id = rendomKeygenerate.toString())

                    db.getReference().child("mcq").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (randomKeySnapShot in snapshot.children){
                                val randomKey = randomKeySnapShot.key

                                db.getReference().child("mcq").child(randomKey!!).child("subject")
                                    .addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (subRandomKeySnapShot in snapshot.children){
                                                val subRandomKey = subRandomKeySnapShot.key

                                                if (subRandomKey == id){

                                                    db.getReference().child("mcq").child(randomKey!!).child("subject").child(id!!)
                                                        .child("chapter").child(rendomKeygenerate!!).setValue(chapterModel)
                                                        .addOnSuccessListener {
                                                            Toast.makeText(context , "Data Uploaded" , Toast.LENGTH_SHORT).show()
                                                            progress.dismiss()
                                                        }
                                                        .addOnFailureListener {
                                                            Toast.makeText(context , "Data Not Uploaded" , Toast.LENGTH_SHORT).show()
                                                            progress.dismiss()
                                                        }
                                                }
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }

                                    })

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(context, "StdKey Failed" , Toast.LENGTH_SHORT).show()
                        }

                    })
                }

                setNegativeButton("Cancel"){dialog, which ->
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

        val id = intent.getStringExtra("SUBID")

        db.getReference().child("mcq").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (randomKeySnapShot in snapshot.children) {
                    val randomKey = randomKeySnapShot.key

                    db.getReference().child("mcq").child(randomKey!!).child("subject")
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (subRandomKeySnapShot in snapshot.children) {
                                    val subRandomKey = subRandomKeySnapShot.key

                                    if (subRandomKey == id) {

                                        dbref = FirebaseDatabase.getInstance().getReference().child("mcq").child(randomKey!!)
                                            .child("subject").child(id!!).child("chapter")
                                        dbref.addValueEventListener(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {

                                                chModel.clear()

                                                if (snapshot.exists()){
                                                    for (chSnap in snapshot.children){
                                                        val chValue = chSnap.getValue(ChapterModel::class.java)
                                                        chModel.add(chValue!!)
                                                    }

                                                    val mAdapter = AddChapterAdapter(this@AdminChapterActivity , chModel)
                                                    chRecyclerView.adapter = mAdapter

                                                    chRecyclerView.visibility = View.VISIBLE
                                                }
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                TODO("Not yet implemented")
                                            }

                                        })
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}