package com.example.earningapp.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.earningapp.R
import com.example.earningapp.adapter.CategoryAdapter
import com.example.earningapp.databinding.FragmentHomeBinding
import com.example.earningapp.model.Category
import com.example.earningapp.model.User
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth:FirebaseAuth= FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

         val categoryList = ArrayList<Category>()

        categoryList.add(Category(R.drawable.scince,"science"))
        categoryList.add(Category(R.drawable.englishs,"english"))
        categoryList.add(Category(R.drawable.historyimg,"history"))
        categoryList.add(Category(R.drawable.mathmetic,"mathematics"))
        binding.categoryRecyclerView.layoutManager=GridLayoutManager(requireContext(),2)
        val adapter = CategoryAdapter(categoryList,requireContext())
        binding.categoryRecyclerView.adapter=adapter
        binding.categoryRecyclerView.setHasFixedSize(true)

    }


}