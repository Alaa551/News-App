package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.newsapp.ui.adapter.OnArticleClickListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SavedNewsFragment : BaseFragment<NewsViewModel>(), OnArticleClickListener {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSavedNewsBinding
    private val TAG= "SavedNewsFragment"


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(binding.rvSavedNews)


        viewModel.getSavedNews().observe(viewLifecycleOwner, Observer {savedArticles ->
            newsAdapter.oldArticles= savedArticles
        })

        val itemTouchHelperCallback= object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                val article= newsAdapter.oldArticles[position]
                viewModel.deleteArticle(article)

                Snackbar.make(view,"Article DeletedSuccessfully",Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.savaArticle(article)
                    }
                    show()
                }
            }
        }

       ItemTouchHelper(itemTouchHelperCallback).apply {
           attachToRecyclerView(binding.rvSavedNews)
       }

    }


    private fun setUpRecyclerView(newsRecyclerView: RecyclerView) {
        newsAdapter= NewsAdapter(this)
        newsRecyclerView.apply {
            adapter= newsAdapter
            layoutManager= LinearLayoutManager(activity) //
        }

    }

    override fun getViewModelClass(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory {
        val newsRepository = NewsRepository(ArticleDatabase(requireContext()) as ArticleDatabase)

        return NewViewModelProviderFactory(
            requireActivity().application,
            newsRepository,
            SharedPreferencesManager.getLanguageOfNews(requireContext()),
            SharedPreferencesManager.getCountryOfNews(requireContext())
        )
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_savedNewsFragment_to_articleFragment,
            bundle
        )
    }

}