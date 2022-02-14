package com.mzhadan.catsapp.ui.catsDownloadPage

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
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
import com.mzhadan.catsapp.ui.dialogPages.CatsDeleteFromDownloadDialogFragment
import kotlinx.android.synthetic.main.cats_download_fragment.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class CatsDownloadFragment : Fragment() {

    lateinit var catsDownloadViewModel: CatsDownloadViewModel
    lateinit var catsDownloadRecyclerAdapter: CatsListRecyclerAdapter

    val GALLERY_OPEN = 1
    val CAMERA_OPEN = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.cats_download_fragment, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupViewModel()

        openGalleryButton.setOnClickListener {
            openGallery()
        }

        openCameraButton.setOnClickListener {
            openCamera()
        }

        setupStatusMessage()
        setupRecyclerRefresh()
    }

    private fun setupRecyclerView(){
        catsDownloadRecyclerAdapter = CatsListRecyclerAdapter(object : CatsListRecyclerAdapter.CatViewHolder.Callback {
            override fun onCatClicked(model: CatInfoItem) {
                val bundle = Bundle()
                val infoArray = arrayListOf(model.id, model.url, "download")
                bundle.putStringArrayList("catsInfo", infoArray)
                findNavController().navigate(R.id.catsInfoFragment, bundle)
            }
            override fun onLongCatClicked(model: CatInfoItem) {
                CatsDeleteFromDownloadDialogFragment(catsDownloadViewModel, model).show(parentFragmentManager, "My Delete From Download Dialog")
            }
        })

        downloadCatsListRecyclerView.layoutManager = GridLayoutManager(this.context, 2)
        downloadCatsListRecyclerView.adapter = catsDownloadRecyclerAdapter
    }

    private fun setupViewModel() {
        catsDownloadViewModel = ViewModelProvider(this).get(CatsDownloadViewModel::class.java)

        catsDownloadViewModel.downloadCatsLiveData.observe(viewLifecycleOwner, {
            if(it != null){
                catsDownloadRecyclerAdapter.submitList(it)
            }
            else{
                Toast.makeText(context, "Failed to fetch data!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun openGallery(){
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), GALLERY_OPEN)
    }

    private fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_OPEN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GALLERY_OPEN && resultCode == Activity.RESULT_OK){
            val catImageUri = data?.data

            val proj = arrayOf(MediaStore.Images.Media.DATA)
            val cursor = requireContext().contentResolver.query(catImageUri!!, proj, null, null, null)
            val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            val absolutePath = cursor.getString(columnIndex)
            val newFile = File(absolutePath)

            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), newFile)
            val body = MultipartBody.Part.createFormData("file", newFile.name, requestFile)

            catsDownloadViewModel.postCatImage(body)
        }
        if(requestCode == CAMERA_OPEN && resultCode == Activity.RESULT_OK){

            val extras = data?.extras
            val imageBitmap = extras!!["data"] as Bitmap?
            val stream = ByteArrayOutputStream()
            imageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val inputData: ByteArray? = stream.toByteArray()
            imageBitmap.recycle()

            val requestFile: RequestBody? =
                    inputData?.toRequestBody("image/jpg".toMediaTypeOrNull())
            val filePart =
                    requestFile?.let {
                        MultipartBody.Part.createFormData(
                                "file",
                                createImageFile(),
                                it
                        )
                    }

            catsDownloadViewModel.postCatImage(filePart!!)

        }

    }

    private fun createImageFile(): String {
        val fileNameClassifier = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "cat${fileNameClassifier}"
        val image = File.createTempFile(imageFileName,".jpg")

        return image.absolutePath
    }

    private fun setupStatusMessage() {
        catsDownloadViewModel.statusLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setupRecyclerRefresh(){
        catsDownloadViewModel.refreshLiveData.observe(viewLifecycleOwner, {
            if (it){
                catsDownloadViewModel.refreshData()
            }
        })
    }
}