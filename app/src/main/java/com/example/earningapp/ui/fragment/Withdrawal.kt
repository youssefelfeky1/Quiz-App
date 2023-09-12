package com.example.earningapp.ui.fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.databinding.FragmentWithdrawalBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Withdrawal : BottomSheetDialogFragment() {
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val binding by lazy {
        FragmentWithdrawalBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDbRef.child("playerCoin").child(mAuth.currentUser!!.uid).addValueEventListener(
            object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        binding.coins.text = snapshot.value.toString()

                    }
                    else{
                        binding.coins.text ="0"
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )
    }

}