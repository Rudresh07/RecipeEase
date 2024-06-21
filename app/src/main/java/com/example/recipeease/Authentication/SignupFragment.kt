package com.example.recipeease.Authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipeease.R
import com.example.recipeease.databinding.FragmentSignupBinding
import com.example.recipeease.utils
import com.google.firebase.auth.FirebaseAuth


class SignupFragment : Fragment() {

private lateinit var binding:FragmentSignupBinding
private lateinit var firebaseAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.GoSignin.setOnClickListener {
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

        }

        binding.SignUpBtn.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val cnfPass=binding.cnfpassword.text.toString()
            val name = binding.name.text.toString()


            if (email.isNotEmpty() && name.isNotEmpty() && cnfPass.isNotEmpty() && email.isNotEmpty())
            {
                if (pass == cnfPass)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                        if (it.isSuccessful)
                        {
                            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                        }
                            else{
                                utils.ShowToast(requireContext(), it.exception.toString())
                        }
                    }
                }
                else
                {
                    utils.ShowToast(requireContext(),"Password is not matching")
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