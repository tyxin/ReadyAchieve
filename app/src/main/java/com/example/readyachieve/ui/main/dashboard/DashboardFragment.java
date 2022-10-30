package com.example.readyachieve.ui.main.dashboard;

import android.os.Bundle;
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
import com.example.readyachieve.ui.main.goals.GoalsRecyclerAdapter;
import com.example.readyachieve.ui.main.home.HomeRecyclerAdapter;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

//    private DashboardViewModel dashboardViewModel;
    private ArrayList<String> milestoneGoalTitleArrList;
    private ArrayList<Integer> imageArrList;
    private ArrayList<Object> milestoneGoalDataArrList;
    private ImageView noDataImageViewDashboard;
    private TextView noDataTextViewDashboard;

    RecyclerView dashboardRecyclerView;
    RecyclerView.LayoutManager dashboardLayoutManager;
    RecyclerView.Adapter dashboardAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initializeDashboardAdapterVariables();
        noDataImageViewDashboard = root.findViewById(R.id.noDataDashboard);
        noDataTextViewDashboard = root.findViewById(R.id.noDataTextDashboard);
        dashboardRecyclerView = root.findViewById(R.id.recycler_view_dashboard);
        dashboardLayoutManager = new LinearLayoutManager(getContext());
        dashboardRecyclerView.setLayoutManager(dashboardLayoutManager);
        dashboardAdapter = new DashboardRecyclerAdapter(milestoneGoalTitleArrList,imageArrList,
                milestoneGoalDataArrList);
        if (milestoneGoalTitleArrList.size()==0){
            noDataImageViewDashboard.setVisibility(View.VISIBLE);
            noDataTextViewDashboard.setVisibility(View.VISIBLE);
        }else{
            noDataImageViewDashboard.setVisibility(View.INVISIBLE);
            noDataTextViewDashboard.setVisibility(View.INVISIBLE);
        }
        dashboardRecyclerView.setAdapter(dashboardAdapter);

        return root;
    }

    private void initializeDashboardAdapterVariables() {
        //For now, goals at top, milestone at bottom
        milestoneGoalDataArrList = new ArrayList<Object>();
        milestoneGoalTitleArrList = new ArrayList<String>();
        imageArrList = new ArrayList<Integer>();

        for (int i = 0;i< User.getGoalSetUpArrayList().size();i++){
            milestoneGoalDataArrList.add(User.getGoalSetUpArrayList().get(i));
            milestoneGoalTitleArrList.add(User.getGoalSetUpArrayList().get(i).getTitle());
            imageArrList.add(R.drawable.goal_home);
        }

        for(int i = 0;i< User.getMilestoneSetUpArrayList().size();i++){
            if (User.getMilestoneSetUpArrayList().get(i).getGoalGroup().equals("None")){
                milestoneGoalDataArrList.add(User.getMilestoneSetUpArrayList().get(i));
                milestoneGoalTitleArrList.add(User.getMilestoneSetUpArrayList().get(i).getTitle());
                imageArrList.add(R.drawable.milestone_home);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //to refresh
        initializeDashboardAdapterVariables();
        if (milestoneGoalTitleArrList.size()==0){
            noDataImageViewDashboard.setVisibility(View.VISIBLE);
            noDataTextViewDashboard.setVisibility(View.VISIBLE);
        }else{
            noDataImageViewDashboard.setVisibility(View.INVISIBLE);
            noDataTextViewDashboard.setVisibility(View.INVISIBLE);
        }
        dashboardAdapter = new DashboardRecyclerAdapter(milestoneGoalTitleArrList,imageArrList,
                milestoneGoalDataArrList);
        dashboardRecyclerView.setAdapter(dashboardAdapter);
        dashboardAdapter.notifyDataSetChanged();
    }

}