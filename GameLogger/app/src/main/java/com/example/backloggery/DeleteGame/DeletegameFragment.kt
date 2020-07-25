package com.example.backloggery.DeleteGame

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.backloggery.Firebase.DatabaseViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentDeletegameBinding
import kotlinx.android.synthetic.main.fragment_deletegame.view.*

class DeletegameFragment : Fragment() {
    private lateinit var binding: FragmentDeletegameBinding
    private lateinit var databaseViewModel: DatabaseViewModel

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_deletegame, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.delete_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Set the color of the action bar
        actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D0021B")))

        // Initializes the DatabaseViewModel
        databaseViewModel = activity.run{ ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java)}

        // Get the values of the item that needs to be deleted from the Adapter
        val args = DeletegameFragmentArgs.fromBundle(arguments!!)
        binding.insertTitle.text = args.title
        binding.insertDeveloper.text = args.developer
        binding.insertPlatform.text = args.platform
        binding.Beaten.setChecked(args.beaten)
        binding.Digital.setChecked(args.digital)
        binding.Favorite.setChecked(args.favorite)
        binding.insertRating.text = args.rating
        binding.insertId.text = args.id

        // Assigns click listeners to the buttons
        binding.yesButton.setOnClickListener() {
            deleteGame()
        }
        binding.noButton.setOnClickListener() {
            getFragmentManager()!!.popBackStackImmediate()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set the color of the action bar
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#008577")))
    }

    // Delete the game assigned to this fragment from the database
    fun deleteGame() {
        // Check if the device is offline
        if (!(activity as MainActivity).isOnline()) {
            Toast.makeText(context, "Your device is offline! The game may be deleted later!", Toast.LENGTH_SHORT).show()
        }

        // Gets the id of the game to be deleted
        val id = binding.insertId.text.toString()

        // Deletes the game from the database thanks to the viewModel
        databaseViewModel.deleteGame(id)

        getFragmentManager()!!.popBackStackImmediate()
    }
}
