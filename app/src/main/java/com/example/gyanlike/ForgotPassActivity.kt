package com.example.gyanlike

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.gyanlike.databinding.ActivityForgotPassBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : CommonActivity(), View.OnClickListener {

    lateinit var binding: ActivityForgotPassBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        initView()

        }

    private fun initView() {
        // Set header
        binding.customHeader.setTitle(R.string.forgot_password)
        binding.btnSubmit.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnSubmit -> {
                fpass()
            }
        }
    }

    private fun fpass() {
        val email =binding.edtFEmail.text.toString().trim()

        // validation
        if (email.isBlank())
        {
            Toast.makeText(this , "Email can't be blank", Toast.LENGTH_SHORT).show()
            return
        }
        else{

            if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            {
                auth.sendPasswordResetEmail(email).addOnCompleteListener(this){
                    if (it.isSuccessful)
                    {
                        Toast.makeText(this, "Successfully send" , Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        Toast.makeText(this, "Failed" , Toast.LENGTH_SHORT).show()
                    }
                }

            }
            else{
                Toast.makeText(this, "Enter valid Email-Id", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
