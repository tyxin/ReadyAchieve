package com.example.readyachieve.ui.main.leaderboard;

import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class LeaderboardSortRank implements Comparator<LeaderboardViewModel> {

    @Override
    public int compare(LeaderboardViewModel o1, LeaderboardViewModel o2) {
        if (o1.getPoints()<o2.getPoints()){
            return 1;
        }else{
            if (o1.getPoints()==o2.getPoints()){
                return 0;
            }else{
                return -1;
            }
        }
    }
}
