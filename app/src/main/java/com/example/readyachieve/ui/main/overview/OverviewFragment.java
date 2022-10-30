package com.example.readyachieve.ui.main.overview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.Milestone;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.dashboard.DashboardViewModel;
import com.example.readyachieve.ui.main.home.HomeRecyclerAdapter;
import com.github.vipulasri.timelineview.TimelineView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import ernestoyaquello.com.verticalstepperform.Step;
import ernestoyaquello.com.verticalstepperform.VerticalStepperFormView;
import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class OverviewFragment extends Fragment implements StepperFormListener {

    private OverviewViewModel overviewViewModel;
    private ArrayList<Object> milestonesAndGoalsInOrderArrList;

    //TimelineView timelineView;
    RecyclerView overviewRecyclerView;
    RecyclerView.LayoutManager overviewLayoutManager;
    RecyclerView.Adapter overviewAdapter;
    ImageView noDataImageViewOverview;
    TextView noDataTextViewOverview;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_overview, container, false);
        milestonesAndGoalsInOrderArrList = User.sortMilestoneGoalsInChronologicalOrder();
        overviewRecyclerView = root.findViewById(R.id.overview_recycler_view);
        System.out.println(milestonesAndGoalsInOrderArrList+"milestonesgoals");

        overviewLayoutManager = new LinearLayoutManager(getContext());
        overviewRecyclerView.setLayoutManager(overviewLayoutManager);
        overviewAdapter = new OverviewTimelineRecyclerAdapter(milestonesAndGoalsInOrderArrList);
        overviewRecyclerView.setAdapter(overviewAdapter);

        noDataImageViewOverview = root.findViewById(R.id.noDataOverview);
        noDataTextViewOverview = root.findViewById(R.id.noDataTextOverview);

        if (milestonesAndGoalsInOrderArrList.size()==0){
            noDataImageViewOverview.setVisibility(View.VISIBLE);
            noDataTextViewOverview.setVisibility(View.VISIBLE);
        }else {
            noDataImageViewOverview.setVisibility(View.INVISIBLE);
            noDataTextViewOverview.setVisibility(View.INVISIBLE);
        }

        return root;
    }

    @Override
    public void onCompletedForm() {
    }

    @Override
    public void onCancelledForm() {
    }
}
