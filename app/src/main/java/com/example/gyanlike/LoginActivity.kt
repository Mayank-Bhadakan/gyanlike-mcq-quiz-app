package com.example.gyanlike

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.gyanlike.PreferenceHelper.set
import com.example.gyanlike.databinding.ActivityLoginBinding
import com.example.gyanlike.util.Pref
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : CommonActivity() , View.OnClickListener {

    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()

        }

    private fun initView() {
        binding.edtLEmail.setOnClickListener(this)
        binding.edtLPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.txtSignup.setOnClickListener(this)
        binding.txtForgotPass.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()

    }

    override fun onClick(view: View) {
        when(view.id){
            R.id.btnLogin -> {
                login()
            }
            R.id.txtSignup -> {
                startActivity(Intent(this , SignUPActivity::class.java))
            }
            R.id.txtForgotPass -> {
                startActivity(Intent(this, ForgotPassActivity::class.java))
            }
        }
    }

    private fun login() {
        val email = binding.edtLEmail.text.toString()
        val pass = binding.edtLPassword.text.toString()

                sharedPreferences[USEREMAIL] = email
                sharedPreferences[USERPASSWORD] = pass
                sharedPreferences[USER_LOGIN] = true

        // check pass
        if (email.isBlank() || pass.isBlank())
        {
            Toast.makeText(this , "Email or Password can't be blank" , Toast.LENGTH_SHORT).show()
            return
        }
        else
        {
            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                auth.signInWithEmailAndPassword(email , pass).addOnCompleteListener(this){
                    if (it.isSuccessful)
                    {
                        Toast.makeText(this , "Successfully Login", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, HomeActivity::class.java))

                        // admin Login
                        val adminEmail = "mayank8758170151@gmail.com"
                        val RAdminEmail = "ULearn612@gmail.com"
                     //   val adminpass = "dvija@123"

                        if (email == adminEmail || email == RAdminEmail)
                        {
                            Pref.setIntValue(Pref.PREF_USER_TYPE, 3)           // admin is 3

                            sharedPreferences[ADMINEMAIL] = adminEmail
                         //   sharedPreferences[ADMINPASSWORD] = adminpass
                            sharedPreferences[ADMIN_LOGIN] = true
                            sharedPreferences[USER_LOGIN] = false

                            startActivity(Intent(this , AdminStd::class.java))
                            Toast.makeText(this, "Admin Login Successfully" , Toast.LENGTH_SHORT).show()
                        }
                        else
                        {
                            Pref.setIntValue(Pref.PREF_USER_TYPE, 2)     // user is 2
                            sharedPreferences[USER_LOGIN] = true
                        }

                    }
                    else
                    {
                        Toast.makeText(this , "Enter Valid Email-ID and Password" , Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else {
                Toast.makeText(this , "Enter Valid Email-ID " , Toast.LENGTH_SHORT).show()
            }

        }
    }
}
