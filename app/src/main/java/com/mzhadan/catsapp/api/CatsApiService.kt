package com.mzhadan.catsapp.api

import com.mzhadan.catsapp.entities.*
import okhttp3.MultipartBody
import retrofit2.http.*

interface CatsApiService {

    @GET("v1/images/search")
    suspend fun getCats(
        @Query("limit")
        limit: Int = 10,
        @Query("page")
        page: Int,
        @Query("breed_id")
        breed_id: String,
        @Query("category_ids")
        category_ids: String
    ): ArrayList<CatInfoItem>

    @GET("v1/breeds")
    suspend fun getBreeds(): ArrayList<Breed>

    @GET("v1/categories")
    suspend fun getCategories(): ArrayList<Category>

    @GET("v1/favourites")
    suspend fun getFavouritesCats(
        @Query("limit")
        limit: Int = 10,
        @Query("page")
        page: Int
    ): ArrayList<CatInfoItem>

    @GET("v1/images")
    suspend fun getDownloadImages(
            @Query("limit")
            limit: Int = 10,
            @Query("page")
            page: Int
    ): ArrayList<CatInfoItem>

    @GET("v1/images/{image_id}/analysis")
    suspend fun getCatAnalysis(
            @Path("image_id")
            id: String
    ): ArrayList<CatAnalysis>

    @POST("v1/favourites")
    suspend fun postFavoriteCat(
        @Body
        favoriteCatPost: FavoriteCatPost
    )

    @POST("v1/votes")
    suspend fun postVote(
            @Body
            votePost: VotePost
    )

    @Multipart
    @POST("v1/images/upload")
    suspend fun postCatImage(
            @Part
            imageFile: MultipartBody.Part
    )

    @DELETE("v1/favourites/{favourite_id}")
    suspend fun deleteFromFavourites(
            @Path("favourite_id")
            id: String
    )

    @DELETE("v1/images/{image_id}")
    suspend fun deleteFromDownloads(
            @Path("image_id")
            id: String
    )
}