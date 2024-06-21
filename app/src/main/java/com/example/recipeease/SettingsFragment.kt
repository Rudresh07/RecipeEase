package com.example.recipeease

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.recipeease.Activity.ChangePassword
import com.example.recipeease.Activity.UpdateUserInfo
import com.example.recipeease.Authentication.AuthenticationActivity
import com.example.recipeease.Model.User
import com.example.recipeease.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        mAuth = FirebaseAuth.getInstance()


        binding.editUser.setOnClickListener {
            navigateToUpdateUserActivity()
        }

        //logout
        binding.logout.setOnClickListener {
            mAuth.signOut()
            navigateToAuthenticationActivity()
        }

        binding.ChangePassword.setOnClickListener {
            navigateToChangePasswordActivity()
        }


        binding.Share.setOnClickListener {
            //implement the intent to share the link of app
        }

        binding.aboutDeveloper.setOnClickListener {
            OpenDeveloperPortfolio()
        }
        binding.PrivacyPolicy.setOnClickListener {
            showPrivacyPolicyDialog()
        }
        //contact
        binding.Contact.setOnClickListener {
            val intent=Intent(Intent.ACTION_SENDTO,Uri.fromParts("mailto","rudresh.patel0707@gmail.com",null))
            startActivity(Intent.createChooser(intent,"Send Mail..."))
        }

        getDataFromFirebase()




        return binding.root
    }

    private fun getDataFromFirebase(){

        CoroutineScope(Dispatchers.IO).launch {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId!=null)
            {
                val db = FirebaseFirestore.getInstance()

                try {
                    val documentsnapshot = db.collection("UserDetail").document(userId).get().await()

                    val user = documentsnapshot.toObject(User::class.java)

                    if (user!=null)
                    {
                        withContext(Dispatchers.Main){
                            binding.userName.text = user.userName
                            Picasso.get().load(user.imageUrl).into(binding.userImage)
                        }

                    }
                }
                catch (e:Exception){
                    Toast.makeText(requireContext(), "Error fetching user data: ${e.message}", Toast.LENGTH_SHORT).show()
                    println("Error fetching user data: ${e.message}")
                }
            }

            else{
                Toast.makeText(requireContext(), "Please Login first", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun navigateToUpdateUserActivity(){
        val intent = Intent(requireActivity(), UpdateUserInfo::class.java)
        startActivity(intent)
        requireActivity()
    }

    private fun navigateToChangePasswordActivity(){
        val intent = Intent(requireActivity(), ChangePassword::class.java)
        startActivity(intent)
        requireActivity()
    }

    private fun OpenDeveloperPortfolio() {
        // Replace "YOUR_DEVELOPER_PORTFOLIO_LINK" with the actual link to your portfolio
        val portfolioUrl = "https://rudresh-portfolio.vercel.app/"

        // Create an Intent with ACTION_VIEW and the URL as data
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(portfolioUrl))

        // Start the activity to open the link in Chrome (or a suitable browser)
        startActivity(intent)
    }

    private fun navigateToAuthenticationActivity() {
        val intent = Intent(requireActivity(), AuthenticationActivity::class.java)
        startActivity(intent)
        requireActivity().finish() // Finish the current activity
    }

    private fun navigateToFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showPrivacyPolicyDialog() {
        val dialog = Dialog(requireContext())
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_layout, null)
        dialog.setContentView(view)

        // Apply the custom background with rounded corners
        view.background = resources.getDrawable(R.drawable.dialogbox_bg, null)

        val privacyPolicyTextView: TextView = view.findViewById(R.id.privacyPolicyTextView)
        val closeButton: Button = view.findViewById(R.id.closeButton)

        // Set the privacy policy text
        privacyPolicyTextView.text = getPrivacyPolicyText()

        // Set close button action
        closeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getPrivacyPolicyText(): String {
        return """
            Privacy Policy

            Effective Date: 12/05/2024

            1. Introduction

            Welcome to RecipeEase. We are committed to protecting your privacy. This Privacy Policy explains how we collect, use, disclose, and safeguard your information when you use our mobile application (the "App"). Please read this privacy policy carefully. If you do not agree with the terms of this privacy policy, please do not access the App.

            2. Information We Collect

            We may collect and process the following types of information:
            2.1 Personal Information
            - Email Address: Required for registration, login, and account management.
            - Name: Optional; used to personalize your experience.
            - Profile Picture: Optional; used to personalize your profile.

            2.2 Non-Personal Information
            - Usage Data: Information about how you use the App, including interaction data, logs, and other similar information.
            - Device Information: Information about your device, including the type, operating system, and unique device identifiers.

            3. How We Use Your Information

            We use the information we collect in the following ways:
            - To facilitate account creation and login processes.
            - To personalize your experience within the App.
            - To send administrative information, such as updates and changes to our terms, conditions, and policies.
            - To respond to your comments, questions, and provide customer service.
            - To monitor and analyze usage and trends to improve your experience with the App.
            - To protect against fraudulent or illegal activity.

            4. Sharing Your Information

            We do not sell, trade, or otherwise transfer your Personal Information to outside parties except in the following circumstances:
            - Service Providers: We may share information with third-party service providers who perform services on our behalf, such as data analysis, email delivery, hosting services, customer service, and marketing assistance.
            - Legal Obligations: We may disclose your information where required to do so by law or in response to valid requests by public authorities (e.g., a court or a government agency).
            - Business Transfers: We may share or transfer your information in connection with, or during negotiations of, any merger, sale of company assets, financing, or acquisition of all or a portion of our business to another company.

            5. Data Security

            We use administrative, technical, and physical security measures to help protect your Personal Information. While we have taken reasonable steps to secure the Personal Information you provide to us, please be aware that despite our efforts, no security measures are perfect or impenetrable, and no method of data transmission can be guaranteed against any interception or other type of misuse.

            6. Third-Party Services

            The App may contain links to third-party websites and services. We are not responsible for the privacy practices or the content of these third parties. Please be aware that the privacy policy of such third parties may differ from our own.

            7. Children's Privacy

            The App is not intended for use by children under the age of 13. We do not knowingly collect personal information from children under 13. If we learn that we have collected personal information from a child under 13, we will delete that information as quickly as possible.

            8. Your Data Protection Rights

            Depending on your location, you may have the following rights regarding your personal data:
            - Access: The right to request copies of your personal data.
            - Rectification: The right to request that we correct any information you believe is inaccurate.
            - Erasure: The right to request that we erase your personal data, under certain conditions.
            - Restrict Processing: The right to request that we restrict the processing of your personal data, under certain conditions.
            - Object to Processing: The right to object to our processing of your personal data, under certain conditions.
            - Data Portability: The right to request that we transfer the data that we have collected to another organization, or directly to you, under certain conditions.

            If you make a request, we have one month to respond to you. If you would like to exercise any of these rights, please contact us at our provided contact information.

            9. Changes to This Privacy Policy

            We may update our Privacy Policy from time to time. We will notify you of any changes by posting the new Privacy Policy on this page. You are advised to review this Privacy Policy periodically for any changes. Changes to this Privacy Policy are effective when they are posted on this page.

            10. Contact Us

            If you have any questions or suggestions about our Privacy Policy, do not hesitate to contact us at:

            
            Email: rudreshpatelbhu@gmail.com
            Address: Varanasi
        """.trimIndent()
    }
}
