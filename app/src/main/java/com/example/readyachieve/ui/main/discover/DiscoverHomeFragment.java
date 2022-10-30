package com.example.readyachieve.ui.main.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.readyachieve.R;

public class DiscoverHomeFragment extends Fragment {

    private Button templateButton;
    private Button goalMilestoneButton;
    private Button tipsAdviceButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_home, container, false);

        templateButton = root.findViewById(R.id.discoverTemplateButton);
        goalMilestoneButton = root.findViewById(R.id.discoverGoalsMilestonesButton);
        tipsAdviceButton = root.findViewById(R.id.discoverTipsButton);

        templateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.home_to_template);
            }
        });

        goalMilestoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.home_to_goal_milestone);
            }
        });

        tipsAdviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.home_to_tips_advice);
            }
        });
        return root;
    }
}

