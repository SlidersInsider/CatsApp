package com.mzhadan.catsapp.repository

import com.mzhadan.catsapp.entities.*
import okhttp3.MultipartBody

interface CatsRepository {

    suspend fun getCats(page: Int, breedId: String, categoryId: String): ArrayList<CatInfoItem>
    suspend fun getBreeds(): ArrayList<Breed>
    suspend fun getCategories(): ArrayList<Category>
    suspend fun getFavouritesCats(page: Int): ArrayList<CatInfoItem>
    suspend fun getDownloadImages(page: Int): ArrayList<CatInfoItem>
    suspend fun getCatAnalysis(id: String): ArrayList<CatAnalysis>

    suspend fun postFavouriteCats(favoriteCatPost: FavoriteCatPost)
    suspend fun postVote(votePost: VotePost)
    suspend fun postCatImage(imageFile: MultipartBody.Part)

    suspend fun deleteFromFavourites(id: String)
    suspend fun deleteFromDownloads(id: String)
}