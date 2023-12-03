package ca.unb.mobiledev.mapgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ca.unb.mobiledev.mapgame.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import org.xmlpull.v1.XmlPullParser
import kotlin.random.Random


// Define the CityInfo data class here
data class CityInfo(val cityName: String, val imageName: String, val drawableResourceId: Int)

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding


    private val cityNamesArray by lazy {
        resources.getStringArray(R.array.city_names)
    }

    private val drawableName by lazy {
        resources.getStringArray(R.array.city_drawables)
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var currentCityName: String = ""

    private var currentImageId: Int = 0 // Variable to store the resource ID of the currently displayed image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

//        val cityImagesArray: Array<Array<String>> = resources.getStringArray(R.array.city_images).map {
//            it.split(",").toTypedArray()
//        }.toTypedArray()

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        displayRandomImage()  //current city agi, te photo nahi aayi

        setupSubmitButton()

        // Handle bottom navigation item clicks
        setupBottomNavigation()

    }


    private fun setupSubmitButton() {
        // Handle submit button click for guessing
        binding.submitGuessButton.setOnClickListener {
            Log.d("SubmitButtonClickTest", "Submit button clicked")
            val userGuess = binding.guessInput.text.toString()

            // Check if the user's guess is correct
            if (isCorrectAnswer(userGuess)) {
                awardPoints()
                Toast.makeText(this, "Correct answer! You earned points.", Toast.LENGTH_SHORT)
                    .show()
                // Move to the next challenge: generate a new image
                //currentCityName = getRandomCityName()
                binding.guessInput.text.clear()
                displayRandomImage()
            } else {
                Toast.makeText(this, "Incorrect answer. Try again.", Toast.LENGTH_SHORT).show()
                // Clear the previous guess
                binding.guessInput.text.clear()
            }
        }
    }

    private fun setupBottomNavigation() {
        val navView: BottomNavigationView = binding.navView
        Log.d("navButtonClickedtest", "Submit button clicked")

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_settings -> {
                    navigateToSettings()
                    true
                }
                R.id.navigation_notifications -> {
                    navigateToLeaderboard()
                    true
                }
                else -> true
            }
        }
    }

    private fun getRandomCityDrawable(): String {
        val totalCityNames = cityNamesArray.size
        val randomIndex = Random.nextInt(totalCityNames)
        currentCityName = cityNamesArray[randomIndex]
        return drawableName[randomIndex]
    }


    private fun navigateToSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToLeaderboard() {
        val intent = Intent(this, LeaderboardActivity::class.java)
        startActivity(intent)
    }

    private fun isCorrectAnswer(userGuess: String): Boolean {
        // Get the correct city name associated with the currently displayed image
        val correctCityInfo = getCorrectCityInfo()


        // Compare the user's guess with the correct city name (case insensitive)
        val isCorrect = userGuess.equals(currentCityName, ignoreCase = true)

        Log.d("AnswerDebug", "User Guess: $userGuess, Correct City Name: $currentCityName, Is Correct: $isCorrect")

        return isCorrect
    }

    private fun getCorrectCityInfo(): CityInfo? {

        // Get the resource ID of the currently displayed image
        val randomImageId = resources.getIdentifier(currentCityName, "drawable", packageName)

        // Load the XML data from the resource
        val parser = resources.getXml(R.xml.allcitiesinfo)

        while (parser.eventType != XmlPullParser.END_DOCUMENT) {
            if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
                val cityInfo = parseCityInfo(parser)

                // Log information for debugging
                Log.d("CityInfoDebug", "Parsed City Info: $cityInfo, Image Id: $randomImageId, currentCityName: $currentCityName")

                // Check if the resource ID matches the given image ID
                if (cityInfo.drawableResourceId != 0 && cityInfo.drawableResourceId == randomImageId) {
                    Log.d("CityInfoDebug", "Matched! Image ID: $randomImageId, City Info: $cityInfo")

                    // Return the correct city information for the matched image ID
                    return cityInfo
                }
            }
            parser.next()
        }

        // Return null if no match is found
        return null
    }

    private fun getRandomImageId(): Int {
        // Get the total number of city names in the array
        val totalCityNames = cityNamesArray.size

        // Generate a random index
        val randomIndex = Random.nextInt(totalCityNames)

        // Get the city name at the randomly selected index
        val randomCityName = cityNamesArray[randomIndex]

        // Log or use the randomCityName as needed
        Log.d("RandomCityName", randomCityName)

        // Use the city name to get the corresponding drawable resource ID
        return resources.getIdentifier(randomCityName, "drawable", packageName)
    }


    private fun parseCityInfo(parser: XmlPullParser): CityInfo {
        var eventType = parser.eventType
        var cityName = ""
        var imageName = ""
        var drawableResourceId = 0

        while (!(eventType == XmlPullParser.END_TAG && parser.name == "item")) {
            when (eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    "name" -> cityName = parseTextContent(parser)
                    "imageName" -> imageName = parseTextContent(parser)
                    "drawable" -> drawableResourceId = parseDrawableResourceId(parser)
                }
            }
            eventType = parser.next()
        }

        return CityInfo(cityName, imageName, drawableResourceId)
    }

    private fun parseTextContent(parser: XmlPullParser): String {
        val eventType = parser.next()

        return if (eventType == XmlPullParser.TEXT) {
            parser.text
        } else {
            ""
        }
    }

    private fun parseDrawableResourceId(parser: XmlPullParser): Int {
        val eventType = parser.next()

        return if (eventType == XmlPullParser.TEXT) {
            // Assuming the drawable reference is stored as text content of the <drawable> tag
            resources.getIdentifier(parser.text, "drawable", packageName)
        } else {
            0
        }
    }

    private fun awardPoints() {
        val db = FirebaseFirestore.getInstance()
        // Query the users collection for the document with the matching email
        val userEmail = auth.currentUser?.email
        db.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        // Update the points field for the found document
                        document.reference.update("points", FieldValue.increment(100) )
                            .addOnSuccessListener {
                                // Handle success
                                println("Points updated successfully!")
                            }
                            .addOnFailureListener { e ->
                                // Handle failure
                                println("Failed to update points: ${e.message}")
                            }
                    }
                } else {
                    // Handle error getting documents
                    println("Error getting documents: ${task.exception}")
                }
            }
    }

    private fun displayRandomImage() {
        val randomImageId = resources.getIdentifier(getRandomCityDrawable(), "drawable", packageName)
        Log.e("DisplayRandomImageID", "Invalid resource ID for image: $randomImageId")
        binding.cityImage.setImageResource(randomImageId)
    }
}
