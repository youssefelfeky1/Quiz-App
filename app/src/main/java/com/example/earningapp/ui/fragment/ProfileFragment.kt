package com.example.earningapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class ProfileFragment : Fragment() {

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private var isExpand = true
    private val mDbRef:DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth:FirebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding.imageButton.setOnClickListener {
            if(isExpand)
            {
                binding.expandableLayout.visibility=View.VISIBLE
                binding.imageButton.setImageResource(R.drawable.arrowup)
            }
            else{
                binding.expandableLayout.visibility=View.GONE
                binding.imageButton.setImageResource(R.drawable.downarrow)

            }
            isExpand = !isExpand
        }

        mDbRef.child("Users").child(mAuth.currentUser!!.uid)
            .addValueEventListener(

            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {

                       val user = snapshot.getValue<User>()

                       binding.name.text = user!!.name
                       binding.myNameTxtView.text = user.name
                       binding.age.text = user.age
                       binding.email.text = user.email
                       binding.password.text = user.password

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


        // Inflate the layout for this fragment
        return binding.root
    }


}