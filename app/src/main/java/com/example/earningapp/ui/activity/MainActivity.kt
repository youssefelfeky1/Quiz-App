package com.example.earningapp.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.earningapp.databinding.ActivityMainBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var  mAuth:FirebaseAuth
    private lateinit var mDbRef:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        mAuth= FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().reference

        binding.signupBtn.setOnClickListener {
          signup()
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




    private fun signup(){

        val name = binding.nameEdtTxt.text.toString()
        val age = binding.ageEdtTxt.text.toString()
        val email = binding.emailEdtTxt.text.toString()
        val password = binding.passwordEdtTxt.text.toString()

        if (name.isEmpty() ||
            age.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty())
        {
            Toast.makeText(this,"Please fill all data",Toast.LENGTH_SHORT).show()
        }else{
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                if(it.isSuccessful){
                    val user = User(name,age,email,password)
                    mDbRef.child("Users").child(mAuth.currentUser!!.uid).setValue(user).addOnSuccessListener {
                        startActivity(Intent(this,HomeActivity::class.java))
                        finish()
                    }

                }else{
                    Toast.makeText(this,it.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
                }
            }

        }

    }
}