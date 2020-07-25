package com.example.backloggery.InfoGame

import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.backloggery.R
import com.squareup.picasso.Picasso
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class InfogameViewModel : ViewModel() {
    private val url = "https://rawg-video-games-database.p.rapidapi.com/games/"
    private val client = OkHttpClient()

    private val _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    private val _developer = MutableLiveData<String>()
    val developer: LiveData<String>
        get() = _developer

    private val _publisher = MutableLiveData<String>()
    val publisher: LiveData<String>
        get() = _publisher

    private val _releaseDate = MutableLiveData<String>()
    val releaseDate: LiveData<String>
        get() = _releaseDate

    private val _genre = MutableLiveData<String>()
    val genre: LiveData<String>
        get() = _genre

    private val _platforms = MutableLiveData<String>()
    val platforms: LiveData<String>
        get() = _platforms

    private val _description = MutableLiveData<String>()
    val description: LiveData<String>
        get() = _description

    private val _imageUrl2 = MutableLiveData<String>()
    val imageUrl2: LiveData<String>
        get() = _imageUrl2


    // Makes a request to the RAWG database to get info about the current game
    fun findInfo(title: String) {
        // Makes the title compatible with the RAWG database
        var parsedTitle = title.toLowerCase()
                                .replace(" ", "-")
                                .replace("'", "")
                                .replace(":", "")
                                .replace(".", "")
                                .replace("!", "")
                                .replace("-&-", "-")
        var url = this.url+parsedTitle
        Log.d("System.out.println", url)

        // Prepares the request
        val request = Request.Builder()
            .url(url)
            .addHeader("x-rapidapi-host", "rawg-video-games-database.p.rapidapi.com")
            .addHeader("x-rapidapi-key", "3a602fed4amshf58a4926f9621bep1312aajsn764ff31b0e71")
            .build()

        // Makes the requests and waits for the response
        client.newCall(request).enqueue(object : Callback {
            // The call fails
            override fun onFailure(call: Call, e: IOException) {
                _imageUrl.postValue("-1")
                _developer.postValue("???")
                _publisher.postValue("???")
                _releaseDate.postValue("???")
                _genre.postValue("???")
                _platforms.postValue("???")
                _description.postValue("Sorry, couldn't find anything on the RAWG database... :(")
                _imageUrl2.postValue("-1")
            }

            // The call receives a response
            override fun onResponse(call: Call, response: Response) {
                // Parse the html response into a JSON object
                var obj = JSONObject(response.body()?.string()!!)

                // Check if there's a redirect
                try {
                    if (obj.get("redirect").toString() == "true") {
                        findInfo(obj.get("slug").toString())
                        return
                    }
                }
                catch(e: JSONException) { }

                // Access the 'background_image' field
                try {
                    _imageUrl.postValue(obj.get("background_image").toString())
                }
                catch(e: JSONException) {
                    _imageUrl.postValue("-1")
                }

                // Access the 'developers' field
                try {
                    var developers = JSONArray(obj.get("developers").toString())
                    var firstDeveloper = JSONObject(developers[0].toString())
                    _developer.postValue(firstDeveloper.get("name").toString())
                }
                catch(e: JSONException) {
                    _developer.postValue("???")
                }

                // Access the 'publishers' field
                try {
                    var publishers = JSONArray(obj.get("publishers").toString())
                    var firstPublisher = JSONObject(publishers[0].toString())
                    _publisher.postValue(firstPublisher.get("name").toString())
                }
                catch(e: JSONException) {
                    _publisher.postValue("???")
                }

                // Access the 'released' field
                try {
                    _releaseDate.postValue(obj.get("released").toString())
                }
                catch(e: JSONException) {
                    _releaseDate.postValue("???")
                }

                // Access the 'genres' field
                try {
                    var genres = JSONArray(obj.get("genres").toString())
                    var firstGenre = JSONObject(genres[0].toString())
                    _genre.postValue(firstGenre.get("name").toString())
                }
                catch(e: JSONException) {
                    _genre.postValue("???")
                }

                // Access the 'platforms' field
                var index = 0
                var platformList = ""
                try {
                    var platforms = JSONArray(obj.get("platforms").toString())
                    while (true) {
                        var platform = JSONObject(platforms[index].toString())
                        var platformName = JSONObject(platform.get("platform").toString())
                        platformList += platformName.get("name").toString() + " - "
                        index = index+1
                    }
                }
                catch(e: JSONException) {
                    if (index == 0) {
                        _platforms.postValue("???") }
                    else {
                        _platforms.postValue(platformList.dropLast(3))
                    }
                }


                // Access the 'description' field
                try {
                    var descr = obj.get("description_raw").toString()

                    if (descr == "") {
                        descr = obj.get("description").toString()
                    }

                    // Remove various tags
                    descr = descr.replace("<p>", "")
                            .replace("</p>", "")
                            .replace("&#39;", "'")
                            .replace("&quot;", "'")
                            .replace("<br>", "")
                            .replace("<br />", "")
                            .replace("<br/>", "")

                    // Assign the description to its livedata counterpart using POSTVALUE
                    _description.postValue(descr)
                }
                catch(e: JSONException) {
                    _description.postValue("Sorry, couldn't find anything on the RAWG database... :(")
                }

                // Access the 'background_image_additional' field
                try {
                    _imageUrl2.postValue(obj.get("background_image_additional").toString())
                }
                catch(e: JSONException) {
                    _imageUrl2.postValue("-1")
                }
            }
        })
    }
}