package com.example.earningapp.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toIcon
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.earningapp.R
import com.example.earningapp.databinding.FragmentProfileBinding
import com.example.earningapp.model.User
import com.example.earningapp.ui.activity.SignInActivity
import com.example.earningapp.ui.activity.SignUpActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class ProfileFragment : Fragment() {

    private val binding by lazy {
        FragmentProfileBinding.inflate(layoutInflater)
    }

    private var isExpand = true
    private lateinit var mDbRef:DatabaseReference
    private lateinit var mAuth:FirebaseAuth
    private lateinit var storageRef:StorageReference


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mAuth=FirebaseAuth.getInstance()
        mDbRef= FirebaseDatabase.getInstance().reference
        storageRef =  FirebaseStorage.getInstance().reference
    }
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
        binding.logoutBtn.setOnClickListener {
            mAuth.signOut()
            activity!!.finish()
            startActivity(Intent(requireContext(),SignInActivity::class.java))


        }
        binding.editImg.setOnClickListener {
            selectAndUploadPhotoToFirebaseStorage()
        }

        storageRef.child(mAuth.currentUser!!.uid).downloadUrl.addOnSuccessListener {

            Glide.with(requireActivity()).load(it).
            apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(binding.profileImg)

        }.addOnFailureListener{
            binding.profileImg.setImageResource(R.drawable.avtar)
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



        return binding.root
    }

    private fun selectAndUploadPhotoToFirebaseStorage() {

        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"

        startActivityForResult(galleryIntent, IMAGE_PICK_CODE)
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = data?.data

            if (selectedImageUri != null) {
                // Upload the selected image to Firebase Storage
                uploadImageToFirebaseStorage(selectedImageUri)
            } else {
                Toast.makeText(requireContext(), "Failed to select image", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun uploadImageToFirebaseStorage(imageUri:Uri) {

        val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child(mAuth.currentUser!!.uid)

        storageRef.putFile(imageUri ).addOnSuccessListener {
            // Image uploaded successfully
            Toast.makeText(activity, "Image uploaded successfully", Toast.LENGTH_SHORT).show()

            Glide.with(requireActivity()).load(imageUri)
                .into(binding.profileImg)

        }.addOnFailureListener { e ->
            // Handle the error
            Toast.makeText(activity, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private  val IMAGE_PICK_CODE = 1001
}