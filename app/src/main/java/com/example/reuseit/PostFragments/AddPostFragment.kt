package com.example.reuseit.PostFragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.reuseit.Application.Database.Entity.PostEntity
import com.example.reuseit.Application.Global.CurrentUser
import com.example.reuseit.DatabaseInstance
import com.example.reuseit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddPostFragment : Fragment() {
    private lateinit var imagePreview: ImageView
    private lateinit var etTitle: EditText
    private lateinit var photoFile: File
    private var imagePath: String? = null

    companion object {
        private const val REQUEST_IMAGE_CAPTURE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_post, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imagePreview = view.findViewById(R.id.imagePreview)
        etTitle = view.findViewById(R.id.etTitle)

        val btnTakePicture = view.findViewById<Button>(R.id.btnTakePicture)
        val btnSavePost = view.findViewById<Button>(R.id.btnSavePost)

        btnTakePicture.setOnClickListener {
            dispatchTakePictureIntent()
        }

        btnSavePost.setOnClickListener {
            val title = etTitle.text.toString()
            if (title.isBlank()) return@setOnClickListener

            val post = PostEntity(
                userId = CurrentUser.Data.UserID,
                userName = "${CurrentUser.Data.FirstName} ${CurrentUser.Data.LastName}",
                title = title,
                imagePath = imagePath ?: ""
            )


            CoroutineScope(Dispatchers.IO).launch {
                DatabaseInstance.Access.postDAO().insert(post)
            }

            findNavController().popBackStack()
        }
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            try {
                photoFile = createImageFile()
                val photoURI: Uri = FileProvider.getUriForFile(
                    requireContext(),
                    "${requireActivity().packageName}.fileprovider",
                    photoFile
                )
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
    }

    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            imagePath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, Uri.fromFile(photoFile))
            imagePreview.setImageBitmap(bitmap)
        }
    }
}
