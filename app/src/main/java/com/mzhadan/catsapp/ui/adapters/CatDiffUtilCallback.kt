package com.mzhadan.catsapp.ui.adapters

import androidx.recyclerview.widget.DiffUtil
import com.mzhadan.catsapp.entities.CatInfoItem

class CatDiffUtilCallback: DiffUtil.ItemCallback<CatInfoItem>() {

    override fun areItemsTheSame(oldItem: CatInfoItem, newItem: CatInfoItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CatInfoItem, newItem: CatInfoItem): Boolean {
        return (oldItem.id == newItem.id) && (oldItem.url == newItem.url)
    }
}