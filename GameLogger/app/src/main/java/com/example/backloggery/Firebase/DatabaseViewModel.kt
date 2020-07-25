package com.example.backloggery.Firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class DatabaseViewModel : ViewModel() {
    // User authentication
    private val user = FirebaseAuth.getInstance().currentUser!!.uid;

    // Access Cloud Firestore instance
    private val db = FirebaseFirestore.getInstance()

    // The current list of games from the FireBase Database and their number
    private val _games = MutableLiveData<MutableList<DocumentSnapshot>>()
    val games: LiveData<MutableList<DocumentSnapshot>>
        get() = _games

    // The latest games added to the Firebase Database
    private val _latestGames = MutableLiveData<MutableList<DocumentSnapshot>>()
    val latestGames: LiveData<MutableList<DocumentSnapshot>>
        get() = _latestGames

    // Get the games after a filter is applied to them
    private val _filteredGames = MutableLiveData<MutableList<DocumentSnapshot>>()
    val filteredGames: LiveData<MutableList<DocumentSnapshot>>
        get() = _filteredGames

    // Indicates to the fragments if something happened to the database
    private val _mustReadLatest = MutableLiveData<Boolean>()
    val mustReadLatest: LiveData<Boolean>
        get() = _mustReadLatest

    // Indicates if a game has been added correctly (-1: error, 1: success)
    private var addedGameFlag: Int = 0

    // Indicates if a game has been deleted correctly (-1: error, 1: success)
    private var deletedGameFlag: Int = 0

    // Indicates if a game has been edited correctly (-1: error, 1: success)
    private var editedGameFlag: Int = 0

    // Indicates if a filter has been applied
    private var filteredFlag: Boolean = false

    // Indicates if the database has been erased (-1: error, 1: success)
    private var erasedFlag: Int = 0


    // Stats
    private var nGames: Int = 0 // # of games
    private var nBeaten: Int = 0 // # of beaten games
    private var nDigital: Int = 0 // # of digital games
    private var nFavorite: Int = 0 // # of favorite games
    private var nFiltered: Int = 0 // # of filtered games

    // Filters currently applied
    private lateinit var platforms: List<String>
    private lateinit var ratings: List<String>
    private var preferencesBeaten: Int = -1
    private var preferencesDigital: Int = -1
    private var preferencesFavorite: Int = -1

    init {
        Log.d("System.out.println", "I've created the ViewModel!")
        _mustReadLatest.value = true
    }

    // Get a list of ALL the user's games from the database
    fun getGames() {
        // Prepare the query
        val query = db.collection(user).orderBy("name")

        // Execute the query and get the list of games
        query.get().addOnSuccessListener {
            // Reset all stats
            resetStats()
            // Get total number of games
            for (document in it) {
                nGames += 1
                if (document.get("beaten").toString() == "true") {
                    nBeaten += 1
                }
                if (document.get("digital").toString() == "true") {
                    nDigital += 1
                }
                if (document.get("favorite").toString() == "true") {
                    nFavorite += 1
                }
            }
            Log.d("System.out.println", "I've read $nGames games!")
            _games.value = it.documents
        }
    }

    // Get the last 10 games added to the database from the user
    fun getLatestGames() {
        // Prepare the query
        val query = db.collection(user).orderBy("timestamp", Query.Direction.DESCENDING).limit(10)

        // Execute the query and get the list of games
        query.get().addOnSuccessListener {
            Log.d("System.out.println", "I've read the latest games!")
            _latestGames.value = it.documents
        }
    }

    // Add game to the database
    fun addGame(name: String, developer: String, platform: String, rating: String, beaten: Boolean, digital: Boolean, favorite: Boolean) {
        // Create a new game instance
        val game = hashMapOf(
            "name" to name,
            "developer" to developer,
            "platform" to platform,
            "rating" to rating,
            "beaten" to beaten,
            "digital" to digital,
            "favorite" to favorite,
            "timestamp" to FieldValue.serverTimestamp()
        )

        // Add a new document with a generated ID
        db.collection(user)
            .add(game)
            .addOnSuccessListener {
                Log.d("System.out.println", "I've added a new game!")
                addedGameFlag = 1
                _mustReadLatest.value = true
            }
            .addOnFailureListener {
                addedGameFlag = -1
                _mustReadLatest.value = true
            }
    }

    // Deletes a game
    fun deleteGame(id: String) {
        // Prepare the query
        val query = db.collection(user).document(id)

        // Execute the query and get the list of games to delete
        query.delete()
            .addOnSuccessListener {
                Log.d("System.out.println", "Game successfully deleted!")
                deletedGameFlag = 1
                _mustReadLatest.value = true
        }
            .addOnFailureListener {
                deletedGameFlag = -1
                _mustReadLatest.value = true
        }

    }

    // Edit a game inside the database
    fun editGame(id: String, name: String, developer: String, platform: String, rating: String, beaten: Boolean, digital: Boolean, favorite: Boolean) {
        // Game with the new parameters
        val game = hashMapOf(
            "name" to name,
            "developer" to developer,
            "platform" to platform,
            "rating" to rating,
            "beaten" to beaten,
            "digital" to digital,
            "favorite" to favorite
        )

        // Prepare the query
        val query = db.collection(user).document(id)

        // Execute query and apply changes to the game
        query.set(game, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("System.out.println", "Game successfully edited!")
                editedGameFlag = 1
                _mustReadLatest.value = true
            }
            .addOnFailureListener {
                editedGameFlag = -1
                _mustReadLatest.value = true
            }
    }

    // Apply a filter to the games according to the parameters chosen by the user
    fun applyFilter(platforms: List<String>, ratings: List<String>, preferencesBeaten: Int, preferencesDigital: Int, preferencesFavorite: Int) {
        // Refresh the filters currently saved in the ViewModel
        this.platforms = platforms
        this.ratings = ratings
        this.preferencesBeaten = preferencesBeaten
        this.preferencesDigital = preferencesDigital
        this.preferencesFavorite = preferencesFavorite

        applyFilter()
    }

    // Apply a filter to the games according to the parameters chosen previously
    fun applyFilter() {
        var currentGames = mutableListOf<DocumentSnapshot>()
        // Cycle through every game in games
        for (game in _games.value!!) {

            // Filters the platforms
            for (platform in platforms) {
                if (game.data!!["platform"] == platform || platform == "Any") {

                    // Filters the ratings
                    for (rating in ratings) {
                        if (game.data!!["rating"] == rating) {

                            // Filters the beaten preferences
                            if ((preferencesBeaten == 0 && game.data!!["beaten"] == true) || (preferencesBeaten == 1 && game.data!!["beaten"] == false) || (preferencesBeaten == 2)) {

                                // Filter the digital preferences
                                if ((preferencesDigital == 0 && game.data!!["digital"] == true) || (preferencesDigital == 1 && game.data!!["digital"] == false) || (preferencesDigital == 2)) {

                                    // Filter the favorite preferences
                                    if ((preferencesFavorite == 0 && game.data!!["favorite"] == true) || (preferencesFavorite == 1 && game.data!!["favorite"] == false) || (preferencesFavorite == 2)) {
                                        currentGames.add(game)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Log.d("System.out.println", "I've applied a filter!")
        filteredFlag = true
        nFiltered = currentGames.size
        _filteredGames.value = currentGames
    }

    // Erase every single game of an user
    fun eraseDatabase() {
        // Erase all games one by one
        for (game in _games.value!!) {
            db.collection(user).document(game.id).delete()
        }

        // See if all games have been deleted
        var flag = true
        db.collection(user).get()
            .addOnSuccessListener {
                for (game in it) {
                    flag = false
                }
            }
        if (flag) {
            Log.d("System.out.println", "Database successfully erased!")
            erasedFlag = 1
            _mustReadLatest.value = true
        }
        else {
            erasedFlag = -1
            _mustReadLatest.value = true
        }
    }

    // Stats return methods
    fun getNumberOfGames(): Int {
        return nGames
    }

    fun getNumberOfBeatenGames(): Int {
        return nBeaten
    }

    fun getNumberOfDigitalGames(): Int {
        return nDigital
    }

    fun getNumberOfFavoriteGames(): Int {
        return nFavorite
    }

    fun getNumberOfFilteredGames(): Int {
        return nFiltered
    }

    // Flag set/get methods
    fun getMustReadLatest(): Boolean {
        return _mustReadLatest.value!!
    }

    fun haveReadLatest() {
        _mustReadLatest.value = false
    }

    fun getAddedGameFlag(): Int {
        return addedGameFlag
    }

    fun restoreAddedGameFlag() {
        addedGameFlag = 0
    }

    fun getDeletedGameFlag(): Int {
        return deletedGameFlag
    }

    fun restoreDeletedGameFlag() {
        deletedGameFlag = 0
    }

    fun getEditedGameFlag(): Int {
        return editedGameFlag
    }

    fun restoreEditedGameFlag() {
        editedGameFlag = 0
    }

    fun getFilteredFlag(): Boolean {
        return filteredFlag
    }

    fun restoreFilteredFlag() {
        filteredFlag = false
    }

    fun getErasedFlag(): Int {
        return erasedFlag
    }

    fun restoreErasedFlag() {
        erasedFlag = 0
    }

    // Reset all stats values
    fun resetStats() {
        nGames = 0
        nBeaten = 0
        nDigital = 0
        nFavorite = 0
        nFiltered = 0
    }

}