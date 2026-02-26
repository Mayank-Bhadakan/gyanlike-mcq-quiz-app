package com.example.gyanlike

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.children
import com.example.gyanlike.Model.MCQListModel
import com.example.gyanlike.databinding.ActivityUploadMcqactivityBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UploadMCQActivity : CommonActivity() {

    lateinit var binding: ActivityUploadMcqactivityBinding
    lateinit var db: FirebaseDatabase
    lateinit var dbref: DatabaseReference
    lateinit var progress: ProgressDialog
    lateinit var mcqListModel: MCQListModel

    var model = ArrayList<MCQListModel>()
    lateinit var answer: EditText
    lateinit var radioButton: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadMcqactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()

        initView()

        progress = ProgressDialog(this)
        progress.setTitle("Uploading")
        progress.setMessage("Please wait..!")

    }

    private fun initView() {

        binding.customHeader.setTitle(R.string.chapter)

        val id = intent.getStringExtra("CHID")
        val chName = intent.getStringExtra("CHNAME")
        binding.txtchtitle.text =  chName

        binding.upload.setOnClickListener {
            progress.show()

//            var question = binding.edtAque.text.toString().trim()
//            var optionA = binding.edtAoption.text.toString().trim()
//            var optionB = binding.edtBoption.text.toString().trim()
//            var optionC = binding.edtCoption.text.toString().trim()
//            var optionD = binding.edtDoption.text.toString().trim()

            var correct = -1

            for (i in 0 until binding.optionContainer.childCount )
            {
                 answer = binding.answerContainer.getChildAt(i) as EditText

                if (answer.text.toString().isEmpty())
                {
                    answer.setError("Required")
                    return@setOnClickListener
                }

                 radioButton = binding.optionContainer.getChildAt(i) as RadioButton

                if (radioButton.isChecked){
                    correct = i
                    break
                }
            }

            if ( correct == -1){
                Toast.makeText(this , "Please mark correct option" , Toast.LENGTH_SHORT).show()
            }

            var randomKeyGenerate = db.reference.push().key

            var que = binding.edtAque.text.toString().trim()
            var A = (binding.answerContainer.getChildAt(0) as EditText).text.toString().trim()
            var B = (binding.answerContainer.getChildAt(1) as EditText).text.toString().trim()
            var C = (binding.answerContainer.getChildAt(2) as EditText).text.toString().trim()
            var D = (binding.answerContainer.getChildAt(3) as EditText).text.toString().trim()
            var ans = (binding.answerContainer.getChildAt(correct) as EditText).text.toString().trim()

//            var ans = (binding.optionContainer.getChildAt(correct) as RadioButton).text.toString().trim()

            mcqListModel = MCQListModel(id = randomKeyGenerate.toString(), chId = id.toString(), question = que, a = A, b = B, c = C, d = D, ans = ans)

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

                                                            db.getReference().child("mcq").child(randomKey)
                                                                .child("subject").child(subRandomKey)
                                                                .child("chapter").child(id!!)
                                                                .child("question").child(randomKeyGenerate!!).setValue(mcqListModel)
                                                                .addOnSuccessListener {
                                                                    Toast.makeText(this@UploadMCQActivity, "Data Uploaded", Toast.LENGTH_SHORT).show()
                                                                    progress.dismiss()
                                                                }
                                                                .addOnFailureListener {
                                                                    Toast.makeText(this@UploadMCQActivity , "Data Not Uploaded" , Toast.LENGTH_SHORT).show()
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
                                    TODO("Not yet implemented")
                                }

                            })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

            binding.edtAque.text!!.clear()
            binding.edtAoption.text!!.clear()
            binding.edtBoption.text!!.clear()
            binding.edtCoption.text!!.clear()
            binding.edtDoption.text!!.clear()

        }

    }
}