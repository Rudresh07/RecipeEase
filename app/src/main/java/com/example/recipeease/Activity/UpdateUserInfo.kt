package com.example.recipeease.Activity

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.recipeease.Model.User
import com.example.recipeease.databinding.ActivityUpdateUserInfoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class UpdateUserInfo : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateUserInfoBinding
    private lateinit var imageShow: ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private var selectedImageURI: Uri? = null
    private var downloadableImageURI: String? = null
    private lateinit var saveUserButton: AppCompatButton
    private lateinit var userName: String

    companion object {
        const val PICK_IMAGE_REQUEST = 108
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateUserInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)

            imageShow = binding.img

            firebaseAuth = FirebaseAuth.getInstance()

            binding.btnSelect.setOnClickListener {
                openGallery()
            }

            saveUserButton = binding.btnChangeDetail as AppCompatButton
            saveUserButton.setOnClickListener {
                userName = binding.edtUserName.text.toString()

                if (selectedImageURI != null && userName.isNotEmpty()) {
                    Toast.makeText(this, "All good!", Toast.LENGTH_SHORT).show()
                    uploadImageToCloudStorage()
                } else {
                    Toast.makeText(this, "First select image and fill all fields!", Toast.LENGTH_SHORT).show()
                }
            }

        }

        private fun uploadImageToCloudStorage() {
            val progressiveDialog = ProgressDialog(this)
            progressiveDialog.setTitle("Uploading...")
            progressiveDialog.setMessage("Uploading your image..")
            progressiveDialog.show()

            val ref = FirebaseStorage.getInstance().reference
                .child("images/${UUID.randomUUID()}.jpg")

            selectedImageURI?.let { uri ->
                CoroutineScope(Dispatchers.Main).launch {
                    try {
                        withContext(Dispatchers.IO) {
                            val tasksnapshot = ref.putFile(uri).await()
                            downloadableImageURI = tasksnapshot.storage.downloadUrl.await().toString()
                        }
                        progressiveDialog.dismiss()
                        println("Image URI: $downloadableImageURI")
                        saveToDatabase()
                    }
                    catch (e: Exception) {
                        progressiveDialog.dismiss()
                        Toast.makeText(this@UpdateUserInfo, e.message, Toast.LENGTH_SHORT).show()
                        println("Failed To Upload: ${e.message}")
                    }
                }
            } ?: run {
                progressiveDialog.dismiss()
                Toast.makeText(this, "Image URI is null", Toast.LENGTH_SHORT).show()
                println("Image URI is null")
            }
        }

        private fun saveToDatabase() {
            CoroutineScope(Dispatchers.Main).launch {//running on main thread
                val progressDialog = ProgressDialog(this@UpdateUserInfo)
                progressDialog.setTitle("Saving User")
                progressDialog.setMessage("Please Wait..")
                progressDialog.show()

                val userId = firebaseAuth.currentUser?.uid

                if (userId!=null)
                {
                    val user = User(
                        userId = userId,
                        userName = userName,
                        imageUrl = downloadableImageURI!!
                    )

                    val db = FirebaseFirestore.getInstance()
                    try {
                        withContext(Dispatchers.IO){//running on input output thread
                            db.collection("UserDetail").document(userId).set(user).await()

                        }
                        progressDialog.dismiss()
                        Toast.makeText(this@UpdateUserInfo, "User Detail Updated successfully", Toast.LENGTH_SHORT).show()
                        clearEntries()
                    } catch (e:Exception){
                        progressDialog.dismiss()
                        Toast.makeText(this@UpdateUserInfo, "Error Updating User: ${e.message}", Toast.LENGTH_SHORT).show()
                        println("Error Updating User: ${e.message}")
                    }
                }

                else{
                    progressDialog.dismiss()
                    Toast.makeText(this@UpdateUserInfo, "UserID not found please login", Toast.LENGTH_SHORT).show()
                    println("UserID null")
                }


            }
        }
        private fun clearEntries() {
            // Clear the input fields
            binding.edtUserName.text.clear()
            // Clear the image view
            imageShow.setImageURI(null)
            selectedImageURI = null
        }

        private fun openGallery() {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

            if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
                selectedImageURI = data.data
                println("Selected image URI: $selectedImageURI")

                if (selectedImageURI != null) {
                    imageShow.setImageURI(selectedImageURI)
                }
            }
        }

    }
