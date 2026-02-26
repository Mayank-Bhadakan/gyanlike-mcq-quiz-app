package com.example.gyanlike.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.AdminChapterActivity
import com.example.gyanlike.AdminSubjectActivity
import com.example.gyanlike.Model.AddStdModel
import com.example.gyanlike.Model.SubjectModel
import com.example.gyanlike.databinding.ItemSubBinding
import com.example.gyanlike.util.Pref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AddSubAdapter (var context: Context, var model: ArrayList<SubjectModel> )
    :RecyclerView.Adapter<AddSubAdapter.ViewHolder>(){

        lateinit var db : FirebaseDatabase

    class ViewHolder ( var binding: ItemSubBinding):RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemSubBinding.inflate(LayoutInflater.from(context) , parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance()
        val currentItem = model[position]

        holder.binding.itemSub.text = model[position].subjectname
        holder.binding.itemSub.tag = position



        holder.binding.sub.setOnClickListener {

            val intent = Intent(context , AdminChapterActivity::class.java)
            intent.putExtra("STDSUB" , model[position].subjectname)
            intent.putExtra("SUBID", model[position].id)
            context.startActivity(intent)
        }

        holder.binding.sub.setOnLongClickListener {

            if (Pref.getIntValue(Pref.PREF_USER_TYPE,3) == 3) {

                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Delete Item Permanently")
                    .setMessage("Are you sure you want to delete ${currentItem.subjectname} ?")
                    .setPositiveButton("Yes") { _, _ ->

                        db.getReference().child("mcq")
                            .addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    // mcq -> id
                                    for (randomKeySnapShot in snapshot.children) {
                                        val randomkey = randomKeySnapShot.key

                                        db.getReference().child("mcq").child(randomkey!!)
                                            .child("subject")
                                            .addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                @SuppressLint("NotifyDataSetChanged")
                                                override fun onDataChange(subsnapshot: DataSnapshot) {

                                                    // mcq -> id -> subject -> id
                                                    for (stdrandomkeySnapshot in subsnapshot.children) {
                                                        val stdrandomKey = stdrandomkeySnapshot.key

                                                        if (stdrandomKey == currentItem.id) {
                                                            //mcq -> id -> subject -> id -> subjectName
                                                            for (subSnap in stdrandomkeySnapshot.children) {
                                                                //  val subValue = subSnap.getValue(String::class.java)
                                                                val subValue = subSnap.value

                                                                if (subValue == currentItem.subjectname) {
                                                                    stdrandomkeySnapshot.ref.removeValue()
                                                                        .addOnSuccessListener {Toast.makeText(context,"Item Removed Successfully",Toast.LENGTH_SHORT).show()
                                                                        }
                                                                        .addOnFailureListener { error ->
                                                                            Toast.makeText(context,"error ${error.message}",Toast.LENGTH_SHORT).show()
                                                                        }
                                                                    notifyDataSetChanged()
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