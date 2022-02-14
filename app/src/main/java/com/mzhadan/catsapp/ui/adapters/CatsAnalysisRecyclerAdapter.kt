package com.mzhadan.catsapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mzhadan.catsapp.R
import com.mzhadan.catsapp.entities.CatLabels
import kotlinx.android.synthetic.main.analysis_recyclerview_item.view.*

class CatsAnalysisRecyclerAdapter: RecyclerView.Adapter<CatsAnalysisRecyclerAdapter.AnalysisViewHolder>() {

    private val analysisInfo: ArrayList<CatLabels> = ArrayList()

    fun setData(newAnalysisInfo: ArrayList<CatLabels>) {
        analysisInfo.clear()
        analysisInfo.addAll(newAnalysisInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnalysisViewHolder {
        return AnalysisViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.analysis_recyclerview_item, parent, false))
    }

    override fun onBindViewHolder(holder: AnalysisViewHolder, position: Int) {
        holder.bind(analysisInfo[position])
    }

    override fun getItemCount(): Int {
        return analysisInfo.size
    }

    class AnalysisViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(model: CatLabels){
            itemView.analysisNameTextView.text = model.name
            itemView.analysisConfidenceTextView.text = model.confidence.toString()
        }
    }
}