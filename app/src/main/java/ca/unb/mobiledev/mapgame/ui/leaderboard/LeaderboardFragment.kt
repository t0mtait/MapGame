package ca.unb.mobiledev.mapgame.ui.leaderboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import ca.unb.mobiledev.mapgame.R
import ca.unb.mobiledev.mapgame.model.User

class LeaderboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_leaderboard, container, false)

        // Example user data
        val users = listOf(
            User("John", 1000),
            User("Alice", 750),
            User("Bob", 500)
        )

        val userListLayout = root.findViewById<LinearLayout>(R.id.userListLayout)

        for (user in users) {
            val userItemLayout = LinearLayout(requireContext())
            userItemLayout.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            userItemLayout.orientation = LinearLayout.VERTICAL

            val usernameTextView = TextView(requireContext())
            usernameTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            usernameTextView.text = "Username: ${user.username}"
            usernameTextView.setPadding(8, 8, 8, 8)
            userItemLayout.addView(usernameTextView)

            val pointsTextView = TextView(requireContext())
            pointsTextView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            pointsTextView.text = "Points: ${user.points}"
            pointsTextView.setPadding(8, 8, 8, 8)
            userItemLayout.addView(pointsTextView)

            userListLayout.addView(userItemLayout)
        }

        return root
    }
}
