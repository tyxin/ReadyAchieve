package com.example.readyachieve.ui.main.goals;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;

import java.util.ArrayList;

public class GoalsRecyclerAdapter extends RecyclerView.Adapter<GoalsRecyclerAdapter.ViewHolder> {

    private ArrayList<GoalSetUp> goalSetUpArrayList;
    private RecyclerView.Adapter adapter;

    public GoalsRecyclerAdapter(ArrayList<GoalSetUp> goalSetUpArrayList){
        this.goalSetUpArrayList = goalSetUpArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goal_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull GoalsRecyclerAdapter.ViewHolder holder, int position) {
        holder.itemTitle.setText(goalSetUpArrayList.get(position).getTitle());
        int noCompleted = 0;
        for (int i = 0;i<goalSetUpArrayList.get(position).getMilestonesInGoalsArrList().size();i++){
            if (goalSetUpArrayList.get(position).getMilestonesInGoalsArrList().get(i).isCompleted()){
                noCompleted++;
            }
        }
        String formattedGoalCount = noCompleted+"/"+goalSetUpArrayList.get(position).getMilestonesInGoalsArrList().size();
        holder.itemGoalCount.setText(formattedGoalCount);
        //holder.itemDescription.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return goalSetUpArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemGoalCount;
        TextView itemTitle;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.goal_card_text);
            itemGoalCount = itemView.findViewById(R.id.goal_card_noOfMilestones);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, GoalsViewActivity.class);
                    intent.putExtra("LayoutPosition",getLayoutPosition());
                    intent.putExtra("Title",
                            (goalSetUpArrayList.get(getLayoutPosition())).getTitle());
                    context.startActivity(intent);
                }
            });
        }
    }
}