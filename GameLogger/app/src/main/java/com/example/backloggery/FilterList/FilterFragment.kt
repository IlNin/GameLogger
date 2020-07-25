package com.example.backloggery.FilterList

import android.os.Bundle
import android.util.Log
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
import com.example.backloggery.databinding.FragmentFilterBinding

class FilterFragment : Fragment() {
    private lateinit var binding: FragmentFilterBinding
    private lateinit var databaseViewModel: DatabaseViewModel

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.filter_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initializes the DatabaseViewModel
        databaseViewModel = activity.run{ ViewModelProviders.of(this!!).get(DatabaseViewModel::class.java)}

        // Assign click listeners to the buttons
        binding.filterButton.setOnClickListener {
            applyFilter()
        }

        binding.anybeatenCheck.setOnClickListener {
            binding.beatenCheck.setChecked(false)
            binding.unbeatenCheck.setChecked(false)
        }

        binding.beatenCheck.setOnClickListener {
            binding.unbeatenCheck.setChecked(false)
            binding.anybeatenCheck.setChecked(false)
        }

        binding.unbeatenCheck.setOnClickListener {
            binding.beatenCheck.setChecked(false)
            binding.anybeatenCheck.setChecked(false)
        }

        binding.anydigitalCheck.setOnClickListener {
            binding.digitalCheck.setChecked(false)
            binding.physicalCheck.setChecked(false)
        }

        binding.digitalCheck.setOnClickListener {
            binding.physicalCheck.setChecked(false)
            binding.anydigitalCheck.setChecked(false)
        }

        binding.physicalCheck.setOnClickListener {
            binding.digitalCheck.setChecked(false)
            binding.anydigitalCheck.setChecked(false)
        }

        binding.anyfavoriteCheck.setOnClickListener {
            binding.favoriteCheck.setChecked(false)
            binding.unfavoriteCheck.setChecked(false)
        }

        binding.favoriteCheck.setOnClickListener {
            binding.unfavoriteCheck.setChecked(false)
            binding.anyfavoriteCheck.setChecked(false)
        }

        binding.unfavoriteCheck.setOnClickListener {
            binding.favoriteCheck.setChecked(false)
            binding.anyfavoriteCheck.setChecked(false)
        }

        binding.anyconsoleCheck.setOnClickListener {
            disableOtherPlatformButtons()
        }

        binding.pcCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.switchCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.xboneCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.ps4Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.wiiuCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.psvitaCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.ds3Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.wiiCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.ps3Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.xbox360Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.pspCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.dsCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.xboxCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.cubeCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.ps2Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.dreamcastCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.gbaCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.n64Check.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.psxCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.saturnCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.gbcCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.snesCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.genesisCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.gbCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.nesCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.masterCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.watchCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        binding.otherCheck.setOnClickListener {
            binding.anyconsoleCheck.setChecked(false)
        }

