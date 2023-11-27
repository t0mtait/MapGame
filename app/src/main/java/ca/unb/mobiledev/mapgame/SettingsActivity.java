package ca.unb.mobiledev.mapgame;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout_button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        Log.w(TAG, "Settings activity CREATED");
        auth = FirebaseAuth.getInstance();
        logout_button = findViewById(R.id.btn_logout);
        textView = findViewById(R.id.currentUser);
        user = auth.getCurrentUser();


        if (user == null)
        {
            Log.d("UserCheck", "User is null. Redirecting to LoginActivity.");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            textView.setText("Currently signed in as: " + user.getEmail());

            logout_button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    auth.signOut();
                    Toast.makeText(SettingsActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


    }
}