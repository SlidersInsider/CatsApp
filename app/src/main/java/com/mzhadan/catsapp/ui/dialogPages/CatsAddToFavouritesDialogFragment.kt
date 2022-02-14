package com.mzhadan.catsapp.ui.dialogPages

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.ui.catsListPage.CatsListViewModel


class CatsAddToFavouritesDialogFragment(catsListViewModel: CatsListViewModel, model: CatInfoItem): DialogFragment() {

    private val viewModel = catsListViewModel
    private val catModel = model

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setMessage("Add to favourites?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    viewModel.postToFavourites(model = catModel)
                    dialog.cancel()

                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                .create()
    }



}