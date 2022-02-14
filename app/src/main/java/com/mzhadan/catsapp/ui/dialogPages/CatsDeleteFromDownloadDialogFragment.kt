package com.mzhadan.catsapp.ui.dialogPages

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mzhadan.catsapp.entities.CatInfoItem
import com.mzhadan.catsapp.ui.catsDownloadPage.CatsDownloadViewModel

class CatsDeleteFromDownloadDialogFragment(catsDownloadViewModel: CatsDownloadViewModel, model: CatInfoItem): DialogFragment() {

    private val viewModel = catsDownloadViewModel
    private val catModel = model

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(context)
                .setMessage("Delete from downloads?")
                .setPositiveButton("Yes"){ dialog, _ ->
                    viewModel.deleteFromDownloads(catModel.id)
                    dialog.cancel()
                }
                .setNegativeButton("No"){ dialog, _ ->
                    dialog.cancel()
                }
                .create()
    }



}