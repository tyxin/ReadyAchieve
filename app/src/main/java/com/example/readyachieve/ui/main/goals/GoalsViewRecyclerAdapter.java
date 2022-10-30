package com.example.readyachieve.ui.main.goals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.MilestoneActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;

import java.util.ArrayList;

public class GoalsViewRecyclerAdapter extends RecyclerView.Adapter<GoalsViewRecyclerAdapter.ViewHolder> {

    private ArrayList<MilestoneSetUp> milestonesInGoalArrList;
    private RecyclerView.Adapter adapter;

    public GoalsViewRecyclerAdapter(ArrayList<MilestoneSetUp> milestonesInGoalArrList){
        this.milestonesInGoalArrList = milestonesInGoalArrList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_view_milestone_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(milestonesInGoalArrList.get(position).getTitle());
        //holder.itemGoalCount.setText(milestoneSetUpArrayList.get(position).getGoalsInMilestonesArrList().size());
        //holder.itemDescription.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return milestonesInGoalArrList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //TextView itemGoalCount;
        TextView itemTitle;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.goal_view_milestone_card_text);
            //itemGoalCount = itemView.findViewById(R.id.milestone_card_noOfGoals);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MilestoneActivity.class);
                    intent.putExtra("Title",
                            (milestonesInGoalArrList.get(getLayoutPosition()).getTitle()));
                    intent.putExtra("LayoutPosition",getLayoutPosition());
                    context.startActivity(intent);
                }
            });
        }
    }
}

