package com.mzhadan.catsapp.ui.catsDownloadPage

import android.util.Log
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
import okhttp3.MultipartBody
import java.util.concurrent.Executors

class CatsDownloadViewModel: ViewModel() {

    var downloadCatsLiveData = MutableLiveData<PagedList<CatInfoItem>>()
    var statusLiveData = MutableLiveData<String>()
    var refreshLiveData = MutableLiveData<Boolean>()
    val catsRepositoryImpl = CatsRepositoryImpl()
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
                    val response = catsRepositoryImpl.getDownloadImages(pages)
                    callback.onResult(response, pages)
                }
            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfoItem>) {
                viewModelScope.launch(Dispatchers.IO) {
                    pages++
                    val response = catsRepositoryImpl.getDownloadImages(pages)
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

        downloadCatsLiveData.postValue(pagedList)
    }

    fun refreshData(){
        setupPaging(setupDataSource())
        downloadCatsLiveData.postValue(pagedList)
    }

    fun postCatImage(imageFile: MultipartBody.Part){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                catsRepositoryImpl.postCatImage(imageFile = imageFile)
                statusLiveData.postValue("Successfully downloaded!")
                refreshLiveData.postValue(true)
            }
            catch (e: Exception){
                statusLiveData.postValue("Can`t download!")
                refreshLiveData.postValue(false)
            }
        }
    }

    fun deleteFromDownloads(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                catsRepositoryImpl.deleteFromDownloads(id = id)

            }
            catch (e: Exception){
                Log.e("AAAA", e.message.toString())
                statusLiveData.postValue("Successfully deleted!")
                refreshLiveData.postValue(true)
            }
        }
    }
}