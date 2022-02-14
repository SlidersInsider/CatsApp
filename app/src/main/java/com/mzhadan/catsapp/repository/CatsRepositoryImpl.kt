package com.mzhadan.catsapp.repository

import com.mzhadan.catsapp.App
import com.mzhadan.catsapp.api.CatsApiService
import com.mzhadan.catsapp.entities.*
import okhttp3.MultipartBody
import javax.inject.Inject

class CatsRepositoryImpl: CatsRepository {

    @Inject
    lateinit var catsApiService: CatsApiService

    init{
        App.appComponent.inject(this)
    }

    override suspend fun getCats(page: Int, breedId: String, categoryId: String): ArrayList<CatInfoItem> {

        return catsApiService.getCats(page = page, breed_id = breedId, category_ids = categoryId)
    }

    override suspend fun getBreeds(): ArrayList<Breed> {
        return catsApiService.getBreeds()
    }

    override suspend fun getCategories(): ArrayList<Category> {
        return catsApiService.getCategories()
    }

    override suspend fun getFavouritesCats(page: Int): ArrayList<CatInfoItem> {
        return catsApiService.getFavouritesCats(page = page)
    }

    override suspend fun getDownloadImages(page: Int): ArrayList<CatInfoItem> {
        return catsApiService.getDownloadImages(page = page)
    }

    override suspend fun getCatAnalysis(id: String): ArrayList<CatAnalysis> {
        return catsApiService.getCatAnalysis(id = id)
    }

    override suspend fun postFavouriteCats(favoriteCatPost: FavoriteCatPost) {
        return catsApiService.postFavoriteCat(favoriteCatPost = favoriteCatPost)
    }

    override suspend fun postVote(votePost: VotePost) {
        return catsApiService.postVote(votePost = votePost)
    }

    override suspend fun postCatImage(imageFile: MultipartBody.Part) {
        return catsApiService.postCatImage(imageFile)
    }

    override suspend fun deleteFromFavourites(id: String) {
        return catsApiService.deleteFromFavourites(id = id)
    }

    override suspend fun deleteFromDownloads(id: String) {
        return catsApiService.deleteFromDownloads(id = id)
    }
}