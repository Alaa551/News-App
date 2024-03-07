package com.example.newsapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.ItemArticlePreviewBinding
import com.squareup.picasso.Picasso

class NewsAdapter(
    private val onArticleClickListener: OnArticleClickListener
) : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {


    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val article = oldArticles[position]
                    onArticleClickListener.onItemClick(article)
                }
            }
        }
    }


    private val differ = AsyncListDiffer(this, MyDiffUtil())
    var oldArticles: List<Article>
        get() = differ.currentList
        set(value) = differ.submitList(value)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArticleViewHolder {
        val view = ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(view)
    }

    override fun getItemCount(): Int {
        return oldArticles.size
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.binding.apply {
            val article = oldArticles[position]
            //glide to get image from url
            // ivArticleImage.context allows you to access the context associated with the ImageView,
//            // which is required for certain operations, like loading images with Glide.
//            Glide.with(ivArticleImage.context)
//                .load(article.url)
//                  .placeholder(R.drawable.ic_launcher_foreground) // Placeholder image while loading
////                  .error(R.drawable.error_image) // Error image if loading fails
//                .into(ivArticleImage)

            Picasso.get().load(article.image).into(ivArticleImage)



            tvSource.text = article.source.name
            tvTitle.text = article.title
            tvPublishedAt.text = article.publishedAt
            tvDescription.text = article.description


        }
    }


}