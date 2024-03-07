package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.databinding.CategoryItemPreviewBinding
import com.example.newsapp.data.model.Category
import com.example.newsapp.databinding.ItemArticlePreviewBinding

class CategoriesAdapter  (
    private val categories: List<Category>,
    private val onCategoryClickListener: OnCategoryClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {


    inner class CategoryViewHolder(val binding: CategoryItemPreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.categoryCard.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val category = categories[position]
                    onCategoryClickListener.onItemClick(category)
                }
            }
        }
    }




    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoriesAdapter.CategoryViewHolder {
        val view = CategoryItemPreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoriesAdapter.CategoryViewHolder, position: Int) {
        val category= categories[position]
        holder.binding.apply {
            name.text= category.name
            imageView2.setImageResource(category.img)
        }
    }

}