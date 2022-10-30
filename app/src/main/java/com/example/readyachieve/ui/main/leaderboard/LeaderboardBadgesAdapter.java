package com.example.readyachieve.ui.main.leaderboard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.Map;

public class LeaderboardBadgesAdapter extends RecyclerView.Adapter<LeaderboardBadgesAdapter.ViewHolder> {

    private Map<String,Integer> badgesData;
    private String[] badgesTitle = {"Getting Started","Warming Up","Hang of it","Achieving it"};
    private int[] badgesImage = {R.drawable.ic_points_gettingstarted,R.drawable.ic_points_warmingup,
    R.drawable.ic_points_hangofit,R.drawable.ic_points_achieving};
    private RecyclerView.Adapter adapter;

    public LeaderboardBadgesAdapter(Map<String,Integer> badgesData){
        this.badgesData = badgesData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.leaderboard_badges_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(badgesTitle[position]);
        if (badgesData.get(badgesTitle[position])==1){
            holder.itemImage.setImageResource(badgesImage[position]);
        }
    }

    @Override
    public int getItemCount() {
        return badgesTitle.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //TextView itemGoalCount;
        TextView itemTitle;
        ImageView itemImage;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.android_gridview_text);
            itemImage = itemView.findViewById(R.id.android_gridview_image);
            //itemGoalCount = itemView.findViewById(R.id.milestone_card_noOfGoals);

        }
    }
}
