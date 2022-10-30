package com.example.readyachieve.ui.main.leaderboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalMilestoneSortDateTarget;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.goals.GoalsRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class LeaderboardRankingFragment extends Fragment {

    private DatabaseReference mDatabaseLeaderboardRanking;
    private ArrayList<LeaderboardViewModel> leaderboardDataArrList;
    RecyclerView leaderboardRankRecyclerView;
    RecyclerView.LayoutManager leaderboardRankLayoutManager;
    RecyclerView.Adapter leaderboardRankAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leaderboard_ranking_layout, container, false);

        leaderboardDataArrList = new ArrayList<LeaderboardViewModel>();
        leaderboardRankRecyclerView = root.findViewById(R.id.leaderboard_rank_recycler_view);
        leaderboardRankLayoutManager = new LinearLayoutManager(getContext());
        leaderboardRankRecyclerView.setLayoutManager(leaderboardRankLayoutManager);
        mDatabaseLeaderboardRanking = FirebaseDatabase.getInstance().getReference().child("leaderboard");
        ValueEventListener initializeLeaderboard = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaderboardDataArrList.clear();
                for (DataSnapshot tempRank: snapshot.getChildren()){
                    String name = tempRank.child("name").getValue(String.class);
                    //int rank = tempRank.child("rank").getValue(Integer.class);
                    int points = tempRank.child("points").getValue(Integer.class);
                    LeaderboardViewModel tempLeaderboardPerson = new LeaderboardViewModel(0,name,points);
                    leaderboardDataArrList.add(tempLeaderboardPerson);
                }

                System.out.println(leaderboardDataArrList+"leaderboardArrList");
                //actually sorting by points, then assigning rank
                Collections.sort(leaderboardDataArrList, new LeaderboardSortRank());
                int rank = 1;
                for (int i = 0;i<leaderboardDataArrList.size()-1;i++){
                    leaderboardDataArrList.get(i).setRank(rank);
                    if (leaderboardDataArrList.get(i).getPoints()==leaderboardDataArrList.get(i+1).getPoints()){
                    }else{
                        rank++;
                    }
                }

                if (leaderboardDataArrList.get(leaderboardDataArrList.size()-2).getPoints()==
                        leaderboardDataArrList.get(leaderboardDataArrList.size()-1).getPoints()){
                    leaderboardDataArrList.get(leaderboardDataArrList.size()-1).setRank(
                            leaderboardDataArrList.get(leaderboardDataArrList.size()-2).getRank());
                }else{
                    leaderboardDataArrList.get(leaderboardDataArrList.size()-1).setRank(
                            leaderboardDataArrList.get(leaderboardDataArrList.size()-2).getRank()+1);
                }

                leaderboardRankAdapter = new LeaderboardRecyclerAdapter(leaderboardDataArrList);
                leaderboardRankRecyclerView.setAdapter(leaderboardRankAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("on cancelled firebase leaderboard ranking");
            }
        };

        mDatabaseLeaderboardRanking.addValueEventListener(initializeLeaderboard);
        return root;
    }
}
