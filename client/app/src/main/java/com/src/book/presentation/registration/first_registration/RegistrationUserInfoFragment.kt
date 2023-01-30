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
import com.src.book.R
import com.src.book.databinding.FragmentLoadingBinding
import com.src.book.databinding.FragmentRegistrationUserInformationBinding
import com.src.book.domain.utils.RegistrationState
import com.src.book.presentation.registration.LoginActivity
import com.src.book.presentation.registration.first_registration.viewModel.RegistrationViewModel
import com.src.book.presentation.utils.PhotoCompression
import com.src.book.utils.REGEX_SPACE
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.json.JSONObject
import java.util.*

class RegistrationUserInfoFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationUserInformationBinding
    private lateinit var bindingLoading: FragmentLoadingBinding
    private lateinit var viewModel: RegistrationViewModel
    private var photo: Uri? = null

    val GALLERY_REQUEST_CODE = 1234

    private var isClickNext = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentRegistrationUserInformationBinding.inflate(inflater)
        viewModel = (activity as LoginActivity).getRegistrationViewModel()
        bindingLoading = binding.loading
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (viewModel.imageIsNotEmpty()) {
            val image = viewModel.liveDataImage.value
            binding.ivAddFriends.setPadding(0, 0, 0, 0)
            Glide.with(this)
                .load(image)
                .into(binding.ivAddFriends)

        }
        binding.cdAddPicture.setOnClickListener {
            pickFromGallery()
        }
        viewModel.liveDataRegistration.observe(this.viewLifecycleOwner, this::checkRegistration)
        viewModel.liveDataIsLoading.observe(this.viewLifecycleOwner, this::checkLoading)
        setOnClickListenerForNextButton()
        setOnClickListenerForSkipButton()
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
        binding.ivAddFriends.setPadding(0, 0, 0, 0)
        photo = uri
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

    //TODO обработка ошибок
    private fun checkRegistration(state: RegistrationState) {
        if (isClickNext) {
            isClickNext = false
            when (state) {
                is RegistrationState.SuccessState -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ConfirmCodeFragment())
                        .addToBackStack(null)
                        .commit()
                }
                is RegistrationState.EmailAlreadyExistsState -> println("такая почта уже существует")
                is RegistrationState.LoginAlreadyExistsState -> println("такой логин уже существует")
                is RegistrationState.ErrorState -> println("ошибка сервера")
                else -> {

                }
            }
        }
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
                    viewModel.setName(binding.etName.text.toString())
                    viewModel.setLogin(binding.etNickname.text.toString())
                    if (viewModel.liveDataLogin.value == null || viewModel.liveDataName.value == null || viewModel.liveDataEmail.value == null || viewModel.liveDataPassword.value == null) {
                        //TODO обработка ошибки (в клиенте что-то не сохранилось)
                    } else {
                        val photoCompression = PhotoCompression()
                        if (photo != null) {
                            photo = photoCompression.execute(requireContext(), photo!!)
                            viewModel.setImage(uri = photo!!)
                        }
                        viewModel.registration(photo)
                    }
                }
            }
            isClickNext = true
        }
    }

    private fun checkLoading(isLoading: Boolean) {
        if (isLoading) {
            bindingLoading.clLoadingPage.visibility = View.VISIBLE
        } else {
            bindingLoading.clLoadingPage.visibility = View.GONE
        }
    }

    //TODO перейти в новый фрагмент
    private fun setOnClickListenerForSkipButton() {
        binding.tvSkipButton.setOnClickListener {
            viewModel.loginAsGuest()
        }
    }

    companion object {
        private const val TAG = "AppDebug"
    }

}
