package com.src.book.presentation.registration.first_registration

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentRegistrationUserInformationBinding
import com.src.book.presentation.MainActivity
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.sign_in.viewModel.SignInViewModel
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView

class RegistrationUserInfoFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationUserInformationBinding
    private lateinit var bindingLoading: FragmentLoadingBinding
    private lateinit var viewModel: SignInViewModel

    val GALLERY_REQUEST_CODE = 1234
    var imSemafor: ImageView? = null

    private var isClickNext = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = FragmentRegistrationUserInformationBinding.inflate(inflater)
        viewModel = (activity as LoginActivity).getSignInViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvButtonNext.setOnClickListener {
            binding.tilNickname.error = "Введите корректный никнейм"
            binding.tilNickname.errorIconDrawable = null
            binding.tilEnterName.error = "Введите корректное имя"
            binding.tilEnterName.errorIconDrawable = null
        }

        binding.cdAddPicture.setOnClickListener {
            pickFromGallery()
        }
    }

    private fun pickFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        val mimeTypes = arrayOf("image/jpeg", "image/png", "image/jpg")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            GALLERY_REQUEST_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    data?.data.let { uri ->
                        launchImageCrop(uri)
                    }
                } else {
                    Log.e(Companion.TAG, "Image selection error")
                }
            }

            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                println(result.uri)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        setImage(it)
                    }

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Log.e(Companion.TAG, "Crop error: ${result.error}")
                }
            }
        }
    }

    private fun setImage(uri: Uri) {
        binding.ivAddFriends.setPadding(0,0,0,0)
        Glide.with(this)
            .load(uri)
            .into(binding.ivAddFriends)
    }

    private fun launchImageCrop(uri: Uri?) {
        CropImage.activity(uri)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(requireContext(), this)
    }

    companion object {
        private const val TAG = "AppDebug"
    }

}
