package com.mzhadan.catsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mzhadan.catsapp.R
import com.mzhadan.catsapp.entities.CatInfoItem
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cat_recyclerview_item.view.*

class CatsListRecyclerAdapter(private val callback: CatViewHolder.Callback): PagedListAdapter<CatInfoItem, CatsListRecyclerAdapter.CatViewHolder>(CatDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatViewHolder {
        return CatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cat_recyclerview_item, parent, false), callback = callback)
    }

    override fun onBindViewHolder(holder: CatViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    class CatViewHolder(itemView: View, private val callback: Callback): RecyclerView.ViewHolder(itemView){

        interface Callback{
            fun onCatClicked(model: CatInfoItem)
            fun onLongCatClicked(model: CatInfoItem)
        }

        fun bind(model: CatInfoItem){
            if(model.url != null){
                Picasso.get().load(model.url).into(itemView.catImageView)
            }
            else{
                Picasso.get().load(model.image.url).into(itemView.catImageView)
            }

            itemView.catImageView.setOnClickListener{
                callback.onCatClicked(model = model)
            }
            itemView.catImageView.setOnLongClickListener {
                callback.onLongCatClicked(model = model)
                true
            }
        }
    }

}