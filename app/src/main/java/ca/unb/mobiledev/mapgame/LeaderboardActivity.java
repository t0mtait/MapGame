package ca.unb.mobiledev.mapgame;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import ca.unb.mobiledev.mapgame.model.User;
import ca.unb.mobiledev.mapgame.model.UserAdapter;

public class LeaderboardActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.w(TAG, "Leaderboard activity CREATED");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_leaderboard);

        Log.w(TAG, "Connecting to firebase instance...");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Log.w(TAG, "Connecting to users collection...");
        CollectionReference users = db.collection("users");

        recyclerView = findViewById(R.id.userList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        userAdapter = new UserAdapter();
        recyclerView.setAdapter(userAdapter);


        users.orderBy("points").limit(10).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    // Convert each document to a User object and add it to your adapter
                    User user = document.toObject(User.class);
                    userAdapter.addUser(user);
                    Log.w(TAG, "NEW USER DETECTED!");
                }
                // Notify the adapter that the data set has changed
                userAdapter.notifyDataSetChanged();
            } else {
                // Handle errors
                Log.e("LeaderboardActivity", "Error getting documents: ", task.getException());
            }
        });
    }

}
