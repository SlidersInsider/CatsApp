package com.mzhadan.catsapp.ui.catsFavoritePage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.repository.CatsRepositoryImpl
import com.mzhadan.catsapp.ui.adapters.MainThreadExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class CatsFavoriteViewModel: ViewModel() {

    val favoriteCatsLiveData = MutableLiveData<PagedList<CatInfoItem>>()
    var statusLiveData = MutableLiveData<String>()
    var refreshLiveData = MutableLiveData<Boolean>()
    var catsRepositoryImpl = CatsRepositoryImpl()
    lateinit var pagedList: PagedList<CatInfoItem>

    init {
        setupPaging(setupDataSource())
    }

    private fun setupDataSource(): PositionalDataSource<CatInfoItem> {
        val dataSource = object: PositionalDataSource<CatInfoItem>(){

            var pages: Int = 0

            override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CatInfoItem>) {
                viewModelScope.launch(Dispatchers.IO) {
                    pages = 0
                    val response = catsRepositoryImpl.getFavouritesCats(pages)
                    callback.onResult(response, pages)
                }
            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfoItem>) {
                viewModelScope.launch(Dispatchers.IO) {
                    pages++
                    val response = catsRepositoryImpl.getFavouritesCats(pages)
                    callback.onResult(response)
                }
            }
        }
        return dataSource
    }

    private fun setupPaging(dataSource: PositionalDataSource<CatInfoItem>){

        val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build()
        pagedList = PagedList.Builder(dataSource, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setNotifyExecutor(MainThreadExecutor())
                .build()

        favoriteCatsLiveData.postValue(pagedList)
    }

    fun deleteFromFavourites(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                catsRepositoryImpl.deleteFromFavourites(id = id)
                refreshLiveData.postValue(true)
                statusLiveData.postValue("Successfully deleted!")
            }
            catch (e: Exception) {
                refreshLiveData.postValue(false)
                statusLiveData.postValue("Can`t delete!")
            }
        }
    }

    fun refreshData(){
        setupPaging(setupDataSource())
        favoriteCatsLiveData.postValue(pagedList)
    }
}