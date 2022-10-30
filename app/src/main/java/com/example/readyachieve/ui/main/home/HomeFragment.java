package com.example.readyachieve.ui.main.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.MainActivity;
import com.example.readyachieve.MilestoneActivity;
import com.example.readyachieve.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ArrayList<String> titleArrList;
    private ArrayList<String> descriptionArrList;
    private ArrayList<Integer> imageArrList;
    private FloatingActionButton fab_home;
    private FloatingActionButton fabGoal;
    private FloatingActionButton fabMilestone;
    private TextView fabGoalText;
    private TextView fabMilestoneText;
    private boolean isFabOpen = false;
//    private ViewDataBinding bi;

    RecyclerView homeRecyclerView;
    RecyclerView.LayoutManager homeLayoutManager;
    RecyclerView.Adapter homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        View root = inflater.inflate(R.layout.fragment_home, container, false);
//
//        CollapsingToolbarLayout collapsingToolbarLayout = root.findViewById(R.id.collapsing_toolbar);
//        collapsingToolbarLayout.setTitle("ReadyAchieve");
//        collapsingToolbarLayout.setContentScrimColor(Color.DKGRAY);

        initializeHomeAdapterVariables();
        homeRecyclerView = root.findViewById(R.id.recycler_view_home);
        homeLayoutManager = new LinearLayoutManager(getContext());
        homeRecyclerView.setLayoutManager(homeLayoutManager);
        homeAdapter = new HomeRecyclerAdapter(titleArrList,descriptionArrList,imageArrList);
        homeRecyclerView.setAdapter(homeAdapter);

        fab_home = root.findViewById(R.id.fab);
        fabGoal = root.findViewById(R.id.fab_goal);
        fabMilestone = root.findViewById(R.id.fab_milestone);
        fabGoalText = root.findViewById(R.id.goal_text_fab);
        fabMilestoneText = root.findViewById(R.id.milestone_text_fab);

        fab_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                if (!isFabOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });

        fabMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MilestoneActivity.class));
            }
        });

        fabGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), GoalActivity.class));
            }
        });
        return root;
    }


    private void showFABMenu(){
        isFabOpen=true;
        System.out.println("reached here");
        fab_home.animate().setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).rotation(135);
        fabGoal.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        fabMilestone.animate().translationY(-getResources().getDimension(R.dimen.standard_125));
        fabGoalText.setVisibility(View.VISIBLE);
        fabMilestoneText.setVisibility(View.VISIBLE);
        fabGoalText.animate().translationY(-getResources().getDimension(R.dimen.standard_75));
        fabMilestoneText.animate().translationY(-getResources().getDimension(R.dimen.standard_125));

    }

    private void closeFABMenu(){
        isFabOpen=false;
        fab_home.animate().setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }
        }).rotation(0);
        fabGoal.animate().translationY(0);
        fabMilestone.animate().translationY(0);
        fabGoalText.setVisibility(View.INVISIBLE);
        fabMilestoneText.setVisibility(View.INVISIBLE);
        fabGoalText.animate().translationY(0);
        fabMilestoneText.animate().translationY(0);
    }

    private void initializeHomeAdapterVariables() {
        titleArrList = new ArrayList<String>();
        descriptionArrList = new ArrayList<String>();
        imageArrList = new ArrayList<Integer>();

        String[] title = {"What is a Goal?","What is a Milestone?"};
        String[] description = {"A goal is a target and objective one wants to meet.",
                "A milestone is a stepping stone in life. To achieve a major goal, many stepping stones " +
                        "(milestones) are taken to progress bit by bit to achieve a goal."};
        int[] images = {R.drawable.goal_home,R.drawable.milestone_home};

        titleArrList.addAll(Arrays.asList(title));
        descriptionArrList.addAll(Arrays.asList(description));
        for (int i = 0;i<images.length;i++){
            imageArrList.add(images[i]);
        }
    }
}