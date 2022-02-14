package com.mzhadan.catsapp.ui.dialogPages

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.ui.catsFavoritePage.CatsFavoriteViewModel

class CatsDeleteFromFavouritesDialogFragment(catsFavoriteViewModel: CatsFavoriteViewModel, model: CatInfoItem): DialogFragment() {

    private val viewModel = catsFavoriteViewModel
    private val catModel = model

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setMessage("Delete from favourites?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    viewModel.deleteFromFavourites(catModel.id)
                    dialog.cancel()
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                .create()
    }
}