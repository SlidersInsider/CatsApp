package com.mzhadan.catsapp.ui.catsFavoritePage

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mzhadan.catsapp.R
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.ui.adapters.CatsListRecyclerAdapter
import com.mzhadan.catsapp.ui.dialogPages.CatsDeleteFromFavouritesDialogFragment
import kotlinx.android.synthetic.main.cats_favorite_fragment.*

class CatsFavoriteFragment : Fragment() {

    lateinit var catsFavoriteViewModel: CatsFavoriteViewModel
    lateinit var catsFavoriteRecyclerAdapter: CatsListRecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cats_favorite_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isNetworkConnected()) {
            setupRecyclerView()
            setupViewModel()
            setupRecyclerRefresh()
            setupStatusMessage()
        } else {
            favoriteCatsListRecyclerView.visibility = View.GONE
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
        }
    }

    private fun isNetworkConnected(): Boolean {
        val cm = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null && cm.activeNetworkInfo!!.isConnected
    }

    private fun setupRecyclerView(){
        catsFavoriteRecyclerAdapter = CatsListRecyclerAdapter(object: CatsListRecyclerAdapter.CatViewHolder.Callback{
            override fun onCatClicked(model: CatInfoItem) {
                val bundle = Bundle()
                val infoArray = arrayListOf(model.id, model.image.url, "favourite")
                bundle.putStringArrayList("catsInfo", infoArray)
                findNavController().navigate(R.id.catsInfoFragment, bundle)
            }

            override fun onLongCatClicked(model: CatInfoItem) {
                CatsDeleteFromFavouritesDialogFragment(catsFavoriteViewModel, model).show(parentFragmentManager, "My Delete From Favourites Dialog")
            }
        })

        favoriteCatsListRecyclerView.layoutManager = GridLayoutManager(this.context,2)
        favoriteCatsListRecyclerView.adapter = catsFavoriteRecyclerAdapter
    }

    private fun setupViewModel(){
        catsFavoriteViewModel = ViewModelProvider(this).get(CatsFavoriteViewModel::class.java)

        favoriteCatsListRecyclerView.visibility = View.VISIBLE

        catsFavoriteViewModel.favoriteCatsLiveData.observe(viewLifecycleOwner, {
            if(it != null){
                catsFavoriteRecyclerAdapter.submitList(it)
            }
            else{
                Toast.makeText(context, "Failed to fetch data!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupRecyclerRefresh(){
        catsFavoriteViewModel.refreshLiveData.observe(viewLifecycleOwner, {
            if (it){
                catsFavoriteViewModel.refreshData()
            }
        })
    }
    private fun setupStatusMessage() {
        catsFavoriteViewModel.statusLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

}