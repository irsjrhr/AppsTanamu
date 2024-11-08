package com.bangkit.ch2_ps178_android.view.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bangkit.ch2_ps178_android.R
import com.bangkit.ch2_ps178_android.databinding.FragmentProfileBinding
import com.bangkit.ch2_ps178_android.view.profile.about.AboutUsActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef: StorageReference = storage.reference.child("profile_images")

    lateinit var textName: TextView
    lateinit var textEmail: TextView
    lateinit var btnLogout: Button
    lateinit var profileImageView: ImageView
    lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null
    private var profileImageURL: String? = null
        set(value) {
            field = value
            loadProfileImage()
        }

    private var hasProfileImageLoaded = false

    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedImageUri = uri
                    uploadImageToFirebase()
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textName = binding.profileName
        textEmail = binding.profileEmail
        btnLogout = binding.btnLogout
        profileImageView = binding.profileUser
        progressBar = binding.progressBar

        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser != null) {
            textName.text = firebaseUser.displayName
            textEmail.text = firebaseUser.email
            loadProfileImage()
            loadProfileImageURL()
        }

        profileImageView.setOnClickListener {
            openGalleryForImage()
        }

        val aboutLayout = binding.layoutProfileAbout
        aboutLayout.setOnClickListener {
            val intent = Intent(requireContext(), AboutUsActivity::class.java)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            (activity as? LogoutListener)?.onLogout()
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getImageContent.launch(intent)
    }

    private fun uploadImageToFirebase() {
        progressBar.visibility = View.VISIBLE

        selectedImageUri?.let { uri ->
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val originalBitmap = BitmapFactory.decodeStream(inputStream)

            val compressedBitmap = compressBitmap(originalBitmap)
            val outputStream = ByteArrayOutputStream()
            compressedBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val data = outputStream.toByteArray()

            val imageRef = storageRef.child("${firebaseAuth.currentUser?.uid}.jpg")

            imageRef.putBytes(data)
                .addOnSuccessListener {
                    Log.d("ProfileFragment", "Image uploaded successfully")
                    saveProfileImageURL(imageRef)
                    progressBar.visibility = View.GONE
                }
                .addOnFailureListener {
                    Log.e("ProfileFragment", "Failed to upload image: ${it.message}")
                    progressBar.visibility = View.GONE
                }
        }
    }

    private fun compressBitmap(originalBitmap: Bitmap): Bitmap? {
        val width = originalBitmap.width
        val height = originalBitmap.height

        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(outputBitmap)
        val paint = android.graphics.Paint()
        val rect = android.graphics.Rect(0, 0, width, height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        canvas.drawCircle(
            (width / 2).toFloat(),
            (height / 2).toFloat(),
            (width / 2).toFloat(),
            paint
        )
        paint.xfermode =
            android.graphics.PorterDuffXfermode(android.graphics.PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(originalBitmap, rect, rect, paint)

        return outputBitmap
    }

    private fun saveProfileImageURL(imageRef: StorageReference) {
        imageRef.downloadUrl
            .addOnSuccessListener { uri ->
                profileImageURL = uri.toString()
            }
            .addOnFailureListener { exception ->
                Log.e("ProfileFragment", "Failed to save profile image URL: ${exception.message}")
            }
    }

    private fun loadProfileImageURL() {
        if (!hasProfileImageLoaded) {
            val imageRef = storageRef.child("${firebaseAuth.currentUser?.uid}.jpg")
            imageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    profileImageURL = uri.toString()
                    hasProfileImageLoaded = true
                }
                .addOnFailureListener { exception ->
                    Log.e(
                        "ProfileFragment",
                        "Failed to save profile image URL: ${exception.message}"
                    )
                }
        }
    }

    private fun loadProfileImage() {
        if (isAdded) {
            if (profileImageURL != null) {
                Glide.with(this)
                    .load(profileImageURL)
                    .circleCrop()
                    .into(profileImageView)
            } else {
                profileImageView.setImageResource(R.drawable.ic_profile_person)
            }
        }
    }

    override fun onDetach() {
        super.onDetach()
        Glide.with(this).clear(profileImageView)
    }

    interface LogoutListener {
        fun onLogout()
    }
}
