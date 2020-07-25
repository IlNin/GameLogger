# GameLogger
An Android Application written in Kotlin that helps you keep track of your collection of videogames, and that I had to develop for the "Mobile and Cloud Applications" course.


<b>Features</b>:
- After making your account (or signing in using your Google profile), you can add your games by specifying their title, developer, platform and other stats, such as if you have beaten them.<br/>
- Once added, you can apply a filter to your list of games, so you can easily figure out which ones you still need to beat, which ones are your favorites, which one you possess for a specific platform and so on. You can also delete games or edit information about them.<br/>
- Your games will be saved on the Cloud using Firestore, so your archive will follow you across your devices.<br/>
- By clicking the magnifying glass icon underneath your added games, you will also get further information about them, like their release date, descriptions and images, all provided by the RAWG database, which is the largest video game related database out there!


<b>Comments:</b>:
This was a very time-consuming, and yet rewarding, project. Before I could even write a line of code, I had to learn a lot of theory and follow countless tutorials so I could get the basics right on how to develop a Kotlin Android application. There was so much to unpack: the differences between activities and fragments, the concepts of ViewModels and LiveData, how to set up user-friendly layouts and navigation, how to communicate with Cloud databases in order to store and load data, how to offer more services by utilizing APIs, and so on. Even the mere act of adding a dynamic scrollable list, where the elements out of view are recycled in order to free up resources, can be a challenging task that requires multiple steps. 
All in all, I'm quite happy with out the application turned out, especially its UI: my inspiration was a website called Backloggery (hence the app's interna name), and I tried my best to replicate a subset of its features on my app.
Sadly there's no written report this time since it wasn't asked by the professor.


<b>Install Instructions:</b>:
In order to compile this application you need Android Studio: just be aware that all the UI was made while keeping in mind the size of my phone's screen (4,65"), so on different types of screens it may appear quite a bit awkward. I know a good application should be scalable across different devices, but I just didn't have the time to optimize! :(
