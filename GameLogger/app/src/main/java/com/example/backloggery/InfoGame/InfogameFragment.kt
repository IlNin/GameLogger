package com.example.backloggery.InfoGame

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.backloggery.MainActivity
import com.example.backloggery.R
import com.example.backloggery.databinding.FragmentInfogameBinding
import com.squareup.picasso.Picasso


class InfogameFragment : Fragment() {
    private lateinit var binding: FragmentInfogameBinding
    private lateinit var infogameViewModel: InfogameViewModel

    // Called when the fragment is first created
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Get a reference to the binding object and inflate the fragment views.
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_infogame, container, false)

        // Update the title on the action bar and the back button
        activity?.setTitle(R.string.info_screen)
        val actionBar = (activity as MainActivity).supportActionBar
        actionBar!!.setDisplayHomeAsUpEnabled(true)

        // Initializes the InfogameViewModel
        infogameViewModel =  ViewModelProviders.of(this!!).get(InfogameViewModel::class.java)

        // Get the values of the item that needs to be deleted from the Adapter
        val args = InfogameFragmentArgs.fromBundle(arguments!!)
        var title = args.title

        binding.title.text = title.toUpperCase()
        binding.disclaimer.setVisibility(View.GONE)

        infogameViewModel.findInfo(title)

        // Observe the various values
        infogameViewModel.imageUrl.observe(viewLifecycleOwner, Observer { imageUrl ->
            if (imageUrl != "-1") {
                binding.disclaimer.setVisibility(View.VISIBLE)
                val picasso = Picasso.Builder(context!!).loggingEnabled(true).build()
                picasso
                    .load(imageUrl)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.insertCover)
            }
            else {
                binding.insertCover.setVisibility(View.GONE)
            }
        })

        infogameViewModel.developer.observe(viewLifecycleOwner, Observer { developer ->
            binding.insertDeveloper.setText(developer)
        })

        infogameViewModel.publisher.observe(viewLifecycleOwner, Observer { publisher ->
            binding.insertPublisher.setText(publisher)
        })

        infogameViewModel.releaseDate.observe(viewLifecycleOwner, Observer { releaseDate ->
            binding.insertReleaseDate.setText(releaseDate)
        })

        infogameViewModel.genre.observe(viewLifecycleOwner, Observer { genre ->
            binding.insertGenre.setText(genre)
        })

        infogameViewModel.platforms.observe(viewLifecycleOwner, Observer { platforms ->
            binding.insertPlatform.setText(platforms)
        })


        infogameViewModel.description.observe(viewLifecycleOwner, Observer { description ->
            binding.insertDescription.setText(description)
        })

        infogameViewModel.imageUrl2.observe(viewLifecycleOwner, Observer { imageUrl ->
            Log.d("System.out.println", imageUrl)
            if (imageUrl != "-1") {
                val picasso = Picasso.Builder(context!!).loggingEnabled(true).build()
                picasso
                    .load(imageUrl)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.insertBackCover)
            }
            else {
                binding.insertBackCover.setVisibility(View.GONE)
            }
        })

        return binding.root
    }


}