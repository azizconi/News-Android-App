package com.example.newsapp.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.data.Models.HistoryNewsEntity.ArticlesHistoryNews
import com.example.newsapp.data.Models.HistoryNewsEntity.NewsResponseHistory
import com.example.newsapp.network.api.Api
import com.example.newsapp.network.api.RetrofitInstance
import com.example.newsapp.ui.logout.AdapterAllCategoryNews
import com.example.newsapp.ui.logout.AllViewModel
import com.example.newsapp.utils.OnClick
import kotlinx.android.synthetic.main.fragment_recent.*
import tj.livo.newsapp.models.Articles

class SearchFragment : Fragment(), OnClick {
    private var getNewsResponse = MutableLiveData<NewsResponseHistory>()
    private lateinit var recyclerViewSearchNews: RecyclerView

    private lateinit var adapterAllCategoryNews: AdapterAllCategoryNews
    private lateinit var articlesHistoryNews: ArticlesHistoryNews
    private lateinit var viewModel: AllViewModel
    private lateinit var articles: Articles
    private var articlesList: List<Articles> = emptyList()
    private lateinit var progressBar: ProgressBar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AllViewModel::class.java)
        progressBar = view.findViewById(R.id.progress_circular)


        recyclerViewSearchNews = view.findViewById(R.id.searchRecyclerView)
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewSearchNews.layoutManager = layoutManager
        adapterAllCategoryNews = AdapterAllCategoryNews(this, this)
        recyclerViewSearchNews.adapter = adapterAllCategoryNews
        viewModel.readAllData.observe(viewLifecycleOwner) {
            

            adapterAllCategoryNews.setData(it)
            progressBar.visibility = View.GONE
            Log.e("TAG", "onQueryTextSubmit: $it")
        }



        searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                filter: CharSequence?,
                start: Int,
                count: Int,
                after: Int,
            ) {

            }

            override fun onTextChanged(filter: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(filter.toString())
            }

            override fun afterTextChanged(filter: Editable?) {

            }

        })


    }


    private fun filterList(filter: String) {

        val tempList: MutableList<Articles> = ArrayList()

        for (d in articlesList) {
            if (filter in d.title.toString()) {
                tempList.add(d)
            }
        }

        viewModel.searchDataBase(filter).observe(viewLifecycleOwner) {
            adapterAllCategoryNews.setData(it)
            progressBar.visibility = View.GONE
            Log.e("TAG", "onQueryTextSubmit: $it")
        }

    }


    override fun onclickListener(articles: Articles) {
        val bundle: Bundle = Bundle()
        bundle.putParcelable("article", articles)
        findNavController().navigate(R.id.allNewsFragment, bundle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }


}