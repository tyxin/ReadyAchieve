package com.example.readyachieve.ui.onboarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.readyachieve.OnboardingActivity;
import com.example.readyachieve.R;

public class OnBoardingStepTwo extends Fragment {
    Button skipButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.onboarding_step_two, container, false);
        skipButton = view.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((OnboardingActivity)getActivity()).skipToProfileOnboarding();
            }
        });
        return view;
    }
}
