package com.example.readyachieve.ui.main.dashboard;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.MilestoneActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;

import java.util.ArrayList;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.ViewHolder> {

    private ArrayList<String> titleData;
    private ArrayList<Integer> imageData;
    private ArrayList<Object> allMilestoneAndGoalData;
    private RecyclerView.Adapter adapter;

    public DashboardRecyclerAdapter(ArrayList<String> titleData, ArrayList<Integer> imageData,
                                    ArrayList<Object> allMilestoneAndGoalData){
        this.titleData = titleData;
        this.imageData = imageData;
        this.allMilestoneAndGoalData = allMilestoneAndGoalData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(titleData.get(position));
        holder.itemImage.setImageResource(imageData.get(position));
        if (allMilestoneAndGoalData.get(position) instanceof MilestoneSetUp){
            holder.isCompleted.setChecked(((MilestoneSetUp) allMilestoneAndGoalData.get(position)).isCompleted());
        }else{
            if (allMilestoneAndGoalData.get(position) instanceof GoalSetUp){
                holder.isCompleted.setChecked(((GoalSetUp) allMilestoneAndGoalData.get(position)).isCompleted());
            }
        }
        holder.isCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                System.out.println("Called");
                if (allMilestoneAndGoalData.get(position) instanceof MilestoneSetUp){
                    updateCompleted(isChecked,"Milestone");
                    ((MilestoneSetUp) allMilestoneAndGoalData.get(position)).setCompleted(isChecked);
                }else{
                    if (allMilestoneAndGoalData.get(position) instanceof GoalSetUp){
                        updateCompleted(isChecked,"Goal");
                        ((GoalSetUp) allMilestoneAndGoalData.get(position)).setCompleted(isChecked);
                    }
                }
            }
        });

        //holder.itemDescription.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemTitle;
        Context context;
        CheckBox isCompleted;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.dashboard_card_text);
            itemImage = itemView.findViewById(R.id.dashboard_card_image);
            isCompleted = itemView.findViewById(R.id.isCompletedDashboardCheckBox);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (allMilestoneAndGoalData.get(getLayoutPosition()) instanceof MilestoneSetUp){
                        Intent intent = new Intent(context, MilestoneActivity.class);
                        intent.putExtra("LayoutPosition",getLayoutPosition());
                        intent.putExtra("Title",
                                ((MilestoneSetUp) allMilestoneAndGoalData.get(getLayoutPosition())).getTitle());
                        context.startActivity(intent);
                    }else{
                        if (allMilestoneAndGoalData.get(getLayoutPosition()) instanceof GoalSetUp){
                            Intent intent = new Intent(context, GoalActivity.class);
                            intent.putExtra("LayoutPosition",getLayoutPosition());
                            intent.putExtra("Title",
                                    ((GoalSetUp) allMilestoneAndGoalData.get(getLayoutPosition())).getTitle());
                            context.startActivity(intent);
                        }else{
                            //should not be called
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setMessage("An unexpected error occurred!")
                                    .setTitle("Error occurred");
                            AlertDialog errorDialog = builder.create();
                            errorDialog.show();
                        }
                    }
                }
            });
        }
    }

    private void updateCompleted(boolean isChecked, String type){
        if (type.equals("Milestone")){
            if (isChecked){
                User.updateLeaderboard(25);
            }else{
                User.updateLeaderboard(-25);
            }
        }
        if (type.equals("Goal")){
            if (isChecked){
                User.updateLeaderboard(100);
            }else{
                User.updateLeaderboard(-100);
            }
        }
    }
}
