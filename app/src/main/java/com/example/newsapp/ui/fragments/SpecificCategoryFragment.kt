package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentSpecificCategoryBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.OnArticleClickListener
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class SpecificCategoryFragment : BaseFragment<NewsViewModel>(), OnArticleClickListener {
    private lateinit var binding: FragmentSpecificCategoryBinding
    private val args: SpecificCategoryFragmentArgs by navArgs()
    private lateinit var newsAdapter: NewsAdapter



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSpecificCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        showShimmer()

        binding.categoryName.text= args.categoryName

        viewModel.getCategoryNews(
            SharedPreferencesManager.getCountryOfNews(requireContext()),
            SharedPreferencesManager.getLanguageOfNews(requireContext()),
            category = args.categoryName
        )

        viewModel.categoryNews.observe(viewLifecycleOwner, Observer { response ->

            when (response) {
                is Resource.Success -> {
                    hideShimmer()
                    response.data?.let { newsResponse ->
                        newsAdapter.oldArticles = newsResponse.articles
                    }
                }

                is Resource.Error -> {
                    hideShimmer()
                    response.message?.let { message ->
                        Snackbar.make(view, "An error occurred: $message", Snackbar.LENGTH_SHORT)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    showShimmer()
                }
            }
        })


    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_specificCategoryFragment_to_articleFragment, bundle
        )
    }


    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.rvBreakingNews.visibility = View.VISIBLE

    }


    private fun showShimmer() {
        binding.shimmerLayout.startShimmer()

    }

    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireContext()) //
        }

    }

    override fun getViewModelClass(): Class<NewsViewModel> {
        return NewsViewModel::class.java
    }

    override fun getViewModelFactory(): ViewModelProvider.Factory {
        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))

        return NewViewModelProviderFactory(
            requireActivity().application,
            newsRepository,
            SharedPreferencesManager.getLanguageOfNews(requireContext()),
            SharedPreferencesManager.getCountryOfNews(requireContext())
        )
    }


}


