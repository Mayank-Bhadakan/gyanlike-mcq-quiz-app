package com.example.gyanlike

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.snap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.MCQListModel
import com.example.gyanlike.adapter.AddMCQAdapter
import com.example.gyanlike.databinding.ActivityMcqlistBinding
import com.example.gyanlike.util.Pref
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MCQListActivity : CommonActivity() {

    lateinit var binding: ActivityMcqlistBinding
    lateinit var db: FirebaseDatabase
    lateinit var dbref: DatabaseReference
    lateinit var mcqRecyclerView: RecyclerView

    var model = ArrayList<MCQListModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMcqlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()

        initView()

        mcqRecyclerView = findViewById(R.id.AMcqList)
        mcqRecyclerView.layoutManager = LinearLayoutManager(this)
        mcqRecyclerView.setHasFixedSize(true)

    }

    private fun initView() {

        binding.customHeader.setTitle(R.string.chapter)

        val id = intent.getStringExtra("CHID")
        val chName = intent.getStringExtra("CHNAME")
        binding.txtchtitle.text = chName

        if (Pref.getIntValue(Pref.PREF_USER_TYPE,2) == 2){
            binding.btnaddMCQ.visibility = View.GONE
            binding.addimg.visibility = View.GONE
        }

        binding.btnaddMCQ.setOnClickListener {

            var intent = Intent(this , UploadMCQActivity::class.java)
            intent.putExtra("CHID" , id)
            intent.putExtra("CHNAME" , chName)
            this.startActivity(intent)

        }

        fatchData()

    }


    private fun fatchData() {
        val id = intent.getStringExtra("CHID")

        db.getReference().child("mcq").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (randomKeySnapShot in snapshot.children){
                    val randomKey = randomKeySnapShot.key

                    db.getReference().child("mcq").child(randomKey!!).child("subject")
                        .addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {

                                for (subRandomKeySnapShot in snapshot.children){
                                    val subRandomKey = subRandomKeySnapShot.key

                                    db.getReference().child("mcq").child(randomKey).child("subject").child(subRandomKey!!)
                                        .child("chapter").addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {

                                                for (chRandomKeySnapShot in snapshot.children){
                                                    val chRandomKey = chRandomKeySnapShot.key

                                                    if (chRandomKey == id){

                                                        dbref = FirebaseDatabase.getInstance().getReference().child("mcq").child(randomKey)
                                                            .child("subject").child(subRandomKey).child("chapter").child(id!!)
                                                            .child("question")

                                                        dbref.addValueEventListener(object : ValueEventListener{
                                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                                model.clear()

                                                                if (snapshot.exists()){
                                                                    for (queSnap in snapshot.children){
                                                                        val queValue = queSnap.getValue(MCQListModel::class.java)
                                                                        model.add(queValue!!)
                                                                    }

                                                                    val madapter = AddMCQAdapter(this@MCQListActivity , model)

                                                                    mcqRecyclerView.adapter = madapter

                                                                    mcqRecyclerView.visibility = View.VISIBLE
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

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}