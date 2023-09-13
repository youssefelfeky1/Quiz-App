package com.example.earningapp.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.earningapp.adapter.HistoryAdapter
import com.example.earningapp.databinding.FragmentHistoryBinding
import com.example.earningapp.model.History
import com.example.earningapp.model.User
import com.example.earningapp.ui.fragment.Withdrawal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.example.earningapp.R

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var listHistory = ArrayList<History>()
    private lateinit var adapter:HistoryAdapter
    private lateinit var mDbRef:DatabaseReference
    private lateinit var mAuth:FirebaseAuth
    private lateinit var storageRef: StorageReference

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAuth=FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().reference
        storageRef =  FirebaseStorage.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDbRef.child("playerCoinHistory").child(mAuth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    listHistory.clear()
                    for (dataSnapShot in snapshot.children)
                    {
                        val data = dataSnapShot.getValue(History::class.java)
                        listHistory.add(data!!)


                    }
                    listHistory.reverse()
                    adapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        storageRef.child(mAuth.currentUser!!.uid).downloadUrl.addOnSuccessListener {

            Glide.with(requireActivity()).load(it).
            apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.profileImgView)

        }.addOnFailureListener{
            binding.profileImgView.setImageResource(R.drawable.avtar)
        }

        binding=FragmentHistoryBinding.inflate(layoutInflater)
        adapter= HistoryAdapter(listHistory)
        binding.historyRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter=adapter
        binding.historyRecyclerView.setHasFixedSize(true)


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

        mDbRef.child("playerCoin").child(mAuth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialogFragment:BottomSheetDialogFragment  = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinWithdrawal1.setOnClickListener {
            val bottomSheetDialogFragment:BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(requireActivity().supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }
    }


}