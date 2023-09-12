package com.example.earningapp.ui.fragment

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.earningapp.databinding.FragmentSpinBinding
import com.example.earningapp.model.History
import com.example.earningapp.model.User
import com.example.earningapp.ui.fragment.Withdrawal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlin.random.Random


class SpinFragment : Fragment() {

    private lateinit var binding: FragmentSpinBinding
    private lateinit var timer: CountDownTimer
    private val itemTitles = arrayOf("100","Try Again","500","Try Again","200","Try Again")
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth: FirebaseAuth =FirebaseAuth.getInstance()
    private var currentChance = 0L
    private var currentCoin=0L




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSpinBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun showResult(itemTitle:String,spin:Int){
        Toast.makeText(requireContext(),itemTitle,Toast.LENGTH_SHORT).show()
        if(currentChance>0 && spin%2==0){
            mDbRef.child("PlayChance").child(mAuth.currentUser!!.uid).setValue(currentChance-1)
            mDbRef.child("playerCoin").child(mAuth.currentUser!!.uid).setValue(itemTitle.toInt()+currentCoin)
            mDbRef.child("playerCoinHistory").child(mAuth.currentUser!!.uid)
                .push().setValue(History(System.currentTimeMillis().toString(),itemTitle,false))
        }

        binding.spinBtn.isEnabled=true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinWithdrawal1.setOnClickListener {
            val bottomSheetDialogFragment:BottomSheetDialogFragment  = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }

        mDbRef.child("PlayChance").child(mAuth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentChance=snapshot.value as Long
                        binding.spinChances.text = snapshot.value.toString()

                    }
                    else{
                        binding.spinChances.text ="0"
                    }
                    if (currentChance==0L)
                    {binding.spinBtn.isEnabled = false}
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

        mDbRef.child("playerCoin").child(mAuth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        currentCoin=snapshot.value as Long
                        binding.coinWithdrawal1.text = snapshot.value.toString()

                    }
                    else{
                        binding.coinWithdrawal1.text ="0"
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )


        binding.spinBtn.setOnClickListener {
            binding.spinBtn.isEnabled=false

            val spin= Random.nextInt(6)
            val degree = 60f *spin
            timer = object :CountDownTimer(5000,50){
                var rotation =0f

                override fun onTick(millisUntilFinished: Long) {
                    rotation+=5f
                    if(rotation>=degree){
                        rotation=degree
                        showResult(itemTitles[spin],spin)
                        timer.cancel()

                    }
                    binding.ivWheel.rotation=rotation

                }

                override fun onFinish() {}
            }.start()
        }

        mDbRef.child("Users").child(mAuth.currentUser!!.uid)
            .addValueEventListener(

                object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val user = snapshot.getValue<User>()

                        binding.nameTxtView.text = user!!.name


                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                }
            )




    }



    }
