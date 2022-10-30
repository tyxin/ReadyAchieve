package com.example.readyachieve.ui.main.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.ui.main.dashboard.DashboardRecyclerAdapter;
import com.example.readyachieve.ui.main.leaderboard.LeaderboardRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DiscoverTemplateFragment extends Fragment {

    private Button returnTemplateButton;
    private DatabaseReference mDatabaseDiscoverTemplate;
    private ArrayList<Object> templateDataArrList;

    RecyclerView discoverTemplateRecyclerView;
    RecyclerView.LayoutManager discoverTemplateLayoutManager;
    RecyclerView.Adapter discoverTemplateAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_template, container, false);

        discoverTemplateRecyclerView = root.findViewById(R.id.discover_template_recycler_view);
        discoverTemplateLayoutManager = new LinearLayoutManager(getContext());
        discoverTemplateRecyclerView.setLayoutManager(discoverTemplateLayoutManager);
        returnTemplateButton = root.findViewById(R.id.returnTemplateButton);
        returnTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.template_to_home);
            }
        });

        templateDataArrList = new ArrayList<Object>();

        mDatabaseDiscoverTemplate = FirebaseDatabase.getInstance().getReference().child("discover_templates");
        initializeTemplates();

        return root;
    }

    private void initializeTemplates() {
        ValueEventListener initializeTemplates = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot tempTemplate: snapshot.getChildren()){
                    String type = tempTemplate.child("type").getValue(String.class);
                    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    if (type.equals("goal")){
                        String title = tempTemplate.child("title").getValue(String.class);
                        String description = tempTemplate.child("description").getValue(String.class);
                        String goalCreatedString = tempTemplate.child("dateGoalCreated").getValue(String.class);
                        String goalTargetString = tempTemplate.child("dateGoalTarget").getValue(String.class);
                        LocalDate goalCreated = LocalDate.parse(goalCreatedString,dateTimeFormatter);
                        LocalDate goalTarget = LocalDate.parse(goalTargetString,dateTimeFormatter);
                        ArrayList<String> milestoneInArrList = new ArrayList<String>();
                        GoalSetUp templateGoal = new GoalSetUp(title,description,goalTarget,goalCreated,milestoneInArrList);
                        templateDataArrList.add(templateGoal);
                    }else{
                        if (type.equals("milestone")){
                            String title = tempTemplate.child("title").getValue(String.class);
                            String description = tempTemplate.child("description").getValue(String.class);
                            String goalGroup = tempTemplate.child("goalGroup").getValue(String.class);
                            boolean isShortTerm = tempTemplate.child("isShortTerm").getValue(Boolean.class);
                            String repeatType = tempTemplate.child("repeatType").getValue(String.class);
                            String dateCreatedString = tempTemplate.child("dateCreated").getValue(String.class);
                            String dateTargetString = tempTemplate.child("dateTarget").getValue(String.class);
                            LocalDate dateCreated = LocalDate.parse(dateCreatedString,dateTimeFormatter);
                            LocalDate dateTarget = LocalDate.parse(dateTargetString,dateTimeFormatter);
                            MilestoneSetUp templateMilestone = new MilestoneSetUp(title,description,isShortTerm,
                                    repeatType,dateCreated,dateTarget,goalGroup);
                            templateDataArrList.add(templateMilestone);
                        }else{
                            System.out.println("Error, type not identified");
                        }
                    }
                }

                discoverTemplateAdapter = new DiscoverTemplateRecyclerAdapter(templateDataArrList);
                discoverTemplateRecyclerView.setAdapter(discoverTemplateAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };

        mDatabaseDiscoverTemplate.addValueEventListener(initializeTemplates);
    }
}
