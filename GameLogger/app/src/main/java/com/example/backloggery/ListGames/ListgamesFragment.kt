package com.example.backloggery.ListGames

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.backloggery.Firebase.DatabaseViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentListgamesBinding
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

class ListgamesFragment : Fragment() {
    private lateinit var binding: FragmentListgamesBinding
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var games: MutableList<DocumentSnapshot>

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_listgames, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.list_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Enable the option menu on the action bar
        setHasOptionsMenu(true)

        // Initializes the DatabaseViewModel
        databaseViewModel = activity.run{ ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java)}

        checkDatabase()

        // Assign click listeners to the buttons
        binding.removeFilterButton.setOnClickListener {
            databaseViewModel.restoreFilteredFlag()
            it.setVisibility(View.GONE)
            games = databaseViewModel.games.value!!
            setRecyclerView()
            binding.insertNumberGames.text = databaseViewModel.getNumberOfGames().toString()
            Toast.makeText(context, "Filter removed", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    // Creates the option menu on the action bar
    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.listgames_menu, menu)
    }

    // Called when the user presses a button on the options menu
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        // The user has pressed the "Log out" button
        if (item?.itemId == R.id.add_button) {
            findNavController().navigate(R.id.action_listgamesFragment_to_addgameFragment)
        }
        else if (item?.itemId == R.id.filter_button) {
            findNavController().navigate(R.id.action_listgamesFragment_to_filterFragment)
        }
        else if (item?.itemId == R.id.clear_button) {
            findNavController().navigate(R.id.action_listgamesFragment_to_eraselistFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    // See if something has happened to the database
    private fun checkDatabase() {
        // If nothing new has happened. The fragment can read old values since they are still valid
        if (!databaseViewModel.getMustReadLatest()) {
            // No filter is applied
            if (!databaseViewModel.getFilteredFlag()) {
                games = databaseViewModel.games.value!!
                binding.insertNumberGames.text = databaseViewModel.getNumberOfGames().toString()
            }
            // A filter is applied
            else {
                games = databaseViewModel.filteredGames.value!!
                binding.removeFilterButton.setVisibility(View.VISIBLE)
                binding.insertNumberGames.text = databaseViewModel.getNumberOfFilteredGames().toString()
            }
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
                    val filteredFlag = databaseViewModel.getFilteredFlag()
                    readDatabase(filteredFlag)
                }
            }
        })
    }

    // Read the games from the database
    private fun readDatabase(flagFilter: Boolean) {
        // Read ALL games
        databaseViewModel.getGames()
        databaseViewModel.games.observe(viewLifecycleOwner, Observer {
            // The filter must be applied again since the list of games have changed
            if (flagFilter) {
                databaseViewModel.applyFilter()
                games = databaseViewModel.filteredGames.value!!
                setRecyclerView()
                binding.removeFilterButton.setVisibility(View.VISIBLE)
                binding.insertNumberGames.text = databaseViewModel.getNumberOfFilteredGames().toString()
            }
            else {
                games = it
                setRecyclerView()
                binding.insertNumberGames.text = databaseViewModel.getNumberOfGames().toString()
            }
        })

        // Read the latest 10 games
        databaseViewModel.getLatestGames()
        databaseViewModel.latestGames.observe(viewLifecycleOwner, Observer {
            // Send flag that the database has been read
            databaseViewModel.haveReadLatest()
        })
    }

    // Set the RecyclerView
    private fun setRecyclerView() {
        val adapter = GamesAdapter(games)
        binding.games.adapter = adapter
    }
}