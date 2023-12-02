package ca.unb.mobiledev.mapgame.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ca.unb.mobiledev.mapgame.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
        }

        // Initialize UI elements from the XML layout
//        val cityImage: ImageView = binding.cityImage
//        val hintsText: TextView = binding.hintsText
//        val guessInput: EditText = binding.guessInput
//        val submitGuessButton: Button = binding.submitGuessButton

//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        // You can now work with the UI elements as needed.

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}