        return binding.root
    }

    private fun disableOtherPlatformButtons() {
        binding.pcCheck.setChecked(false)
        binding.switchCheck.setChecked(false)
        binding.xboneCheck.setChecked(false)
        binding.ps4Check.setChecked(false)
        binding.wiiuCheck.setChecked(false)
        binding.psvitaCheck.setChecked(false)
        binding.ds3Check.setChecked(false)
        binding.wiiCheck.setChecked(false)
        binding.ps3Check.setChecked(false)
        binding.xbox360Check.setChecked(false)
        binding.pspCheck.setChecked(false)
        binding.dsCheck.setChecked(false)
        binding.xboxCheck.setChecked(false)
        binding.cubeCheck.setChecked(false)
        binding.ps2Check.setChecked(false)
        binding.dreamcastCheck.setChecked(false)
        binding.gbaCheck.setChecked(false)
        binding.n64Check.setChecked(false)
        binding.psxCheck.setChecked(false)
        binding.saturnCheck.setChecked(false)
        binding.gbcCheck.setChecked(false)
        binding.snesCheck.setChecked(false)
        binding.genesisCheck.setChecked(false)
        binding.gbCheck.setChecked(false)
        binding.nesCheck.setChecked(false)
        binding.masterCheck.setChecked(false)
        binding.watchCheck.setChecked(false)
        binding.otherCheck.setChecked(false)
    }

    // Apply the filter using the user's settings
    private fun applyFilter() {
        // Get the consoles
        var platforms = mutableListOf<String>()
        var nPlatforms = 0
        if (binding.pcCheck.isChecked) {
            platforms.add(binding.pcCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.switchCheck.isChecked) {
            platforms.add(binding.switchCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.xboneCheck.isChecked) {
            platforms.add(binding.xboneCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.ps4Check.isChecked) {
            platforms.add(binding.ps4Check.text.toString())
            nPlatforms += 1
        }
        if (binding.wiiuCheck.isChecked) {
            platforms.add(binding.wiiuCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.psvitaCheck.isChecked) {
            platforms.add(binding.psvitaCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.ds3Check.isChecked) {
            platforms.add(binding.ds3Check.text.toString())
            nPlatforms += 1
        }
        if (binding.wiiCheck.isChecked) {
            platforms.add(binding.wiiCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.ps3Check.isChecked) {
            platforms.add(binding.ps3Check.text.toString())
            nPlatforms += 1
        }
        if (binding.xbox360Check.isChecked) {
            platforms.add(binding.xbox360Check.text.toString())
            nPlatforms += 1
        }
        if (binding.pspCheck.isChecked) {
            platforms.add(binding.pspCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.dsCheck.isChecked) {
            platforms.add(binding.dsCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.xboxCheck.isChecked) {
            platforms.add(binding.xboxCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.cubeCheck.isChecked) {
            platforms.add(binding.cubeCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.ps2Check.isChecked) {
            platforms.add(binding.ps2Check.text.toString())
            nPlatforms += 1
        }
        if (binding.dreamcastCheck.isChecked) {
            platforms.add(binding.dreamcastCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.gbaCheck.isChecked) {
            platforms.add(binding.gbaCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.n64Check.isChecked) {
            platforms.add(binding.n64Check.text.toString())
            nPlatforms += 1
        }
        if (binding.psxCheck.isChecked) {
            platforms.add(binding.psxCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.saturnCheck.isChecked) {
            platforms.add(binding.saturnCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.gbcCheck.isChecked) {
            platforms.add(binding.gbcCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.snesCheck.isChecked) {
            platforms.add(binding.snesCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.genesisCheck.isChecked) {
            platforms.add(binding.genesisCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.gbCheck.isChecked) {
            platforms.add(binding.gbCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.nesCheck.isChecked) {
            platforms.add(binding.nesCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.masterCheck.isChecked) {
            platforms.add(binding.masterCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.watchCheck.isChecked) {
            platforms.add(binding.watchCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.otherCheck.isChecked) {
            platforms.add(binding.otherCheck.text.toString())
            nPlatforms += 1
        }
        if (binding.anyconsoleCheck.isChecked) {
            platforms.add("Any")
            nPlatforms += 1
        }

        // Get the ratings
        var ratings = mutableListOf<String>()
        var nRatings = 0
        if (binding.noratingCheck.isChecked) {
            ratings.add(binding.noratingCheck.text.toString())
            nRatings += 1
        }
        if (binding.rating1Check.isChecked) {
            ratings.add(binding.rating1Check.text.toString())
            nRatings += 1
        }
        if (binding.rating2Check.isChecked) {
            ratings.add(binding.rating2Check.text.toString())
            nRatings += 1
        }
        if (binding.rating3Check.isChecked) {
            ratings.add(binding.rating3Check.text.toString())
            nRatings += 1
        }
        if (binding.rating4Check.isChecked) {
            ratings.add(binding.rating4Check.text.toString())
            nRatings += 1
        }
        if (binding.rating5Check.isChecked) {
            ratings.add(binding.rating5Check.text.toString())
            nRatings += 1
        }

        // Get the beaten preferences
        var preferencesBeaten = -1
        if (binding.beatenCheck.isChecked) {
            preferencesBeaten = 0
        }
        else if (binding.unbeatenCheck.isChecked) {
            preferencesBeaten = 1
        }
        else if (binding.anybeatenCheck.isChecked) {
            preferencesBeaten = 2
        }

        // Get the digital preferences
        var preferencesDigital = -1
        if (binding.digitalCheck.isChecked) {
            preferencesDigital = 0
        }
        else if (binding.physicalCheck.isChecked) {
            preferencesDigital = 1
        }
        else if (binding.anydigitalCheck.isChecked) {
            preferencesDigital = 2
        }

        // Get the favorite preferences
        var preferencesFavorite = -1
        if (binding.favoriteCheck.isChecked) {
            preferencesFavorite = 0
        }
        else if (binding.unfavoriteCheck.isChecked) {
            preferencesFavorite = 1
        }
        else if (binding.anyfavoriteCheck.isChecked) {
            preferencesFavorite = 2
        }

        if (nPlatforms > 0 && nRatings > 0 && preferencesBeaten >= 0 && preferencesDigital >= 0 && preferencesFavorite >= 0) {
            // Apply the filter thanks to the DatabaseViewModel
            databaseViewModel.applyFilter(platforms, ratings, preferencesBeaten, preferencesDigital, preferencesFavorite)
            Toast.makeText(context, "Filter applied successfully!", Toast.LENGTH_SHORT).show()
            getFragmentManager()!!.popBackStackImmediate()
        }
        else {
            Toast.makeText(context, "Please choose the correct number of parameters!", Toast.LENGTH_SHORT).show()
        }
    }
}