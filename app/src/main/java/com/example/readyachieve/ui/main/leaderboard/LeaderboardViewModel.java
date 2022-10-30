package com.example.readyachieve.ui.main.leaderboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LeaderboardViewModel extends ViewModel {

    private int rank;
    private String name;

    public void setRank(int rank) {
        this.rank = rank;
    }

    private int points;

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public LeaderboardViewModel(int rank, String name, int points){
        this.rank = rank;
        this.name = name;
        this.points = points;
    }

    @Override
    public String toString() {
        return "LeaderboardViewModel{" +
                "rank=" + rank +
                ", name='" + name + '\'' +
                ", points=" + points +
                '}';
    }
}
