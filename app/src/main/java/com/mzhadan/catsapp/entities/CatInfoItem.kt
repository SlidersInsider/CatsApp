package com.mzhadan.catsapp.entities

import com.google.gson.annotations.SerializedName

data class CatInfoItem(
    val id: String,
    val url: String? = null,
    val width: Int,
    val height: Int,
    val categories: ArrayList<Category>,
    val breeds: ArrayList<Breed>,

    // Favourite
    val created_at: String,
    val image: ImageCatInfo,
    val image_id: String,
    val sub_id: String,
    val user_id: String
)

data class Category(
    val id: String,
    val name: String
)

data class Breed(
    val id: String,
    val name: String,
    val origin: String,
    val description: String,
)

data class ImageCatInfo(
        val id: String,
        val url: String
)

data class FavoriteCatPost(
        val image_id: String
)

data class VotePost(
        val image_id: String,
        val value: Int
)

data class CatAnalysis(
        val image_id: String,
        val vendor: String,
        val approved: Int,
        val rejected: Int,
        val labels: ArrayList<CatLabels>
)

data class CatLabels(
        @SerializedName("Name")
        val name: String,
        @SerializedName("Confidence")
        val confidence: Float
)
