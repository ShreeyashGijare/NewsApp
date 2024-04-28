package com.example.taskmoengage.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmoengage.R
import com.example.taskmoengage.databinding.ActivityMainBinding
import com.example.taskmoengage.databinding.BottomsheetFilterBinding
import com.example.taskmoengage.model.newsModel.Article
import com.example.taskmoengage.ui.adapter.FilterBottomSheetAdapter
import com.example.taskmoengage.ui.adapter.ViewPagerAdapter
import com.example.taskmoengage.ui.inteface.NewsClickInterface
import com.example.taskmoengage.ui.viewmodel.MainViewModel
import com.example.taskmoengage.utils.Constants
import com.example.taskmoengage.utils.Constants.BASE_URL
import com.example.taskmoengage.utils.OrderType
import com.example.taskmoengage.widgets.ViewPagerPageTransformer
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity : AppCompatActivity(), NewsClickInterface {

    private lateinit var mBinding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var newsPagerAdapter: ViewPagerAdapter
    private lateinit var filterBottomSheetAdapter: FilterBottomSheetAdapter
    private lateinit var viewFilterBottomSheet: BottomSheetDialog
    private lateinit var bottomSheetFilterBinding: BottomsheetFilterBinding
    private var selectedFilter: String = "Latest News"

    private val getNewsEndPoint: String = "news-api-feed/staticResponse.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        setUpFilterBottomSheet()
        setUpFilterAdapter()
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.getNewsData(BASE_URL, OrderType.LatestFirst)

        mBinding.btnFilter.setOnClickListener {
            viewFilterBottomSheet.show()
        }
        mBinding.shimmerView.visibility = View.VISIBLE
        setObserver()
    }

    private fun setObserver() {
        mainViewModel._newsList.observe(this) {
            if (!it.isNullOrEmpty()) {
                mBinding.shimmerView.clearAnim()
                mBinding.shimmerView.visibility = View.GONE
            }
            setUpAdapter(it)
        }
        mainViewModel.errorData.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            Log.i("API_CALL_ERROR", it)
        }
    }


    private fun setUpAdapter(articleList: List<Article>) {
        newsPagerAdapter = ViewPagerAdapter(this, articleList, this)
        mBinding.newsViewPager.adapter = newsPagerAdapter
        mBinding.newsViewPager.setPageTransformer(false, ViewPagerPageTransformer())
    }

    private fun setUpFilterAdapter() {
        filterBottomSheetAdapter =
            FilterBottomSheetAdapter(this, Constants.getOrderTypesList(), this, selectedFilter)

        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        bottomSheetFilterBinding.rvFilterTypes.layoutManager = layoutManager
        bottomSheetFilterBinding.rvFilterTypes.adapter = filterBottomSheetAdapter
    }

    override fun newsClickListener(newsItem: Article) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(newsItem.url))
        startActivity(browserIntent)
    }

    override fun bottomSheetFilterClickListener(filterItem: Constants.KeyValuePairImpl) {
        when (filterItem.key) {
            is OrderType.LatestFirst -> {
                selectedFilter = filterItem.value.toString()
                mainViewModel.getNewsData(BASE_URL, OrderType.LatestFirst)
            }

            is OrderType.OldestFirst -> {
                selectedFilter = filterItem.value.toString()
                mainViewModel.getNewsData(BASE_URL, OrderType.OldestFirst)
            }

            else -> {
                selectedFilter = filterItem.value.toString()
                mainViewModel.getNewsData(BASE_URL, OrderType.Title)
            }
        }
        viewFilterBottomSheet.dismiss()
    }

    private fun setUpFilterBottomSheet() {
        bottomSheetFilterBinding =
            BottomsheetFilterBinding.inflate(layoutInflater)
        viewFilterBottomSheet =
            BottomSheetDialog(this, R.style.Bottomsheet)
        viewFilterBottomSheet.setContentView(bottomSheetFilterBinding.root)

    }
}