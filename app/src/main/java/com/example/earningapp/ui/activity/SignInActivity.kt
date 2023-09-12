package com.example.earningapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.R
import com.example.earningapp.databinding.ActivitySigninBinding
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivitySigninBinding.inflate(layoutInflater)
    }
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.signInBtn.setOnClickListener{
            val email = binding.nameEdtTxt.text.toString()
            val password = binding.passwordEdtTxt.text.toString()
            signin(email,password)
        }
        binding.signUpBtn.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }
    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser != null)
        {
            startActivity(Intent(this,HomeActivity::class.java))
            finish()
        }
    }

    private fun signin(email:String, password:String){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task->
            if(task.isSuccessful){
                val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this@SignInActivity,"User does not exist",Toast.LENGTH_SHORT).show()
            }
        }

    }
}