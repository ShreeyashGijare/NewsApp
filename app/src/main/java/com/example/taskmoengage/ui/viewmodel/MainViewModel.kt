package com.example.taskmoengage.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmoengage.model.newsModel.Article
import com.example.taskmoengage.model.newsModel.NewsResponse
import com.example.taskmoengage.network.RequestHandler
import com.example.taskmoengage.utils.Constants
import com.example.taskmoengage.utils.OrderType
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var newsList: MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    val _newsList: LiveData<List<Article>> = newsList

    var errorData: MutableLiveData<String> = MutableLiveData()


    fun getNewsData(url: String, orderType: OrderType) {
        if (newsList.value!!.isEmpty()) {
            val getNewsData = viewModelScope.async(Dispatchers.IO) {
                RequestHandler.requestGet(url)
            }
            viewModelScope.launch(Dispatchers.Main) {
                try {
                    val response =
                        Gson().fromJson(getNewsData.await(), NewsResponse::class.java)
                    newsList.value = response.articles as ArrayList<Article>
                } catch (e: Exception) {
                    errorData.value = e.message
                }
            }
        } else {
            newsList.value = sortListBasedOnOrder(orderType, newsList.value!!)
        }
    }

    private fun sortListBasedOnOrder(orderType: OrderType, newsList: List<Article>): List<Article> {
        return when (orderType) {
            is OrderType.LatestFirst -> {
                newsList.sortedBy { it.publishedAt.let { it1 -> Constants.convertStringToDate(it1) } }
            }

            is OrderType.OldestFirst -> {
                newsList.sortedByDescending {
                    it.publishedAt.let { it1 ->
                        Constants.convertStringToDate(
                            it1
                        )
                    }
                }
            }

            is OrderType.Title -> {
                newsList.sortedByDescending { it.title }
            }
        }
    }
}