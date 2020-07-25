package com.example.backloggery.Title

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.backloggery.Firebase.LoginViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentTitleBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class TitleFragment : Fragment() {
    private val SIGN_IN_REQUEST_CODE = 1
    private lateinit var binding: FragmentTitleBinding
    private lateinit var viewModel: LoginViewModel

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_title, container, false)

        // Assign click listeners to the buttons
        binding.aboutButton.setOnClickListener { view : View ->
            view.findNavController().navigate(R.id.action_titleFragment_to_aboutFragment)
        }

        binding.startButton.setOnClickListener {
            launchSignInFlow()
        }

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.title_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)

        // Initializes the LoginViewModel
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)

        // Observe if the user is logged in or not
        observeAuthenticationState()

        return binding.root
    }

    // Lets the user sign in
    private fun launchSignInFlow() {
        // The providers give users the option to sign in / register with their email or Google account.
        val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch the sign-in intent. We listen to the response of this activity with the SIGN_IN_REQUEST_CODE.
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), SIGN_IN_REQUEST_CODE)
    }

    // Checks if the user is logged in, so that they can access their home page
    private fun observeAuthenticationState() {
        // Creates an observer to the variable authenticationState in LoginViewModel
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    binding.root.findNavController().navigate(R.id.action_titleFragment_to_homeFragment)
                }
            }
        })
    }

    // Checks if the log in was successful
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in.
                Toast.makeText(context, "Successfully logged in!", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in failed.
                Toast.makeText(context, "Log in unsuccessful", Toast.LENGTH_SHORT).show()
            }
        }
    }
}