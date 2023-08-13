package com.example.cory_admin.Model

import android.content.ContentValues.TAG
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class PaginationScrollListener(private val linearLayoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val visibleItemCount:Int = linearLayoutManager.childCount
        val totalItemCount:Int = linearLayoutManager.itemCount
        val firstItemPosition:Int = linearLayoutManager.findFirstVisibleItemPosition()

        if(isLoading() || isLastPage()){
            return
        }

        if(firstItemPosition>=0 && (visibleItemCount + firstItemPosition)>=totalItemCount){
            loadMoreItem()
        }
    }

    abstract fun loadMoreItem()
    abstract fun isLoading():Boolean
    abstract fun isLastPage():Boolean
}