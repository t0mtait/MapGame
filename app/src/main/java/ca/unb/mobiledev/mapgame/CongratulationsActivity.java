package ca.unb.mobiledev.mapgame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CongratulationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congratulations);

        // Close App Button
        Button closeAppButton = findViewById(R.id.closeAppButton);
        closeAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the app
                finishAffinity(); // This will close the app and all its activities
            }
        });

        // Go to Leaderboard Button
        Button leaderboardButton = findViewById(R.id.leaderboardButton);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the LeaderboardActivity
                Intent intent = new Intent(CongratulationsActivity.this, LeaderboardActivity.class);
                startActivity(intent);
            }
        });
    }
}
