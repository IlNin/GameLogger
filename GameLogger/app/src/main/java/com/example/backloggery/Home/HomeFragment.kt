package com.example.backloggery.Home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.backloggery.Firebase.DatabaseViewModel
import com.example.backloggery.Firebase.LoginViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentHomeBinding
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var latestGames: MutableList<DocumentSnapshot>

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.home_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(false)

        // Apply the spinning animation to the loading image
        val spinning: Animation = AnimationUtils.loadAnimation(context, R.anim.loading_animation);
        binding.loading.startAnimation(spinning)

        // Enable the option menu on the action bar
        setHasOptionsMenu(true)

        // Initializes the LoginViewModel and DatabaseViewModel
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        databaseViewModel = activity.run{ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java)}

        // Observe if the user is logged in or not
        observeAuthenticationState()

        // See if something has happened to the database
        checkDatabase()

        // Assign click listeners to the buttons
        binding.addButton.setOnClickListener() { view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_addgameFragment)
        }

        binding.listButton.setOnClickListener() { view : View ->
            view.findNavController().navigate(R.id.action_homeFragment_to_listgamesFragment)
        }

        return binding.root
    }

    // Creates the option menu on the action bar
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.home_menu, menu)
    }

    // Called when the user presses a button on the options menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // The user has pressed the "Log out" button
        if (item?.itemId == R.id.logout_button) {
            AuthUI.getInstance().signOut(requireContext())
        }
        else if (item?.itemId == R.id.about_button) {
            findNavController().navigate(R.id.action_homeFragment_to_aboutFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    // Checks if the user is logged out, so that they can return to the title screen
    private fun observeAuthenticationState() {
        // Creates an observer to the variable authenticationState in LoginViewModel
        loginViewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                LoginViewModel.AuthenticationState.UNAUTHENTICATED -> {
                    activity!!.getViewModelStore().clear()
                    binding.root.findNavController().navigate(R.id.action_homeFragment_to_titleFragment)
                    Toast.makeText(context, "Logged out!", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    // See if something has happened to the database
    private fun checkDatabase() {
        // If nothing new has happened. The fragment can read old values since they are still valid
        if (!databaseViewModel.getMustReadLatest()) {
            latestGames = databaseViewModel.latestGames.value!!
            binding.insertTotal.text = databaseViewModel.getNumberOfGames().toString()
            binding.insertBeaten.text = databaseViewModel.getNumberOfBeatenGames().toString()
            binding.insertDigital.text = databaseViewModel.getNumberOfDigitalGames().toString()
            binding.insertFavorite.text = databaseViewModel.getNumberOfFavoriteGames().toString()
            setRecyclerView()
        }

        // The fragment listens for updates to the database
        databaseViewModel.mustReadLatest.observe(viewLifecycleOwner, Observer { flag ->
            // Something has happened
            if (flag == true) {
                var flagRead = false
                // A new game has been added and the database must be read again
                if (databaseViewModel.getAddedGameFlag() == 1) {
                    Toast.makeText(context, "Game saved successfully!", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreAddedGameFlag()
                    flagRead = true
                }
                // A new game couldn't be added. There's no need to read the database again!
                else if (databaseViewModel.getAddedGameFlag() == -1) {
                    Toast.makeText(context, "Error: the game couldn't be saved! ", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreAddedGameFlag()
                    databaseViewModel.haveReadLatest()
                }
                // A game has been edited. The database must be read again!
                else if (databaseViewModel.getEditedGameFlag() == 1) {
                    Toast.makeText(context, "Game edited successfully!", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreEditedGameFlag()
                    flagRead = true
                }
                // A game couldn't be edited. There's no need to read the database again!
                else if (databaseViewModel.getEditedGameFlag() == -1) {
                    Toast.makeText(context, "Error: the game couldn't be edited! ", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreEditedGameFlag()
                    databaseViewModel.haveReadLatest()
                }
                // A game has been deleted. The database must be read again!
                else if (databaseViewModel.getDeletedGameFlag() == 1) {
                    Toast.makeText(context, "Game deleted successfully", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreDeletedGameFlag()
                    flagRead = true
                }
                // A game couldn't be deleted. There's no need to read the database again!
                else if (databaseViewModel.getDeletedGameFlag() == -1) {
                    Toast.makeText(context, "Error: the game couldn't be deleted! ", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreDeletedGameFlag()
                    databaseViewModel.haveReadLatest()
                }
                // The entire database has been deleted!
                else if (databaseViewModel.getErasedFlag() == 1) {
                    Toast.makeText(context, "Your entire data has been deleted successfully!", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreErasedFlag()
                    flagRead = true
                }
                // The entire database couldn't be deleted!
                else if (databaseViewModel.getErasedFlag() == -1) {
                    Toast.makeText(context, "Error: your entire data couldn't be deleted!", Toast.LENGTH_SHORT).show()
                    databaseViewModel.restoreErasedFlag()
                    databaseViewModel.haveReadLatest()
                }
                // The database is read for the first time upon the login of the user
                else {
                    flagRead = true
                }

                if (flagRead) {
                    binding.addButton.isEnabled = false
                    binding.listButton.isEnabled = false
                    readDatabase()
                }
            }
        })
    }

    // Read the games from the database
    private fun readDatabase() {
        // Read the latest 10 games
        databaseViewModel.getLatestGames()
        databaseViewModel.latestGames.observe(viewLifecycleOwner, Observer {
            latestGames = it
            setRecyclerView()
        })

        // Read ALL games
        databaseViewModel.getGames()
        databaseViewModel.games.observe(viewLifecycleOwner, Observer {
            // Send flag that the database has been read
            databaseViewModel.haveReadLatest()

            // Enable the buttons again
            binding.addButton.isEnabled = true
            binding.listButton.isEnabled = true

            // Refresh the stats
            binding.insertTotal.text = databaseViewModel.getNumberOfGames().toString()
            binding.insertBeaten.text = databaseViewModel.getNumberOfBeatenGames().toString()
            binding.insertDigital.text = databaseViewModel.getNumberOfDigitalGames().toString()
            binding.insertFavorite.text = databaseViewModel.getNumberOfFavoriteGames().toString()
        })
    }

    // Set the RecyclerView
    private fun setRecyclerView() {
        // Delete the loading animation
        binding.loading.clearAnimation()
        binding.loading.setVisibility(View.GONE)

        // Prepare the RecyclerView
        val adapter = LatestGamesAdapter(latestGames)
        binding.latestGames.adapter = adapter
    }

}