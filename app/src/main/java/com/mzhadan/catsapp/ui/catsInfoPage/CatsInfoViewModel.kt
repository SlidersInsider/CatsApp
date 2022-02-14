package com.mzhadan.catsapp.ui.catsInfoPage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mzhadan.catsapp.entities.CatAnalysis
import com.mzhadan.catsapp.entities.VotePost
import com.mzhadan.catsapp.repository.CatsRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatsInfoViewModel: ViewModel() {

    var catAnalysisLiveData = MutableLiveData<ArrayList<CatAnalysis>>()
    val catsRepositoryImpl = CatsRepositoryImpl()

    fun postCatVote(imageId: String, value: Int){
        val vote = VotePost(image_id = imageId, value = value)
        viewModelScope.launch(Dispatchers.IO) {
            catsRepositoryImpl.postVote(votePost = vote)
        }
    }
    fun getCatAnalysis(imageId: String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = catsRepositoryImpl.getCatAnalysis(id = imageId)
            catAnalysisLiveData.postValue(response)
        }
    }
}