package com.example.taskmoengage.ui.inteface

import com.example.taskmoengage.model.newsModel.Article
import com.example.taskmoengage.utils.Constants

interface NewsClickInterface {
    fun newsClickListener(newsItem: Article)
    fun bottomSheetFilterClickListener(filterItem: Constants.KeyValuePairImpl)
}