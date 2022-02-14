package com.mzhadan.catsapp.ui.catsListPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.mzhadan.catsapp.entities.Breed
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.entities.Category
import com.mzhadan.catsapp.entities.FavoriteCatPost
import com.mzhadan.catsapp.repository.CatsRepositoryImpl
import com.mzhadan.catsapp.ui.adapters.MainThreadExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors


class CatsListViewModel: ViewModel() {

    var catsListLiveData = MutableLiveData<PagedList<CatInfoItem>>()
    var breedsListLiveData = MutableLiveData<ArrayList<Breed>>()
    var categoriesListLiveData = MutableLiveData<ArrayList<Category>>()

    var statusLiveData = MutableLiveData<String>()
    var refreshLiveData = MutableLiveData<Boolean>()
    var catsRepositoryImpl = CatsRepositoryImpl()
    lateinit var pagedList: PagedList<CatInfoItem>
    var breedIdChoosed = ""
    var categoryIdChoosed = ""

    init {
        setupPaging(setupDataSource())
        setupBreeds()
        setupCategories()
    }

    private fun setupDataSource(): PositionalDataSource<CatInfoItem>{
        val dataSource = object: PositionalDataSource<CatInfoItem>(){

            var pages: Int = 0

            override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<CatInfoItem>) {
                viewModelScope.launch(Dispatchers.IO) {
                    pages = 0
                    val response = catsRepositoryImpl.getCats(pages, breedIdChoosed, categoryIdChoosed)
                    callback.onResult(response, pages)
                }
            }

            override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<CatInfoItem>) {
                viewModelScope.launch(Dispatchers.IO) {
                    pages++
                    val response = catsRepositoryImpl.getCats(pages, breedIdChoosed, categoryIdChoosed)
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

        catsListLiveData.postValue(pagedList)
    }

    private fun setupBreeds(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = ArrayList<Breed>()
            response.add(Breed("","All breeds","",""))
            response.addAll(catsRepositoryImpl.getBreeds())
            breedsListLiveData.postValue(response)
        }
    }

    private fun setupCategories(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = ArrayList<Category>()
            response.add(Category("","All categories"))
            response.addAll(catsRepositoryImpl.getCategories())
            categoriesListLiveData.postValue(response)
        }
    }

    fun postToFavourites(model: CatInfoItem){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                catsRepositoryImpl.postFavouriteCats(FavoriteCatPost(image_id = model.id))
                statusLiveData.postValue("Successfully added!")
            }
            catch (e: Exception){
                statusLiveData.postValue("Can`t add!")
            }

        }
    }

    fun refreshData(){
        setupPaging(setupDataSource())
        catsListLiveData.postValue(pagedList)
    }

}