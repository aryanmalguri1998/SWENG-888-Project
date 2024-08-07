package com.example.photogalleryapplication;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {

    private EditText editProfileName;
    private EditText editProfileEmail;
    private EditText editProfileBio;
    private EditText editProfilePronouns;
    private EditText editProfileNationality;
    private Button btnSaveProfile;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = auth.getCurrentUser();

        if (user == null) {
            // User is not logged in, redirect to login
            startActivity(new Intent(edit_profile.this, LoginActivity.class));
            finish();
            return;
        }

        // Initialize views
        editProfileName = findViewById(R.id.edit_profile_name);
        editProfileEmail = findViewById(R.id.edit_profile_email);
        editProfileBio = findViewById(R.id.edit_profile_bio);
        editProfilePronouns = findViewById(R.id.edit_profile_pronouns);
        editProfileNationality = findViewById(R.id.edit_profile_nationality);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

        // Load current user information
        loadUserInfo();

        // Set onClick listener for the save button
        btnSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserProfile();
            }
        });
    }

    private void loadUserInfo() {
        if (user != null) {
            editProfileName.setText(user.getDisplayName());
            editProfileEmail.setText(user.getEmail());

            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String bio = documentSnapshot.getString("bio");
                    String pronouns = documentSnapshot.getString("pronouns");
                    String nationality = documentSnapshot.getString("nationality");
                    editProfileBio.setText(bio);
                    editProfilePronouns.setText(pronouns);
                    editProfileNationality.setText(nationality);
                }
            });
        }
    }

    private void saveUserProfile() {
        String name = editProfileName.getText().toString().trim();
        String email = editProfileEmail.getText().toString().trim();
        String bio = editProfileBio.getText().toString().trim();
        String pronouns = editProfilePronouns.getText().toString().trim();
        String nationality = editProfileNationality.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editProfileName.setError("Name is required");
            editProfileName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editProfileEmail.setError("Email is required");
            editProfileEmail.requestFocus();
            return;
        }

        if (user != null) {
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(edit_profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(edit_profile.this, "Profile update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(edit_profile.this, "Email updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(edit_profile.this, "Email update failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            // Update Firestore
            DocumentReference docRef = db.collection("users").document(user.getUid());
            Map<String, Object> userData = new HashMap<>();
            userData.put("bio", bio);
            userData.put("pronouns", pronouns);
            userData.put("nationality", nationality);
            docRef.set(userData, SetOptions.merge())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(edit_profile.this, "Additional info updated", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(edit_profile.this, "Failed to update additional info", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}
