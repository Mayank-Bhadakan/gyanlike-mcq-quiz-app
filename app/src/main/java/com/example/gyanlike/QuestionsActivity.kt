package com.example.gyanlike

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TimeInterpolator
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gyanlike.Model.MCQListModel
import com.example.gyanlike.databinding.ActivityQuestionsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.protobuf.Value

class QuestionsActivity : CommonActivity() {

    lateinit var binding: ActivityQuestionsBinding
    lateinit var db: FirebaseDatabase
    lateinit var dbref: DatabaseReference
    var list = ArrayList<MCQListModel>()
//     lateinit var selectedOption: Button
    var position = 0
    var count = 0

//    var list = mutableListOf<Any>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance()
        initView()

    }

    private fun initView() {

        val id = intent.getStringExtra("QUEID")
        val name = intent.getStringExtra("QUENAME")
        val optionA = intent.getStringExtra("OPTIONA")
        val optionB = intent.getStringExtra("OPTIONB")
        val optionC = intent.getStringExtra("OPTIONC")
        val optionD = intent.getStringExtra("OPTIOND")
        val ans = intent.getStringExtra("ANS")

//        binding.que.text = name.toString()
//        binding.optionA.text = optionA.toString()
//        binding.optionB.text = optionB.toString()
//        binding.optionC.text = optionC.toString()
//        binding.optionD.text = optionD.toString()

        val chId = intent.getStringExtra("CHID")

        db.getReference().child("mcq").addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                for (randomKeySnapShot in snapshot.children){
                    val randomKey = randomKeySnapShot.key

                    db.getReference().child("mcq").child(randomKey!!).child("subject").addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (subRandomKeySnapShot in snapshot.children){
                                val subRandomKey = subRandomKeySnapShot.key

                                db.getReference().child("mcq").child(randomKey).child("subject").child(subRandomKey!!)
                                    .child("chapter").addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {

                                            for (chRandomKeySnapShot in snapshot.children){
                                                val chRandomKey = chRandomKeySnapShot.key

                                                if (chRandomKey == chId) {

                                                    dbref = FirebaseDatabase.getInstance().getReference().child("mcq").child(randomKey)
                                                        .child("subject").child(subRandomKey).child("chapter").child(chId!!)
                                                        .child("question")

                                                    dbref.addValueEventListener(object :ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {

                                                            list.clear()

                                                            if (snapshot.exists()){

                                                                for (queSnap in snapshot.children){

                                                                    val queValue = queSnap.getValue(MCQListModel::class.java)
                                                                    list.add(queValue!!)

                                                                }

                                                                if (list.size > 0){

                                                                    for (i in 0 until 4){

                                                                        binding.optionContainer.getChildAt(i).setOnClickListener {view->
                                                                             checkAns(view as Button)
                                                                        }
                                                                    }

                                                                    playAnimation(binding.que, 0, list[position].question)

                                                                    binding.btnNext.setOnClickListener {

                                                                        binding.btnNext.isEnabled = false
                                                                        binding.btnNext.alpha = 0.3f
                                                                        
                                                                        enableOption(true)
                                                                        position ++

                                                                        if (position < list.size) {
                                                                            playAnimation(binding.que, 0, list[position].question)
                                                                            playAnimation(binding.optionA, 0, list[position].a)
                                                                            playAnimation(binding.optionB, 0, list[position].b)
                                                                            playAnimation(binding.optionC, 0, list[position].c)
                                                                            playAnimation(binding.optionD, 0, list[position].d)
                                                                        }
                                                                        else{
                                                                            Toast.makeText(this@QuestionsActivity, "All mcqs are done", Toast.LENGTH_SHORT).show()
                                                                            finish()
                                                                        }

                                                                    }
                                                                }

                                                                else{
                                                                    Toast.makeText(this@QuestionsActivity, "Question not exist", Toast.LENGTH_SHORT).show()
                                                                }
                                                            }

                                                            else{
                                                                Toast.makeText(this@QuestionsActivity, "error", Toast.LENGTH_SHORT).show()
                                                            }

                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            Toast.makeText(this@QuestionsActivity, "error below", Toast.LENGTH_SHORT).show()
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

    private fun enableOption(enable: Boolean) {

        for (i in 0 until 4){

            binding.optionContainer.getChildAt(i).isEnabled = enable

            if (enable){
                binding.optionContainer.getChildAt(i).setBackgroundResource(R.drawable.btn_option)
            }
        }
    }

    private fun playAnimation(view: View , value: Int, data: String ) {

        view.animate().alpha(value.toFloat()).scaleX(value.toFloat()).scaleY(value.toFloat())
            .setDuration(500).setStartDelay(100)
            .setInterpolator(DecelerateInterpolator())
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(p0: Animator) {

                    if (value == 0 && count<4){

                        var option: String = ""

                        if (count == 0){ option = list[position].a }
                        else if (count == 1) { option = list[position].b }
                        else if (count == 2) { option = list[position].c }
                        else if (count == 3) { option = list[position].d }

                        playAnimation(binding.optionContainer.getChildAt(count), 0, option)
                        count++
                    }
                }

                @SuppressLint("SetTextI18n")
                override fun onAnimationEnd(p0: Animator) {

                    if (value == 0){
                        try {
                            (view as TextView).text = data
                        }

                        catch (e: Exception){
                            (view as Button).text = data
                        }

                        view.tag = data
                        playAnimation(view,1, data)
                    }
                }

                override fun onAnimationCancel(p0: Animator) {
                    TODO("Not yet implemented")
                }

                override fun onAnimationRepeat(p0: Animator) {
                    TODO("Not yet implemented")
                }

            })

    }

    private fun checkAns(selectedOption: Button) {

        enableOption(false)

        binding.btnNext.isEnabled = true
        binding.btnNext.alpha = 1f

        if (selectedOption.text.toString().equals(list[position].ans)){

            selectedOption.setBackgroundResource(R.drawable.right_answer)
        }

        else{
            selectedOption.setBackgroundResource(R.drawable.wrong_answer)

            var correctOption: Button = binding.optionContainer.findViewWithTag(list[position].ans)
            correctOption.setBackgroundResource(R.drawable.right_answer)
        }
    }




}