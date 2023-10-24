package ca.unb.mobiledev.mapgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var sharedpreferences: SharedPreferences

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val emailEdt = findViewById<EditText>(R.id.emailId)
        val passwordEdt = findViewById<EditText>(R.id.passwordId)
        val loginBtn = findViewById<Button>(R.id.loginButton)

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

        loginBtn.setOnClickListener {
            if (TextUtils.isEmpty(emailEdt.text.toString()) || TextUtils.isEmpty(passwordEdt.text.toString())) {
                Toast.makeText(this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedpreferences.edit()
                editor.putString(EMAIL_KEY, emailEdt.text.toString())
                editor.putString(PASSWORD_KEY, passwordEdt.text.toString())
                editor.apply()

                // Start the MainActivity or navigate to your app's main functionality.
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}
