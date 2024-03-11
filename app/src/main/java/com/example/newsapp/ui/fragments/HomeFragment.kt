package com.example.newsapp.ui.fragments

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.local.ArticleDatabase
import com.example.newsapp.data.model.Article
import com.example.newsapp.data.model.Category
import com.example.newsapp.databinding.FragmentHomeBinding
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.data.local.sharedPref.SharedPreferencesManager
import com.example.newsapp.ui.adapter.CategoriesAdapter
import com.example.newsapp.ui.adapter.NewsAdapter
import com.example.newsapp.ui.adapter.OnArticleClickListener
import com.example.newsapp.ui.adapter.OnCategoryClickListener
import com.example.newsapp.util.Resource
import com.example.newsapp.viewModel.NewViewModelProviderFactory
import com.example.newsapp.viewModel.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class HomeFragment : BaseFragment<NewsViewModel>(), OnCategoryClickListener ,OnArticleClickListener{
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categories = listOf<Category>(
            Category("sports", R.drawable.sports),
            Category("general", R.drawable.general),
            Category("science", R.drawable.science),
            Category("health", R.drawable.health),
            Category("technology", R.drawable.technology),
            Category("business", R.drawable.business),
            Category("entertainment", R.drawable.entertainment),
        )

        setUpCategoriesRecyclerView(categories)
        setUpRecyclerView()
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
                setUpRecyclerView()
                showNoInternetAnim()
            }
        })


        binding.seeAll.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_breakingNewsFragment)
        }
    }

    fun showNoInternetAnim() {
        binding.noInternetAnim.visibility = View.VISIBLE
        binding.noInternetAnim.playAnimation()
    }

    fun hideNoInternetAnim() {
        binding.noInternetAnim.cancelAnimation()
        binding.noInternetAnim.visibility = View.GONE
    }

    private fun setUpCategoriesRecyclerView(categories: List<Category>) {
        categoriesAdapter = CategoriesAdapter(categories, this)
        binding.recyclerViewCategories.apply {
            adapter = categoriesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false) //
        }

    }

    override fun onItemClick(category: Category) {

        Log.d("hhh","click")
        val action= HomeFragmentDirections.actionHomeFragmentToSpecificCategoryFragment3(category.name)
        findNavController().navigate(action)
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

    private fun hideShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility= View.GONE
        binding.rvBreakingNews.visibility= View.VISIBLE

    }


    private fun showShimmer() {
        binding.shimmerLayout.startShimmer()

    }
    private fun setUpRecyclerView() {
        newsAdapter = NewsAdapter(this)
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity) //
        }

    }

    override fun onItemClick(article: Article) {
        val bundle = Bundle().apply {
            putSerializable("article", article)
        }

        findNavController().navigate(
            R.id.action_homeFragment_to_articleFragment,
            bundle
        )
    }
    }
