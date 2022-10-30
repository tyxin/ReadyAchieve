package com.example.readyachieve.ui.main.goals;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.User;

public class GoalsFragment extends Fragment {

    private GoalsViewModel goalsViewModel;
    RecyclerView goalRecyclerView;
    RecyclerView.LayoutManager goalLayoutManager;
    RecyclerView.Adapter goalAdapter;
    private ImageView noDataImageViewGoals;
    private TextView noDataTextViewGoals;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_goals, container, false);

        noDataTextViewGoals = root.findViewById(R.id.noDataTextGoals);
        noDataImageViewGoals = root.findViewById(R.id.noDataGoals);
        goalRecyclerView = root.findViewById(R.id.goals_recycler_view);
        goalLayoutManager = new LinearLayoutManager(getContext());
        goalRecyclerView.setLayoutManager(goalLayoutManager);
        goalAdapter = new GoalsRecyclerAdapter(User.getGoalSetUpArrayList());
        goalRecyclerView.setAdapter(goalAdapter);

        if (User.getGoalSetUpArrayList().size()==0){
            noDataImageViewGoals.setVisibility(View.VISIBLE);
            noDataTextViewGoals.setVisibility(View.VISIBLE);
        }else{
            noDataImageViewGoals.setVisibility(View.INVISIBLE);
            noDataTextViewGoals.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (User.getGoalSetUpArrayList().size()==0){
            noDataImageViewGoals.setVisibility(View.VISIBLE);
            noDataTextViewGoals.setVisibility(View.VISIBLE);
        }else{
            noDataImageViewGoals.setVisibility(View.INVISIBLE);
            noDataTextViewGoals.setVisibility(View.INVISIBLE);
        }

        goalAdapter = new GoalsRecyclerAdapter(User.getGoalSetUpArrayList());
        goalRecyclerView.setAdapter(goalAdapter);
        goalAdapter.notifyDataSetChanged();
    }
}
