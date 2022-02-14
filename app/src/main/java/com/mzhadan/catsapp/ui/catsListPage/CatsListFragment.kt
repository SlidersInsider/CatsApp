package com.mzhadan.catsapp.ui.catsListPage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mzhadan.catsapp.R
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.ui.adapters.CatsListRecyclerAdapter
import com.mzhadan.catsapp.ui.dialogPages.CatsAddToFavouritesDialogFragment
import kotlinx.android.synthetic.main.cats_list_fragment.*


class CatsListFragment: Fragment() {

    lateinit var catsListViewModel: CatsListViewModel
    lateinit var catsListRecyclerAdapter: CatsListRecyclerAdapter

    var breedCounter = 0
    var categoryCounter = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cats_list_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()
        setupFilters()

        setupStatusMessage()
        setupRecyclerRefresh()
    }

    private fun setupRecyclerView(){
        catsListRecyclerAdapter = CatsListRecyclerAdapter(object: CatsListRecyclerAdapter.CatViewHolder.Callback{
            override fun onCatClicked(model: CatInfoItem) {
                val bundle = Bundle()
                val infoArray = arrayListOf(model.id, model.url, "list")
                bundle.putStringArrayList("catsInfo", infoArray)
                findNavController().navigate(R.id.catsInfoFragment, bundle)
            }

            override fun onLongCatClicked(model: CatInfoItem) {
                CatsAddToFavouritesDialogFragment(catsListViewModel, model).show(parentFragmentManager, "My Add To Favourites Dialog")
            }
        })

        mainCatsListRecyclerView.layoutManager = GridLayoutManager(this.context,2)
        mainCatsListRecyclerView.adapter = catsListRecyclerAdapter
    }

    private fun setupViewModel(){
        catsListViewModel = ViewModelProvider(this).get(CatsListViewModel::class.java)

        catsListViewModel.catsListLiveData.observe(viewLifecycleOwner, {
            if(it != null){
                catsListRecyclerAdapter.submitList(it)
            }
            else{
                Toast.makeText(context, "Failed to fetch data!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setupFilters(){
        setupBreedSpinnerData()
        setupCategorySpinnerData()
    }

    private fun setupBreedSpinnerData(){
        catsListViewModel.breedsListLiveData.observe(viewLifecycleOwner, {
            val breedsArrayNames = ArrayList<String>()
            val breedsArrayId = ArrayList<String>()

            for(i in 0 until it.size){
                breedsArrayNames.add(it[i].name)
                breedsArrayId.add(it[i].id)
            }

            breedListSpinner.adapter = ArrayAdapter(requireContext(), R.layout.filter_text_item, breedsArrayNames)

            breedListSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    val item = p0?.getItemAtPosition(p2).toString()

                    if (breedCounter == 0){
                        breedCounter++
                        catsListViewModel.refreshLiveData.postValue(false)
                    }
                    else{
                        if(item == "All breeds"){
                            catsListViewModel.breedIdChoosed = ""
                            categoryListSpinner.isEnabled = true
                        }
                        else{
                            catsListViewModel.breedIdChoosed = breedsArrayId[p2]
                            categoryListSpinner.isEnabled = false
                        }

                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        catsListViewModel.refreshLiveData.postValue(true)
                    }

                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(context, "Nothing was selected!", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun setupCategorySpinnerData(){
        catsListViewModel.categoriesListLiveData.observe(viewLifecycleOwner, {
            val categoriesArrayNames = ArrayList<String>()
            val categoriesArrayId = ArrayList<String>()

            for(i in 0 until it.size){
                categoriesArrayNames.add(it[i].name)
                categoriesArrayId.add(it[i].id)

            }
            categoryListSpinner.adapter = ArrayAdapter(requireContext(), R.layout.filter_text_item, categoriesArrayNames)

            categoryListSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                    val item = p0?.getItemAtPosition(p2).toString()

                    if (categoryCounter == 0){
                        categoryCounter++
                        catsListViewModel.refreshLiveData.postValue(false)
                    }
                    else{
                        if(item == "All categories"){
                            catsListViewModel.categoryIdChoosed = ""
                            breedListSpinner.isEnabled = true
                        }
                        else{
                            catsListViewModel.categoryIdChoosed = categoriesArrayId[p2]
                            breedListSpinner.isEnabled = false
                        }

                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        catsListViewModel.refreshLiveData.postValue(true)
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(context, "Nothing was selected!", Toast.LENGTH_SHORT).show()

                }
            }
        })

    }

    private fun setupStatusMessage() {
        catsListViewModel.statusLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupRecyclerRefresh(){
        catsListViewModel.refreshLiveData.observe(viewLifecycleOwner, {
            if (it){
                catsListViewModel.refreshData()
            }
        })
    }
}