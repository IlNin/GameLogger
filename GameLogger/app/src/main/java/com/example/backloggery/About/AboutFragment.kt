package com.example.backloggery.About

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private lateinit var binding: FragmentAboutBinding

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.about_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }


}