package com.example.readyachieve.ui.main.leaderboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.dashboard.DashboardViewModel;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LeaderboardFragment extends Fragment {

    private LeaderboardViewModel leaderboardViewModel;
    private Button viewLeaderboardButton;
    private Button moreInfoButton;
    private TextView pointsTextView;
    private TextView badgesTextView;
    private DatabaseReference mDatabaseLeaderboard;
    private DatabaseReference mDatabaseBadges;
    private int points;
    private int noOfBadges;
    private Map<String,Integer> badgesData = new HashMap<>();
    RecyclerView leaderboardBadgesRecyclerView;
    RecyclerView.LayoutManager leaderboardBadgesLayoutManager;
    RecyclerView.Adapter leaderboardBadgesAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        viewLeaderboardButton = root.findViewById(R.id.viewLeaderboardButton);
        viewLeaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.leaderboard_to_ranking);
            }
        });

        moreInfoButton = root.findViewById(R.id.infoLeaderboardButton);
        moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),LeaderboardMoreInfoActivity.class);
                startActivity(intent);
            }
        });

        mDatabaseLeaderboard = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        mDatabaseBadges = FirebaseDatabase.getInstance().getReference().child("badges");

        pointsTextView = root.findViewById(R.id.pointsLeaderboardTV);
        badgesTextView = root.findViewById(R.id.badgesLeaderboardTV);

        leaderboardBadgesRecyclerView = root.findViewById(R.id.badges_gridview);

        leaderboardBadgesLayoutManager = new GridLayoutManager(getContext(),4);
        leaderboardBadgesRecyclerView.setLayoutManager(leaderboardBadgesLayoutManager);

        initializeBadgeData();

        ValueEventListener getPoints = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userLeaderboard: snapshot.getChildren()){
                    if (userLeaderboard.child("name").getValue(String.class).equals(User.getUserName())){
                        points = userLeaderboard.child("points").getValue(Integer.class);
                        pointsTextView.setText(points+" pts");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("onCancelled reading leaderboard points");
            }
        };



        mDatabaseLeaderboard.addValueEventListener(getPoints);

        return root;
    }

    /**
     * 20pts for getting started
     * 100 pts for warming up
     * 250 pts for getting the hang of it
     * 500 pts for achieving it
     */
    private void initializeBadgeData() {
        badgesData.put("Getting Started",0);
        badgesData.put("Warming Up",0);
        badgesData.put("Hang of it",0);
        badgesData.put("Achieving it",0);

        ValueEventListener getBadgesPoints = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userLeaderboard: snapshot.getChildren()){
                    if (userLeaderboard.child("name").getValue(String.class).equals(User.getUserName())){
                        points = userLeaderboard.child("points").getValue(Integer.class);
                    }
                }

                badgesData.put("Getting Started",0);
                badgesData.put("Warming Up",0);
                badgesData.put("Hang of it",0);
                badgesData.put("Achieving it",0);

                if (points>=20){ badgesData.put("Getting Started",1); }
                if (points>=100){ badgesData.put("Warming Up",1); }
                if (points>=250){ badgesData.put("Hang of it",1); }
                if (points>=500){ badgesData.put("Achieving it",1); }

                noOfBadges = 0;
                for (String badgeName: badgesData.keySet()){
                    noOfBadges+= badgesData.get(badgeName);
                }

                badgesTextView.setText(noOfBadges+" badges obtained");

                leaderboardBadgesAdapter = new LeaderboardBadgesAdapter(badgesData);
                leaderboardBadgesRecyclerView.setAdapter(leaderboardBadgesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("onCancelled reading leaderboard points");
            }
        };

        mDatabaseLeaderboard.addValueEventListener(getBadgesPoints);

    }
}
