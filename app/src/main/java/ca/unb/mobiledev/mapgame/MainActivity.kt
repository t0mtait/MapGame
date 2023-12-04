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

    private val displayedImages = mutableSetOf<String>()
    private val displayedImagesDrawables = mutableSetOf<String>()

    private var currentCityName: String = ""
    private var currentCityNameDrawable: String = ""
    private var currentImageId: Int = 0 // Variable to store the resource ID of the currently displayed image

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("MainActivity", "onCreate called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView



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
                binding.guessInput.text.clear()
                displayedImages.add(currentCityName)
                displayedImagesDrawables.add(currentCityNameDrawable)
                displayRandomImage()
            } else {
                Toast.makeText(this, "Incorrect answer. Try again.", Toast.LENGTH_SHORT).show()
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


    private fun awardPoints() {
        // Add your logic to award points to the user
        // For example, update a variable to store the user's points

    }

    private fun displayRandomImage() {
        // Filter out displayed images
        val availableImages = cityNamesArray.filter { !displayedImages.contains(it) }
        val availableImagesDrawables = drawableName.filter {!displayedImagesDrawables.contains(it)  }
        if (availableImages.isNotEmpty()) {
            val randomIndex = Random.nextInt(availableImages.size)
            currentCityName = availableImages[randomIndex]
            currentCityNameDrawable = availableImagesDrawables[randomIndex]
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
