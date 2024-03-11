package com.example.newsapp.ui.fragments

import android.opengl.Visibility
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
    private lateinit var scrollListener: RecyclerView.OnScrollListener
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



        viewModel.isOnline.observe(viewLifecycleOwner, Observer {
            if(it==true){
                hideNoInternetAnim()
                viewModel.getBreakingNews(
                    SharedPreferencesManager.getCountryOfNews(requireContext()),
                    SharedPreferencesManager.getLanguageOfNews(requireContext())
                )
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

                                Snackbar.make(
                                    view,
                                    "An error occurred: $message",
                                    Snackbar.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }

                        is Resource.Loading -> {
                            showShimmer()

                        }
                    }
                })

            }
            else{
                hideShimmer()
                setUpRecyclerView(binding.rvBreakingNews)
                showNoInternetAnim()
            }
        })    }

    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
        binding.rvBreakingNews.visibility = View.VISIBLE


    }

    fun showNoInternetAnim() {
        binding.noInternetAnim.visibility = View.VISIBLE
        binding.noInternetAnim.playAnimation()
    }

    fun hideNoInternetAnim() {
        binding.noInternetAnim.cancelAnimation()
        binding.noInternetAnim.visibility = View.GONE
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
            lang = SharedPreferencesManager.getLanguageOfNews(requireContext()),
            countryCode = SharedPreferencesManager.getCountryOfNews(requireContext())
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


}