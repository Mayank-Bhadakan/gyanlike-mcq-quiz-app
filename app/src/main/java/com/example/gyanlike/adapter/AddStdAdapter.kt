package com.example.gyanlike.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.gyanlike.AdminChapterActivity
import com.example.gyanlike.AdminStd
import com.example.gyanlike.AdminSubjectActivity
import com.example.gyanlike.MainActivity
import com.example.gyanlike.Model.AddStdModel
import com.example.gyanlike.Model.SubjectModel
import com.example.gyanlike.databinding.ItemStdBinding
import com.example.gyanlike.util.Pref
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Objects

class AddStdAdapter (var context: Context, var model: ArrayList<AddStdModel> )
    :RecyclerView.Adapter<AddStdAdapter.ViewHolder>() {

    lateinit var db : FirebaseDatabase
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth

    class ViewHolder (var binding: ItemStdBinding):RecyclerView.ViewHolder(binding.root) {    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemStdBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        db = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        val currentItem = model[position]

        holder.binding.itemStd.text = model[position].stdName
        holder.binding.itemStd.tag = position
        holder.binding.std.setOnClickListener {

            val intent = Intent(context , AdminSubjectActivity::class.java)
            intent.putExtra("STDID", model[position].stdName )
            intent.putExtra("ID", model[position].id)
            context.startActivity(intent)

        }

            holder.binding.std.setOnLongClickListener {

                if (Pref.getIntValue(Pref.PREF_USER_TYPE,3) == 3){

                    MaterialAlertDialogBuilder(holder.itemView.context)
                        .setTitle("Delete Item Permanently")
                        .setMessage("Are you sure you want to delete ${currentItem.stdName} ?")
                        .setPositiveButton("Yes") { _, _ ->
                            val firebaseRef = db.getReference().child("mcq")

                            firebaseRef.addListenerForSingleValueEvent(object : ValueEventListener {

                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    for (rendomKeySnapshot in dataSnapshot.children) {
                                        val rendomKey = rendomKeySnapshot.key

                                        for (itemsnapshot in rendomKeySnapshot.children) {
                                            // val itemValue = itemsnapshot.getValue(String::class.java)
                                            val itemValue = itemsnapshot.value

                                            if (itemValue == currentItem.stdName) {
                                                rendomKeySnapshot.ref.removeValue()
                                                    .addOnSuccessListener {
                                                        Toast.makeText(context,"Item removed successfully",Toast.LENGTH_SHORT).show()
                                                    }
                                                    .addOnFailureListener { error ->
                                                        Toast.makeText(context,"error ${error.message}",Toast.LENGTH_SHORT).show()
                                                    }
                                            }
                                        }

                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                }
                            })

                        }
                        .setNegativeButton("No") { _, _ ->
                            Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show()
                        }
                        .show()

                }

                return@setOnLongClickListener true
            }

    }

    override fun getItemCount(): Int {
        return model.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addData(mObjList: ArrayList<AddStdModel>){
        model = ArrayList()
        model.addAll(mObjList)

        notifyDataSetChanged()
    }
}