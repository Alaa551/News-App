package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.OnArticleClickListener
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class BreakingNewsFragment : BaseFragment<NewsViewModel>(), OnArticleClickListener {
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentBreakingNewsBinding
    private lateinit var scrollListener:RecyclerView.OnScrollListener
    private val TAG = "BreakingNewsFragment"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentBreakingNewsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView(binding.rvBreakingNews)
        showShimmer()

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response -> //
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

    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.rvBreakingNews.visibility = View.VISIBLE


    }


    private fun showShimmer() {
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
        val newsRepository = NewsRepository(ArticleDatabase(requireContext()))

        return NewViewModelProviderFactory(
            requireActivity().application,
            newsRepository,
           lang= SharedPreferencesManager.getLanguageOfNews(requireContext()),
           countryCode= SharedPreferencesManager.getCountryOfNews(requireContext())
        )
    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_breakingNewsFragment_to_articleFragment,
            bundle
        )
    }

//
//    fun handlePagination(){
//        var isLoading= false
//        var isLastPage= false
//        var isScrolling= false
//
//         scrollListener= object : RecyclerView.OnScrollListener(){
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
//                    isScrolling=true
//
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//
//                val layoutManager= binding.rvBreakingNews.layoutManager as LinearLayoutManager
//                val firsVisibleItemPosition= layoutManager.findFirstVisibleItemPosition()
//                val visibleItemCount= layoutManager.childCount
//                val totalItemCount=layoutManager.itemCount
//
//
//                val isNotLoadingAndNotLastPage= !isLoading && !isLastPage
//                val isLastItem= firsVisibleItemPosition+visibleItemCount >= totalItemCount
//                val isNotAtBeginning= firsVisibleItemPosition >=0
//                val isTotalMoreThanVisible= totalItemCount >= QUERY_PAGE_SIZE
//
//                val shouldPaginate= isNotAtBeginning && isLastItem && isNotLoadingAndNotLastPage
//                        && isTotalMoreThanVisible && isScrolling
//
//                if(shouldPaginate){
//                    viewModel.getBreakingNews(
//                        countryCode =  SharedPreferencesManager.getLanguageOfNews(requireContext()),
//                       lang = SharedPreferencesManager.getCountryOfNews(requireContext())
//                    )
//
//                    isScrolling=false
//
//                }
//            }
//        }
//    }

}