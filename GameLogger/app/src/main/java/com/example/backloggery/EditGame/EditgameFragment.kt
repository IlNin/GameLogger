package com.example.backloggery.EditGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.backloggery.Firebase.DatabaseViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentAddgameBinding
import com.example.backloggery.databinding.FragmentEditgameBinding
import com.google.firebase.auth.FirebaseAuth


class EditgameFragment : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentEditgameBinding
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var id: String

    // Spinners parameters
    private var platforms = arrayOf("<Select Platform>", "PC",
        "Nintendo Switch", "Xbox One", "PlayStation 4", "Wii U",
        "PlayStation Vita", "Nintendo 3DS",
        "Wii", "PlayStation 3", "Xbox 360",
        "PlayStation Portable", "Nintendo DS",
        "Xbox", "Nintendo GameCube", "PlayStation 2", "Dreamcast",
        "Game Boy Advance",
        "Nintendo 64", "PlayStation", "Sega Saturn",
        "Game Boy Color",
        "Super NES", "Sega Genesis",
        "Game Boy",
        "NES", "Sega Master System",
        "Game & Watch",
        "Other")
    private var ratings = arrayOf("<No Rating>", "1/5", "2/5", "3/5", "4/5", "5/5")
    private var spinnerPlatform: Spinner? = null
    private var spinnerRating: Spinner? = null
    private var chosenPlatform: Int = 0
    private var chosenRating: Int = 0

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_editgame, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.edit_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initializes the DatabaseViewModel
        databaseViewModel = activity.run{ ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java)}

        // Add a listener to the spinners for when its items are selected
        spinnerPlatform = binding.spinnerPlatform
        spinnerRating = binding.spinnerRating
        spinnerPlatform!!.setOnItemSelectedListener(this)
        spinnerRating!!.setOnItemSelectedListener(this)

        // Initialization of an ArrayAdapter which will handle the elements inside a spinner
        val aa1 = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, platforms)
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val aa2 = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, ratings)
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Assign the ArrayAdapter to the spinners
        spinnerPlatform!!.setAdapter(aa1)
        spinnerRating!!.setAdapter(aa2)

        // Get the values of the item that needs to be deleted from the Adapter
        val args = EditgameFragmentArgs.fromBundle(arguments!!)
        binding.editName.setText(args.title)
        binding.editDeveloper.setText(args.developer)
        binding.Beaten.setChecked(args.beaten)
        binding.Digital.setChecked(args.digital)
        binding.Favorite.setChecked(args.favorite)
        setPlatform(args.platform)
        setRating(args.rating)
        id = args.id

        // Assign click listeners to the buttons
        binding.editButton.setOnClickListener {
            editGame()
        }

        return binding.root
    }

    // Override of Adapterview functions for handling the spinner
    override fun onNothingSelected(arg0: AdapterView<*>) { }
    override fun onItemSelected(arg0: AdapterView<*>, arg1: View, position: Int, id: Long) {
        // Get the position of the platform selected
        if (arg0.id == R.id.spinnerPlatform) {
            chosenPlatform = position;
        }

        // Get the position of the rating selected
        if (arg0.id == R.id.spinnerRating) {
            chosenRating = position;
        }
    }

    // Set the right element inside the spinnerPlatform
    private fun setPlatform(platform: String) {
        var position = 0
        var counter = 0
        for (item in platforms) {
            if (item == platform) {
                position = counter
            }
            counter += 1
        }
        binding.spinnerPlatform.setSelection(position)
    }

    // Set the right element inside the spinnerRating
    private fun setRating(rating: String?) {
        var position = 0
        var counter = 0
        for (item in ratings) {
            if (item == rating) {
                position = counter
            }
            counter += 1
        }
        binding.spinnerRating.setSelection(position)
    }

    // Edit a game inside a database
    private fun editGame() {
        // Get values from editText boxes
        var name = binding.editName.text.toString()
        var developer = binding.editDeveloper.text.toString()
        var platform = platforms[chosenPlatform]
        var rating = ratings[chosenRating]
        var beaten = binding.Beaten.isChecked
        var digital = binding.Digital.isChecked
        var favorite = binding.Favorite.isChecked

        // Check if they are not empty
        if (name.trim().length > 0 && chosenPlatform != 0) {
            // Check if the device is offline
            if (!(activity as MainActivity).isOnline()) {
                Toast.makeText(context, "Your device is offline! Your game may be edited later!", Toast.LENGTH_SHORT).show()
            }
            // Add the game to the database via the DatabasaViewModel
            databaseViewModel.editGame(id, name, developer, platform, rating, beaten, digital, favorite)
            getFragmentManager()!!.popBackStackImmediate()
        }

        // Else insert a warning
        else {
            Toast.makeText(context, "Please fill all the fields! ", Toast.LENGTH_SHORT).show()
        }
    }

}