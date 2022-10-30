package com.example.readyachieve.ui.main.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.MilestoneActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.goals.GoalsViewRecyclerAdapter;

import java.util.ArrayList;

public class LeaderboardRecyclerAdapter extends RecyclerView.Adapter<LeaderboardRecyclerAdapter.ViewHolder> {

    private ArrayList<LeaderboardViewModel> leaderboardData;
    private RecyclerView.Adapter adapter;

    public LeaderboardRecyclerAdapter(ArrayList<LeaderboardViewModel> leaderboardData){
        this.leaderboardData = leaderboardData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_ranking_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemRank.setText(String.valueOf(leaderboardData.get(position).getRank()));
        holder.itemName.setText(leaderboardData.get(position).getName());
        if (leaderboardData.get(position).getName().equals(User.getUserName())){
            holder.cardView.setBackgroundColor(Color.parseColor("#E1A55D"));
            holder.cardView.setRadius(12);
        }
        String pointsDisplay = leaderboardData.get(position).getPoints()+" points";
        holder.itemPoints.setText(pointsDisplay);
    }

    @Override
    public int getItemCount() {
        return leaderboardData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //TextView itemGoalCount;
        TextView itemRank;
        TextView itemName;
        TextView itemPoints;
        Context context;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemRank = itemView.findViewById(R.id.ranking_card_rank_no);
            itemName = itemView.findViewById(R.id.ranking_card_name);
            itemPoints = itemView.findViewById(R.id.ranking_card_points);
            cardView = itemView.findViewById(R.id.leaderboard_rank_card_view);
            //itemGoalCount = itemView.findViewById(R.id.milestone_card_noOfGoals);

        }
    }
}
