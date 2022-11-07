package com.src.book.presentation.profile.my_profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.src.book.databinding.FragmentEditProfileBinding
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.domain.utils.EditProfileState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.profile.my_profile.viewModel.MyProfileViewModel
import com.src.book.presentation.utils.PhotoCompression
import com.src.book.utils.REGEX_SPACE
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import java.util.*

class EditMyProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var bindingLoading: FragmentLoadingBinding
    private lateinit var viewModel: MyProfileViewModel
    private var photo: Uri? = null
    private var isClickNext = false

    val GALLERY_REQUEST_CODE = 1234

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater)
        bindingLoading = binding.loading
        viewModel = (activity as MainActivity).getMyProfileViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cdEditPicture.setOnClickListener {
            pickFromGallery()
        }
        setOnClickListenerForNextButton()
        viewModel.liveDataEditProfileState.observe(
            this.viewLifecycleOwner,
            this::checkEditProfileState
        )
        viewModel.liveDataIsLoading.observe(this.viewLifecycleOwner, this::checkLoading)
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
        binding.ivPicture.setPadding(0, 0, 0, 0)
        Glide.with(this)
            .load(uri)
            .into(binding.ivPicture)
        photo = uri
        viewModel.setPhoto(uri)
    }

    private fun launchImageCrop(uri: Uri?) {
        CropImage.activity(uri)
            .setCropShape(CropImageView.CropShape.OVAL)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setAspectRatio(1, 1)
            .start(requireContext(), this)
    }

    private fun setOnClickListenerForNextButton() {
        binding.tvButtonNext.setOnClickListener {
            if (binding.etNickname.text.toString().contains(" ")) {
                binding.tilNickname.error = "Логин не должен содержать пробелы."
                binding.tilNickname.errorIconDrawable = null
            } else {
                val nameWithoutSpace = binding.etName.text.toString()
                    .replace(REGEX_SPACE, " ")
                    .lowercase(Locale.getDefault())
                    .trim()
                val loginWithoutSpace =
                    binding.etNickname.text.toString().replace("\\s".toRegex(), "")
                if (nameWithoutSpace.isEmpty()) {
                    binding.tilEnterName.error = "Заполните поле."
                    binding.tilEnterName.errorIconDrawable = null
                }
                if (loginWithoutSpace.isEmpty()) {
                    binding.tilNickname.error = "Заполните поле."
                    binding.tilNickname.errorIconDrawable = null
                }
                if (binding.etName.text.toString()
                        .isNotEmpty() && binding.etNickname.text.toString()
                        .isNotEmpty()
                ) {
                    val photoCompression = PhotoCompression()
                    if (photo != null) {
                        photo = photoCompression.execute(requireContext(), photo!!)
                    }
                    viewModel.setLogin(loginWithoutSpace)
                    viewModel.setName(nameWithoutSpace)
                    viewModel.editProfile()
                }
            }
        }
        isClickNext = true
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    private fun checkEditProfileState(state: EditProfileState) {
        if (isClickNext) {
            isClickNext = false
            when (state) {
                is EditProfileState.SuccessState -> {
                    println("success")
                }
                is EditProfileState.LoginAlreadyExistsState -> println("такой логин уже существует")
                is EditProfileState.ErrorState -> println("ошибка сервера")
                else -> {

                }
            }
        }
    }

    companion object {
        private const val TAG = "AppDebug"
    }

}