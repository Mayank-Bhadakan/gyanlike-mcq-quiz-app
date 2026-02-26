package com.example.gyanlike.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.Model.ChapterModel
import com.example.gyanlike.Model.MCQListModel
import com.example.gyanlike.QuestionsActivity
import com.example.gyanlike.databinding.ItemMcqListBinding
import com.example.gyanlike.util.Pref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddMCQAdapter (var context: Context, var model: ArrayList<MCQListModel>)
    :RecyclerView.Adapter<AddMCQAdapter.ViewHolder>() {

        lateinit var db: FirebaseDatabase

    class ViewHolder (var binding: ItemMcqListBinding):RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMcqListBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance()
        var currentItem = model[position]

        var chmodel= ArrayList<ChapterModel>()

        holder.binding.itemMcqlist.text = model[position].question
        holder.binding.itemMcqlist.tag = position

        holder.binding.mcqlist.setOnClickListener {

            val intent = Intent(context, QuestionsActivity::class.java)
            intent.putExtra("QUENAME" , model[position].question)
            intent.putExtra("QUEID" , model[position].id)
            intent.putExtra("CHID" , model[position].chId)
            intent.putExtra("OPTIONA", model[position].a)
            intent.putExtra("OPTIONB", model[position].b)
            intent.putExtra("OPTIONC", model[position].c)
            intent.putExtra("OPTIOND", model[position].d)
            intent.putExtra("ANS", model[position].ans)
            context.startActivity(intent)

        }

        holder.binding.mcqlist.setOnLongClickListener {

            if (Pref.getIntValue(Pref.PREF_USER_TYPE,3) == 3) {

                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete Item Permanently")
                    .setMessage("Are you sure you want to delete this question ?")
                    .setPositiveButton("Yes") { _, _ ->

                        db.getReference().child("mcq")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {

                                    for (randomKeySnapShot in snapshot.children) {
                                        val randomKey = randomKeySnapShot.key

                                        db.getReference().child("mcq").child(randomKey!!).child("subject")
                                            .addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {

                                                    for (subRandomKeySnapShot in snapshot.children) {
                                                        val subRandomKey = subRandomKeySnapShot.key

                                                        db.getReference().child("mcq").child(randomKey!!)
                                                            .child("subject").child(subRandomKey!!)
                                                            .child("chapter")
                                                            .addListenerForSingleValueEvent(object :
                                                                ValueEventListener {
                                                                override fun onDataChange(snapshot: DataSnapshot) {

                                                                    for (chRandomKeySnapShot in snapshot.children) {
                                                                        val chRandomKey =
                                                                            chRandomKeySnapShot.key

                                                                        db.getReference().child("mcq").child(randomKey)
                                                                            .child("subject").child(subRandomKey)
                                                                            .child("chapter").child(chRandomKey!!)
                                                                            .child("question")
                                                                            .addListenerForSingleValueEvent(
                                                                                object :
                                                                                    ValueEventListener {
                                                                                    override fun onDataChange(
                                                                                        snapshot: DataSnapshot
                                                                                    ) {

                                                                                        for (queRandomKeySnapShot in snapshot.children) {
                                                                                            val queRandomKey =
                                                                                                queRandomKeySnapShot.key

                                                                                            for (queSnapShot in queRandomKeySnapShot.children) {
                                                                                                val queValue =
                                                                                                    queSnapShot.value

                                                                                                if (queValue == currentItem.question && queRandomKey == currentItem.id) {

                                                                                                    queRandomKeySnapShot.ref.removeValue()
                                                                                                        .addOnSuccessListener {
                                                                                                            Toast.makeText(context,"Item Removed Successfully",Toast.LENGTH_SHORT).show()
                                                                                                        }
                                                                                                        .addOnFailureListener { error ->
                                                                                                            Toast.makeText(context,"error ${error.message}",Toast.LENGTH_SHORT).show()
                                                                                                        }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }

                                                                                    override fun onCancelled(error: DatabaseError) {
                                                                                        Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                                                                                    }

                                                                                })

                                                                    }
                                                                }

                                                                override fun onCancelled(error: DatabaseError) {
                                                                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                                                                }

                                                            })

                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(context,"error",Toast.LENGTH_SHORT).show()
                                                }

                                            })
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show()
                                }

                            })
                    }

                    .setNegativeButton("No") { _, _ ->
                        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show()
                    }
                    .show()
            }

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return model.size
    }


}