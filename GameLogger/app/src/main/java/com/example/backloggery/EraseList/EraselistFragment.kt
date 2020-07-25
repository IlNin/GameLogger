package com.example.backloggery.EraseList

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
import com.example.backloggery.Firebase.DatabaseViewModel
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentEraselistBinding

class EraselistFragment : Fragment() {
    private lateinit var binding: FragmentEraselistBinding
    private lateinit var databaseViewModel: DatabaseViewModel

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_eraselist, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.erase_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Set the color of the action bar
        actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor("#D0021B")))

        // Initializes the DatabaseViewModel
        databaseViewModel = activity.run { ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java) }

        // Assign click listeners to the buttons
        binding.eraseButton.setOnClickListener {
            eraseDatabase()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Set the color of the action bar
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setBackgroundDrawable(ColorDrawable(Color.parseColor("#008577")))
    }

    private fun eraseDatabase() {
        // Check if the device is offline
        if (!(activity as MainActivity).isOnline()) {
            Toast.makeText(context, "Your device is offline! Your entire database may be erased later!", Toast.LENGTH_SHORT).show()
        }

        if (binding.insertCommand.text.toString() == "imsure") {
            databaseViewModel.eraseDatabase()
            getFragmentManager()!!.popBackStackImmediate()
        }
        else {
            Toast.makeText(context, "Insert the command first", Toast.LENGTH_SHORT).show()
        }
    }
}