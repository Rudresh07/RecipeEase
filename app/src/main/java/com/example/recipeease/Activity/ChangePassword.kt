package com.example.recipeease.Activity

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.recipeease.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var currentPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var changePasswordButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        auth = FirebaseAuth.getInstance()

        currentPasswordEditText = findViewById(R.id.currentPasswordEditText)
        newPasswordEditText = findViewById(R.id.newPasswordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
        changePasswordButton = findViewById(R.id.changePasswordButton)

        changePasswordButton.setOnClickListener {
            val currentPassword = currentPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (newPassword == confirmPassword) {
                reauthenticateAndChangePassword(currentPassword, newPassword)
            } else {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun reauthenticateAndChangePassword(currentPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null) {
            val email = user.email
            if (email != null) {
                val credential = EmailAuthProvider.getCredential(email, currentPassword)

                user.reauthenticate(credential)
                    .addOnCompleteListener { reauthTask ->
                        if (reauthTask.isSuccessful) {
                            user.updatePassword(newPassword)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Log.e("ChangePassword", "Error updating password", updateTask.exception)
                                        Toast.makeText(this, "Error changing password", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Log.e("ChangePassword", "Error re-authenticating", reauthTask.exception)
                            Toast.makeText(this, "Error re-authenticating", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }


    }


