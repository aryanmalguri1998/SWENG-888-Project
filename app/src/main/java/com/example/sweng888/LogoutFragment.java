package com.example.sweng888;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LogoutFragment extends Fragment {

    private static final int SPLASH_SCREEN_DELAY = 3000; // Duration in milliseconds (3 seconds)

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);

        // Use a Handler to introduce a delay before redirecting to LoginActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start LoginActivity after the delay
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                // Optionally finish the current activity if you want to remove it from the back stack
                getActivity().finish();
            }
        }, SPLASH_SCREEN_DELAY);

        return view;
    }
}
