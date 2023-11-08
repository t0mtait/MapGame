package ca.unb.mobiledev.mapgame;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.unb.mobiledev.mapgame.model.User;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;
    Button buttonReg;
    TextView textView;
    FirebaseAuth mAuth;

    FirebaseFirestore db;


    @Override
    public void onStart()
    {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_acitivity);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);
        buttonReg = findViewById(R.id.btn_register);
        textView = findViewById(R.id.loginNow);
        db = FirebaseFirestore.getInstance();

        textView.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                        {

                                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });


        buttonReg.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String email, password;
                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this, "Account created.",
                                            Toast.LENGTH_SHORT).show();

                                    User newUser = new User(email);


                                    String entryId = db.collection("users").count().toString();
                                    db.collection("users").document(entryId).set(newUser);


                                }
                                else
                                {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }

        });
    }
}