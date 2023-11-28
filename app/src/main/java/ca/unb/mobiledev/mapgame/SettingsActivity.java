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

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.WriteResult;

public class SettingsActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logout_button;
    Button changename_button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);
        Log.w(TAG, "Settings activity CREATED");
        auth = FirebaseAuth.getInstance();
        logout_button = findViewById(R.id.btn_logout);
        changename_button = findViewById(R.id.btn_changename);
        textView = findViewById(R.id.currentUser);
        TextInputEditText usernameField = findViewById(R.id.newname);
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
                    Toast.makeText(SettingsActivity.this, "Logged out Successfully!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            changename_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    // Get the new username from the TextInputEditText
                    String newUsername = usernameField.getText().toString();

                    // Get the current user's email
                    String userEmail = user.getEmail();

                    // Query the users collection for the document with the matching email
                    db.collection("users")
                            .whereEqualTo("email", userEmail)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful())
                                {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        // Update the username field for the found document
                                        document.getReference().update("username", newUsername)
                                                .addOnSuccessListener(aVoid -> {
                                                    Toast.makeText(SettingsActivity.this, "Username updated!", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.e(TAG, "Error updating username", e);
                                                    Toast.makeText(SettingsActivity.this, "Failed to update username", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                }
                                else
                                {
                                    Log.e(TAG, "Error getting documents: ", task.getException());
                                }
                            });
                }
            });



        }


    }
}