package com.mzhadan.catsapp.ui.catsInfoPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mzhadan.catsapp.R
import com.mzhadan.catsapp.ui.adapters.CatsAnalysisRecyclerAdapter
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.cats_info_fragment.*

class CatsInfoFragment : Fragment() {

    lateinit var catsInfoViewModel: CatsInfoViewModel
    lateinit var catsAnalysisRecyclerAdapter: CatsAnalysisRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cats_info_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupCatInfo()
    }

    private fun setupViewModel(){
        catsInfoViewModel = ViewModelProvider(this).get(CatsInfoViewModel::class.java)
    }

    private fun setupCatInfo() {
        catsAnalysisRecyclerAdapter = CatsAnalysisRecyclerAdapter()
        val catInfo = requireArguments().getStringArrayList("catsInfo")
        val imageId = catInfo?.get(0)
        val imageUrl = catInfo?.get(1)
        val fragmentFlag = catInfo?.get(2)

        Picasso.get().load(imageUrl).into(catInfoFragmentImageView)
        catInfoFragmentName.text = imageId

        buttonLike.setOnClickListener {

            catsInfoViewModel.postCatVote(imageId = imageId!!, value = 1)
            Toast.makeText(context, "Liked!", Toast.LENGTH_SHORT).show()
        }

        buttonDisLike.setOnClickListener {

            catsInfoViewModel.postCatVote(imageId = imageId!!, value = 0)
            Toast.makeText(context, "Disliked!", Toast.LENGTH_SHORT).show()
        }
        if(fragmentFlag.equals("download")){
            catsInfoViewModel.getCatAnalysis(imageId = imageId!!)
            catsInfoViewModel.catAnalysisLiveData.observe(viewLifecycleOwner, {
                catsAnalysisRecyclerAdapter.setData(it[0].labels)

                infoCatsListRecyclerView.layoutManager = LinearLayoutManager(this.context)
                infoCatsListRecyclerView.adapter = catsAnalysisRecyclerAdapter
            })
        }
        else{
            catsAnalysisRecyclerAdapter.setData(ArrayList())

            infoCatsListRecyclerView.layoutManager = LinearLayoutManager(this.context)
            infoCatsListRecyclerView.adapter = catsAnalysisRecyclerAdapter
        }

    }

}