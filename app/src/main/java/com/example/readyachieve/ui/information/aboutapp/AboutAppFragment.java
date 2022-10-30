package com.example.readyachieve.ui.information.aboutapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.readyachieve.R;
import com.example.readyachieve.ui.main.dashboard.DashboardViewModel;

public class AboutAppFragment extends Fragment {

    private AboutAppViewModel aboutAppViewModel;
    private Button discoverTemplateButton;
    private Button discoverGoalMilestoneButton;
    private Button discoverTipAdviceButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_app, container, false);

        discoverGoalMilestoneButton = root.findViewById(R.id.discoverTemplateButton);
        discoverGoalMilestoneButton = root.findViewById(R.id.discoverGoalsMilestonesButton);
        discoverTipAdviceButton = root.findViewById(R.id.discoverTipsButton);



        return root;
    }
}
