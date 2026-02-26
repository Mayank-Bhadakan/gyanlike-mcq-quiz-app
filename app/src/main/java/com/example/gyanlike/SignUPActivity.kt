package com.example.gyanlike

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.PatternMatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.gyanlike.PreferenceHelper.set
import com.example.gyanlike.Model.SignUpModel
import com.example.gyanlike.Model.UserModel
import com.example.gyanlike.databinding.ActivitySignUpactivityBinding
import com.example.gyanlike.util.Pref
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

class SignUPActivity : CommonActivity() , View.OnClickListener {

    lateinit var binding: ActivitySignUpactivityBinding
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // user is 2
        Pref.getIntValue(Pref.PREF_USER_TYPE, 2)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        initView()

        }

    private fun initView() {
        binding.edtName.setOnClickListener(this)
        binding.edtEmail.setOnClickListener(this)
        binding.edtPassword.setOnClickListener(this)
        binding.btnSignup.setOnClickListener(this)
        binding.txtLogin.setOnClickListener(this)
    }


    override fun onClick(view: View) {
        when(view.id){
            R.id.btnSignup -> {
                signUpUser()
            }
            R.id.txtLogin -> {
                startActivity(Intent(this , LoginActivity::class.java))
            }
        }
    }


    private fun signUpUser() {
        val name = binding.edtName.text.toString().trim()
        val email = binding.edtEmail.text.toString().trim()
        val pass = binding.edtPassword.text.toString().trim()


    // check pass & validation of email
        if (email.isBlank() || pass.isBlank())
        {
            Toast.makeText(this , "Email or Password can't be blank" , Toast.LENGTH_SHORT).show()
            return
        }
        else {
            
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                auth.createUserWithEmailAndPassword(email , pass).addOnCompleteListener(this) { task->
                    if (task.isSuccessful) {

                        val userId = task.result?.user?.uid
                        val signUpModel = SignUpModel(name = name , email = email)

                        val dialog: ProgressDialog
                        dialog = ProgressDialog(this)
                        dialog.setMessage("Creating New Account....")

                        dialog.show()

                        db.collection("user").document(userId!!).set(signUpModel).addOnCompleteListener { p0 ->
                            if (p0.isSuccessful) {
                                dialog.dismiss()

                                Pref.setIntValue(Pref.PREF_USER_TYPE, 2)
                                startActivity(Intent(this, HomeActivity::class.java))
                                Toast.makeText(this, "Successfully Sign Up", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                Toast.makeText(this, "Failed data added...!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }

                        // save name in userdata model firebase
                        val usermodel = UserModel(name = name)

                        db.collection("user").document(userId!!).collection("user-info").document("userdata")
                            .set(usermodel).addOnCompleteListener { }
//                        finish()
                    }
                    else
                    {
                        Toast.makeText(this, "Already registered this Email ID please Login" , Toast.LENGTH_SHORT).show()
                    }

                }

            }
            else{
                Toast.makeText(this , "Enter valid Email-ID" , Toast.LENGTH_SHORT).show()
            }
        }

    }
}


