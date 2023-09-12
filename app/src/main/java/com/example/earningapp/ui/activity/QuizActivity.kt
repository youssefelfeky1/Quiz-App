package com.example.earningapp.ui.activity

import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.example.earningapp.databinding.ActivityQuizBinding
import com.example.earningapp.model.Question
import com.example.earningapp.model.User
import com.example.earningapp.ui.fragment.Withdrawal
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class QuizActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityQuizBinding.inflate(layoutInflater)
    }

    private  var questionList = ArrayList<Question>()
    private var currentQuestion = 0
    var score = 0
    var currentChance=0L
    private val mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference
    private val mAuth: FirebaseAuth =FirebaseAuth.getInstance()
    companion object{
        private var firstOpen = true
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.categoryImg.setImageResource(intent.getIntExtra("categoryImg",0))
        val catText: String = intent.getStringExtra("questionType")!!


        if (firstOpen){
            //show loading animation
            binding.loadingView.visibility=View.VISIBLE
            //remove loading animation
            Handler(Looper.getMainLooper()).postDelayed({
                binding.loadingView.visibility=View.GONE
            },2000)
            firstOpen = false

        }


        Firebase.firestore.collection("Questions")
            .document(catText).collection("question1").get().addOnSuccessListener {
                questionData->
                questionList.clear()
                for (data in questionData.documents)
                {

                    val question:Question? = data.toObject(Question::class.java)
                    questionList.add(question!!)

                }
                if (questionList.size>0)
                {
                    binding.question.text=questionList.get(currentQuestion).question
                    binding.option1.text=questionList.get(currentQuestion).option1
                    binding.option2.text=questionList.get(currentQuestion).option2
                    binding.option3.text=questionList.get(currentQuestion).option3
                    binding.option4.text=questionList.get(currentQuestion).option4


                }

            }

        mDbRef.child("PlayChance").child(mAuth.currentUser!!.uid).addValueEventListener(
            object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists())
                    {currentChance=snapshot.value as Long}
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }
        )



        binding.coinWithdrawal.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(this.supportFragmentManager,"TEST")
            bottomSheetDialogFragment.enterTransition
        }
        binding.coinWithdrawal1.setOnClickListener {
            val bottomSheetDialogFragment: BottomSheetDialogFragment = Withdrawal()
            bottomSheetDialogFragment.show(this.supportFragmentManager,"TEST")
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

        binding.option1.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option1.text.toString())
        }
        binding.option2.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option2.text.toString())
        }
        binding.option3.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option3.text.toString())
        }
        binding.option4.setOnClickListener {
            nextQuestionAndScoreUpdate(binding.option4.text.toString())
        }

    }



    private fun nextQuestionAndScoreUpdate(s:String) {
        if (s == questionList[currentQuestion].ans)
        {
            score+=10
        }
        currentQuestion++
        if(currentQuestion<questionList.size){
            binding.question.text= questionList[currentQuestion].question
            binding.option1.text= questionList[currentQuestion].option1
            binding.option2.text= questionList[currentQuestion].option2
            binding.option3.text= questionList[currentQuestion].option3
            binding.option4.text= questionList[currentQuestion].option4
        }
        else{
           if(score>=questionList.size*10)
           {
               binding.winView.visibility= View.VISIBLE
               binding.screenLayout.visibility=View.GONE
               mDbRef.child("PlayChance").child(mAuth.currentUser!!.uid).setValue(currentChance+1)

           }
            else
           {binding.sorryView.visibility=View.VISIBLE
               binding.screenLayout.visibility=View.GONE
           }

        }

    }





}