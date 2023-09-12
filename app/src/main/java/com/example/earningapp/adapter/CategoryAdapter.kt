package com.example.earningapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.earningapp.databinding.CategoryitemBinding
import com.example.earningapp.model.Category
import com.example.earningapp.ui.activity.QuizActivity
import com.example.earningapp.ui.fragment.HomeFragment

class CategoryAdapter(private var categoryList:ArrayList<Category>, private val context:Context):RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(var binding: CategoryitemBinding):RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
       return CategoryViewHolder(CategoryitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val dataList =categoryList[position]
        holder.binding.categoryImgView.setImageResource(dataList.catImage)
        holder.binding.categoryTxtView.text=dataList.catText

        holder.binding.categoryBtn.setOnClickListener{
            val intent = Intent(context,QuizActivity::class.java)
            intent.putExtra("categoryImg",dataList.catImage)
            intent.putExtra("questionType",dataList.catText)
            context.startActivity(intent)
        }
    }
}