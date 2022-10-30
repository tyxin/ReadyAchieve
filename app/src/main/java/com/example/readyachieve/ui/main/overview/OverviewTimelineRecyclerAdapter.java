package com.example.readyachieve.ui.main.overview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.github.vipulasri.timelineview.TimelineView;

import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OverviewTimelineRecyclerAdapter extends RecyclerView.Adapter<OverviewTimelineViewHolder> {

    private ArrayList<Object> milestoneGoalArrList;

    public OverviewTimelineRecyclerAdapter(ArrayList<Object> milestoneGoalArrList){
        this.milestoneGoalArrList = milestoneGoalArrList;
        System.out.println("this should be called");
    }

    @NotNull
    @Override
    public OverviewTimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("view created overview");
        View view = View.inflate(parent.getContext(), R.layout.overview_step_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        System.out.println(viewType+"viewType");
        return new OverviewTimelineViewHolder(view,viewType);
    }


    @Override
    public void onBindViewHolder(@NonNull OverviewTimelineViewHolder holder, int position) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("called here");
        System.out.println(milestoneGoalArrList.get(position) instanceof MilestoneSetUp);
        System.out.println(milestoneGoalArrList.get(position) instanceof GoalSetUp);
        if (milestoneGoalArrList.get(position) instanceof MilestoneSetUp){
            String title = ((MilestoneSetUp) milestoneGoalArrList.get(position)).getTitle();
            String date = ((MilestoneSetUp) milestoneGoalArrList.get(position)).getDateTarget().format(dateTimeFormatter);
            holder.image.setImageResource(R.drawable.milestone_home);
            holder.title.setText(title);
            String dateToDisplay = "Target Date: "+date;
            holder.date.setText(dateToDisplay);
        }else{
            if (milestoneGoalArrList.get(position) instanceof GoalSetUp){
                String title = ((GoalSetUp) milestoneGoalArrList.get(position)).getTitle();
                String date = ((GoalSetUp) milestoneGoalArrList.get(position)).getDateGoalTarget().format(dateTimeFormatter);
                holder.image.setImageResource(R.drawable.goal_home);
                holder.title.setText(title);
                String dateToDisplay = "Target Date: "+date;
                holder.date.setText(dateToDisplay);

            }else{
                System.out.println("Error onBindViewHolder");
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    @Override
    public int getItemCount() {
        return milestoneGoalArrList.size();
    }
}
