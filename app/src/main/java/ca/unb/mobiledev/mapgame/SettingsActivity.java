package ca.unb.mobiledev.mapgame;

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
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SettingsActivity", "Logout page onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.btn_logout);
        textView = findViewById(R.id.currentUser);
        user = auth.getCurrentUser();

        if (user == null)
        {
            Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            textView.setText("Currently signed in as: " + user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                auth.signOut();
                Toast.makeText(SettingsActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}