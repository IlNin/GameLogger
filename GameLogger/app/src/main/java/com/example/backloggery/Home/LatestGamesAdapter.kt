package com.example.backloggery.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.backloggery.R
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.game_item_view.view.*

class LatestGamesAdapter(games: MutableList<DocumentSnapshot>): RecyclerView.Adapter<LatestGamesItemViewHolder>() {
    private var games: MutableList<DocumentSnapshot>
    private val size: Int
    private val empty: Boolean

    init {
        this.games = games
        size = games.size
        if (size == 0) {
            empty = true
        }
        else {
            empty = false
        }
    }

    override fun getItemCount() =
        if (size > 5) {
            5
        }
        else if (size > 0) {
            size
        }
        else {
            1
        }

    override fun onBindViewHolder(holder: LatestGamesItemViewHolder, position: Int) {
        // If the list is empty, it returns only the 'Looks like this list is empty!' message
        if (empty) {
            return
        }

        try {
            // Get the values from the document
            val item = games[position]

            // Initializes the values of the game with the ones gained from the document
            holder.layout.insert_title.text = item.get("name").toString()
            holder.layout.insert_developer.text = item.get("developer").toString()
            holder.layout.insert_platform.text = item.get("platform").toString()
            holder.layout.Beaten.setChecked(item.get("beaten").toString().toBoolean())
            holder.layout.Digital.setChecked(item.get("digital").toString().toBoolean())
            holder.layout.Favorite.setChecked(item.get("favorite").toString().toBoolean())
            val rating = item.get("rating").toString()
            if (rating.equals("<Select Rating>")) {
                holder.layout.insert_rating.text = "<No rating>"
            }
            else {
                holder.layout.insert_rating.text = rating
            }
            holder.layout.insert_id.text = item.id

            // Navigates to the DeletegameFragment and sends the game's parameters
            holder.layout.delete_button.setOnClickListener() {
                val action = HomeFragmentDirections.actionHomeFragmentToDeletegameFragment(holder.layout.insert_title.text.toString(),
                                                            holder.layout.insert_developer.text.toString(),
                                                            holder.layout.insert_platform.text.toString(),
                                                            holder.layout.Beaten.isChecked,
                                                            holder.layout.Digital.isChecked,
                                                            holder.layout.Favorite.isChecked,
                                                            holder.layout.insert_rating.text.toString(),
                                                            holder.layout.insert_id.text.toString())
                it.findNavController().navigate(action)
            }

            // Navigates to the EditgameFragment and sends the game's parameters
            holder.layout.edit_button.setOnClickListener() {
                val action = HomeFragmentDirections.actionHomeFragmentToEditgameFragment(holder.layout.insert_title.text.toString(),
                    holder.layout.insert_developer.text.toString(),
                    holder.layout.insert_platform.text.toString(),
                    holder.layout.Beaten.isChecked,
                    holder.layout.Digital.isChecked,
                    holder.layout.Favorite.isChecked,
                    holder.layout.insert_rating.text.toString(),
                    holder.layout.insert_id.text.toString())
                it.findNavController().navigate(action)
            }

            // Navigates to the Infogamefragment and sends the game's name
            holder.layout.info_button.setOnClickListener() {
                val action = HomeFragmentDirections.actionHomeFragmentToInfogameFragment(holder.layout.insert_title.text.toString())
                it.findNavController().navigate(action)
            }
        }
        catch (e: IndexOutOfBoundsException) { }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestGamesItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        if (empty) {
            val view = layoutInflater.inflate(R.layout.empty_item_home_view, parent, false) as ConstraintLayout
            return LatestGamesItemViewHolder(view)
        }
        else {
            val view = layoutInflater.inflate(R.layout.game_item_view, parent, false) as ConstraintLayout
            return LatestGamesItemViewHolder(view)
        }
    }

}