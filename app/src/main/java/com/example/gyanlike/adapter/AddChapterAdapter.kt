package com.example.gyanlike.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.MCQListActivity
import com.example.gyanlike.Model.ChapterModel
import com.example.gyanlike.databinding.ItemChapterBinding
import com.example.gyanlike.util.Pref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddChapterAdapter (var context: Context , var chModel: ArrayList<ChapterModel>)
    : RecyclerView.Adapter<AddChapterAdapter.ViewHolder>() {

        lateinit var db: FirebaseDatabase

    class ViewHolder (var binding: ItemChapterBinding):RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): AddChapterAdapter.ViewHolder {
        return ViewHolder(ItemChapterBinding.inflate(LayoutInflater.from(context) , parent , false))
    }

    override fun onBindViewHolder(holder: AddChapterAdapter.ViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance()
        val currentItem = chModel[position]

        holder.binding.itemCh.text = chModel[position].chapterName
        holder.binding.itemCh.tag = position

        holder.binding.chapter.setOnClickListener {

            val intent = Intent(context , MCQListActivity::class.java)
            intent.putExtra("CHNAME" , chModel[position].chapterName)
            intent.putExtra("CHID" , chModel[position].id)
            context.startActivity(intent)
        }

        holder.binding.chapter.setOnLongClickListener {

            if (Pref.getIntValue(Pref.PREF_USER_TYPE,3) == 3) {

                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete Item Permanently")
                    .setMessage("Are you sure you want to delete ${currentItem.chapterName} ?")
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

                                                    for (SubRandomKeySnapShot in snapshot.children) {
                                                        val subRendomKey = SubRandomKeySnapShot.key

                                                        db.getReference().child("mcq").child(randomKey)
                                                            .child("subject").child(subRendomKey!!)
                                                            .child("chapter").addListenerForSingleValueEvent(object :
                                                                ValueEventListener {
                                                                override fun onDataChange(snapshot: DataSnapshot) {

                                                                    for (chRandomKeySnapShot in snapshot.children) {
                                                                        val chRandomKey =
                                                                            chRandomKeySnapShot.key

                                                                        for (chSnapShot in chRandomKeySnapShot.children) {
                                                                            // val chValue = chSnapShot.getValue(String::class.java)
                                                                            val chValue =
                                                                                chSnapShot.value

                                                                            if (chValue == currentItem.chapterName && chRandomKey == currentItem.id) {

                                                                                chRandomKeySnapShot.ref.removeValue()
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
        return chModel.size
    }
}