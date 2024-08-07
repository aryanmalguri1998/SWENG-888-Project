package com.example.sweng888;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView profilePicture;
    private TextView profileName;
    private TextView profileEmail;
    private TextView profileBio;
    private TextView profilePronouns;
    private TextView profileNationality;
    private Button btnEditProfile;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.profile_settings, container, false);

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        profilePicture = view.findViewById(R.id.profile_picture);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        profileBio = view.findViewById(R.id.profile_bio);
        profilePronouns = view.findViewById(R.id.profile_pronouns);
        profileNationality = view.findViewById(R.id.profile_nationality);
        btnEditProfile = view.findViewById(R.id.btn_edit_profile);

        // Set onClick listeners
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = auth.getCurrentUser();
                if (user != null) {
                    startActivity(new Intent(getActivity(), edit_profile.class));
                } else {
                    // User is not logged in, redirect to login
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
            }
        });

        // Load user information
        loadUserInfo();

        return view;
    }

    private void loadUserInfo() {
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            profileName.setText(user.getDisplayName());
            profileEmail.setText(user.getEmail());
            if (user.getPhotoUrl() != null) {
                Picasso.get()
                        .load(user.getPhotoUrl())
                        .placeholder(R.drawable.baseline_person_24)
                        .error(R.drawable.baseline_person_24)
                        .into(profilePicture, new Callback() {
                            @Override
                            public void onSuccess() {
                                // Success case
                            }

                            @Override
                            public void onError(Exception e) {
                                e.printStackTrace();
                            }
                        });
            } else {
                profilePicture.setImageResource(R.drawable.baseline_person_24);
            }

            // Load additional info from Firestore
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    String bio = documentSnapshot.getString("bio");
                    String pronouns = documentSnapshot.getString("pronouns");
                    String nationality = documentSnapshot.getString("nationality");
                    profileBio.setText(bio);
                    profilePronouns.setText(pronouns);
                    profileNationality.setText(nationality);
                }
            });
        }
    }
}
