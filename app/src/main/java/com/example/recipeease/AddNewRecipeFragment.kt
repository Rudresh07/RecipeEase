package com.example.recipeease

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.example.recipeease.Model.Recipe
import com.example.recipeease.databinding.FragmentAddNewRecipeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class AddNewRecipeFragment : Fragment() {

    private lateinit var binding: FragmentAddNewRecipeBinding
    private lateinit var imageShow: ImageView
    private lateinit var firebaseAuth: FirebaseAuth
    private var selectedImageURI: Uri? = null
    private var downloadableImageURI: String? = null
    private lateinit var saveRecipeButton: AppCompatButton
    private lateinit var recipeName: String
    private lateinit var ingredients: String
    private lateinit var instructions: String

    companion object {
        const val PICK_IMAGE_REQUEST = 108
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddNewRecipeBinding.inflate(inflater, container, false)
        imageShow = binding.img

        firebaseAuth = FirebaseAuth.getInstance()

        binding.btnSelect.setOnClickListener {
            openGallery()
        }

        saveRecipeButton = binding.btnSaveRecipe as AppCompatButton
        saveRecipeButton.setOnClickListener {
            instructions = binding.edtInstructions.text.toString()
            recipeName = binding.edtRecipeName.text.toString()
            ingredients = binding.edtIngredient.text.toString()

            if (selectedImageURI != null && instructions.isNotEmpty() && recipeName.isNotEmpty() && ingredients.isNotEmpty()) {
                Toast.makeText(context, "All good!", Toast.LENGTH_SHORT).show()
                uploadImageToCloudStorage()
            } else {
                Toast.makeText(context, "First select image and fill all fields!", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun uploadImageToCloudStorage() {
        val progressiveDialog = ProgressDialog(activity)
        progressiveDialog.setTitle("Uploading...")
        progressiveDialog.setMessage("Uploading your image..")
        progressiveDialog.show()

        val ref = FirebaseStorage.getInstance().reference
            .child("images/${UUID.randomUUID()}.jpg")

        selectedImageURI?.let { uri ->
            ref.putFile(uri)
                .addOnSuccessListener { taskSnapshot ->
                    progressiveDialog.dismiss()
                    taskSnapshot.storage.downloadUrl
                        .addOnSuccessListener { uri ->
                            downloadableImageURI = uri.toString()
                            println("Downloadable URI is: $uri")
                            saveToDatabase()
                        }
                }
                .addOnFailureListener { exception ->
                    progressiveDialog.dismiss()
                    Toast.makeText(context, "Failed to upload: ${exception.message}", Toast.LENGTH_SHORT).show()
                    println("Failed to upload: ${exception.message}")
                }
        } ?: run {
            progressiveDialog.dismiss()
            Toast.makeText(context, "Image URI is null", Toast.LENGTH_SHORT).show()
            println("Image URI is null")
        }
    }

    private fun saveToDatabase() {
        val progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Saving Recipe")
        progressDialog.setMessage("Please Wait..")
        progressDialog.show()

        val userId = firebaseAuth.currentUser?.uid

        val recipe = userId?.let {
            Recipe(
                recipeName = recipeName,
                ingredient = ingredients,
                instruction = instructions,
                imageUrl = downloadableImageURI!!,
                owner = it
            )
        }

        if (recipe != null) {
            FirebaseFirestore.getInstance().collection("Recipes").document(userId)
                .set(recipe)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    Toast.makeText(context, "Recipe added successfully!", Toast.LENGTH_SHORT).show()
//                    println("Document reference ID: ${documentReference.id}")

                    clearEntries()
                }
                .addOnFailureListener { exception ->
                    progressDialog.dismiss()
                    Toast.makeText(context, "Error adding Recipe: ${exception.message}", Toast.LENGTH_SHORT).show()
                    println("Error adding Recipe: ${exception.message}")
                }
        } else {
            progressDialog.dismiss()
            Toast.makeText(context, "Recipe is null", Toast.LENGTH_SHORT).show()
            println("Recipe is null")
        }
    }
    private fun clearEntries() {
        // Clear the input fields
        binding.edtInstructions.text.clear()
        binding.edtRecipeName.text.clear()
        binding.edtIngredient.text.clear()
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
