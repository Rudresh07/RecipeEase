package com.example.recipeease.Authentication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeease.Activity.MainActivity
import com.example.recipeease.R
import com.example.recipeease.databinding.FragmentLoginBinding
import com.example.recipeease.utils
import com.google.firebase.auth.FirebaseAuth


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.GoSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)

        }



binding.Signin.setOnClickListener {
    val email = binding.email.text.toString()
    val pass = binding.password.text.toString()

    if (email.isNotEmpty() && pass.isNotEmpty())
    {
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
            if (it.isSuccessful)
            {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                requireActivity().finish()
            }
            else
            {
                utils.ShowToast(requireContext(), "Incorrect password or Email")
            }
        }
    }
    else
    {
        utils.ShowToast(requireContext(), "Please fill all credentials")
    }
}

        return binding.root
        }


}