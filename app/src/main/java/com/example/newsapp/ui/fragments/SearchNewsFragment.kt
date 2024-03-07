package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentSearchNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.OnArticleClickListener
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchNewsFragment :
    BaseFragment<NewsViewModel>(), OnArticleClickListener {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentSearchNewsBinding
    private val TAG = "SearchNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val newsRecyclerView: RecyclerView = view.findViewById(R.id.rvSearchNews)
        val search: EditText = view.findViewById(R.id.etSearch)
        setUpRecyclerView(newsRecyclerView)

        var job: Job? = null
        search.addTextChangedListener {
            job?.cancel()
            job = MainScope().launch {
                it?.let {
                    if (it.toString().isNotEmpty()) {
                        showShimmer()
                        viewModel.searchNews(it.toString(), SharedPreferencesManager.getLanguageOfNews(requireContext()))
                    }
                }
            }
        }



        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response -> //
            when (response) {
                is Resource.Success -> {
                    hideShimmer()
                    response.data?.let { newResponse ->
                        newsAdapter.oldArticles = newResponse.articles
                    }
                }

                is Resource.Error -> {
                   hideShimmer()
                    response.message?.let { message ->
                        Snackbar.make(view,"An error occurred: $message", Snackbar.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showShimmer()
                }
            }
        })

    }


    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.rvSearchNews.visibility = View.VISIBLE

    }


    private fun showShimmer() {
        binding.shimmerLayout.visibility=View.VISIBLE
        binding.shimmerLayout.startShimmer()

    }

    private fun setUpRecyclerView(newsRecyclerView: RecyclerView) {
        newsAdapter = NewsAdapter(this)
        newsRecyclerView.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity) //
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
            R.id.action_searchNewsFragment_to_articleFragment,
            bundle
        )
    }


}