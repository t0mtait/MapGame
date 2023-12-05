package ca.unb.mobiledev.mapgame

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import ca.unb.mobiledev.mapgame.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.random.Random

// Define the CityInfo data class here
data class CityInfo(val cityName: String, val imageName: String, val drawableResourceId: Int)

class MainActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMainBinding
    private val cityNamesArray by lazy {
        resources.getStringArray(R.array.city_names)
    }

    private val drawableName by lazy {
        resources.getStringArray(R.array.city_drawables)
    }

    private val hints by lazy {
        resources.getStringArray(R.array.city_hints)
    }

    private val displayedImages = mutableSetOf<String>()
    private val displayedImagesDrawables = mutableSetOf<String>()
    private val displayedHints = mutableSetOf<String>()


    private var currentCityName: String = ""
    private var currentCityNameDrawable: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val hintText = findViewById<View>(R.id.hintsText)

        // Set the opacity to 0 (completely transparent)
        hintText.alpha = 0f



        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_leaderboard, R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        displayRandomImage()  //current city agi, te photo nahi aayi

        setupSubmitButton()
        setupHintButton()

        // Handle bottom navigation item clicks
        setupBottomNavigation()

    }


    private fun setupHintButton() {
        val hintText = findViewById<View>(R.id.hintsText)
        if (hintText.alpha == 1f) {
            Toast.makeText(this, "Hint already given", Toast.LENGTH_SHORT).show()
        } else {
            binding.showHintButton.setOnClickListener {
                Log.d("HintButtonTest", "Hint button clicked")
                hintText.alpha = 1f
                Toast.makeText(this, "Hint now visible", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupSubmitButton()
    {

        binding.submitGuessButton.setOnClickListener {
            var pointsToAward = 100.0
            val hintText = findViewById<View>(R.id.hintsText)
            if (hintText.alpha == 1f)
            {
                pointsToAward = 50.0
            }
            hintText.alpha = 0f
            Log.d("SubmitButtonClickTest", "Submit button clicked")
            val userGuess = binding.guessInput.text.toString()

            // Check if the user's guess is correct
            if (isCorrectAnswer(userGuess)) {
                awardPoints(pointsToAward)
                Toast.makeText(this, "Correct answer! You earned " + pointsToAward + "points!", Toast.LENGTH_SHORT)
                    .show()
                binding.guessInput.text.clear()
                displayedImages.add(currentCityName)
                displayedImagesDrawables.add(currentCityNameDrawable)
                displayRandomImage()
            } else {
                Toast.makeText(
                    this,
                    "Correct answer! You earned $pointsToAward points!",
                    Toast.LENGTH_SHORT
                ).show()
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
                R.id.navigation_leaderboard -> {
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

        // Compare the user's guess with the correct city name (case insensitive)
        val isCorrect = userGuess.equals(currentCityName, ignoreCase = true)

        Log.d("AnswerDebug", "User Guess: $userGuess, Correct City Name: $currentCityName, Is Correct: $isCorrect")

        return isCorrect
    }


    private fun awardPoints(points: Double)
    {
        println("connecting to db...")
        val db = FirebaseFirestore.getInstance()
        // Query the users collection for the document with the matching email
        val userEmail = auth?.currentUser?.email
        println(userEmail)

        db.collection("users")
            .whereEqualTo("email", userEmail)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in task.result!!) {
                        // Update the points field for the found document
                        document.reference.update("points", FieldValue.increment(points) )
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
        // Filter out displayed images
        val availableImages = cityNamesArray.filter { !displayedImages.contains(it) }
        val availableImagesDrawables = drawableName.filter {!displayedImagesDrawables.contains(it)  }
        if (availableImages.isNotEmpty()) {
            val randomIndex = Random.nextInt(availableImages.size)
            currentCityName = availableImages[randomIndex]
            currentCityNameDrawable = availableImagesDrawables[randomIndex]

            val originalIndex = cityNamesArray.indexOf(currentCityName)
            val cityHint = hints[originalIndex]
            binding.hintsText.text = cityHint

            val randomImageId = resources.getIdentifier(currentCityNameDrawable, "drawable", packageName)
            Log.d("DisplayRandomImageID", "Displaying image for city: $currentCityName")
            binding.cityImage.setImageResource(randomImageId)
        } else {
            // Handle the case where all images have been displayed
            Log.d("DisplayRandomImageID", "All images have been displayed")
            Toast.makeText(this, "You've completed all challenges!", Toast.LENGTH_SHORT).show()
            moveToCongratulationsActivity()
        }
    }

    private fun moveToCongratulationsActivity() {
        val intent = Intent(this, CongratulationsActivity::class.java)

        // Start the CongratulationsActivity
        startActivity(intent)
    }
}
