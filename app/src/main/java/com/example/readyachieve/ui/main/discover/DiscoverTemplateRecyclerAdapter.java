package com.example.readyachieve.ui.main.discover;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.MilestoneActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.dashboard.DashboardRecyclerAdapter;
import com.example.readyachieve.ui.main.goals.GoalsViewActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DiscoverTemplateRecyclerAdapter extends RecyclerView.Adapter<DiscoverTemplateRecyclerAdapter.ViewHolder> {

    private ArrayList<Object> milestoneAndGoalTemplateData;

    public DiscoverTemplateRecyclerAdapter(ArrayList<Object> milestoneAndGoalTemplateData){
        this.milestoneAndGoalTemplateData = milestoneAndGoalTemplateData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_template_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //holder.itemTitle.setText(milestoneAndGoalTemplateData.get(position));
        //holder.itemImage.setImageResource(milestoneAndGoalTemplateData.get(position));
        if (milestoneAndGoalTemplateData.get(position) instanceof MilestoneSetUp){
            holder.itemTitle.setText(((MilestoneSetUp) milestoneAndGoalTemplateData.get(position)).getTitle());
            holder.itemType.setText("Milestone");
        }else{
            if (milestoneAndGoalTemplateData.get(position) instanceof GoalSetUp) {
                holder.itemTitle.setText(((GoalSetUp) milestoneAndGoalTemplateData.get(position)).getTitle());
                holder.itemType.setText("Goal");
            }else{
                //should not be called
                System.out.println("An unexpected error occurred");
            }
        }
    }

    @Override
    public int getItemCount() {
        return milestoneAndGoalTemplateData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView itemType;
        TextView itemTitle;
        Button startButton;
        Context context;
        ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            itemType = itemView.findViewById(R.id.discover_template_card_type);
            itemTitle = itemView.findViewById(R.id.discover_template_card_title);
            //no image currently
            itemImage = itemView.findViewById(R.id.discover_template_card_image);
            startButton = itemView.findViewById(R.id.startUsingTemplateButton);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    if (milestoneAndGoalTemplateData.get(getLayoutPosition()) instanceof MilestoneSetUp){
                        Intent intent = new Intent(context, MilestoneActivity.class);
                        intent.putExtra("IsTemplate",true);
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String[] templateData = {((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle(),
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDescription(),
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).isShortTerm()+"",
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getRepeatType(),
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getGoalGroup(),
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDateCreated().format(dateTimeFormatter),
                        ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDateTarget().format(dateTimeFormatter)};
                        intent.putExtra("TemplateData",templateData);
                        intent.putExtra("Title",
                                ((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle());
                        context.startActivity(intent);
                    }else{
                        if (milestoneAndGoalTemplateData.get(getLayoutPosition()) instanceof GoalSetUp){
                            //TODO: loading of milestonesInArrList
                            Intent intent = new Intent(context, GoalsViewActivity.class);
                            intent.putExtra("IsTemplate",true);
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            String[] templateData = {((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle(),
                            ((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDescription(),
                                    ((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDateGoalCreated().format(dateTimeFormatter),
                                    ((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getDateGoalTarget().format(dateTimeFormatter)
                            };
                            intent.putExtra("TemplateData",templateData);
                            intent.putExtra("Title",
                                    ((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle());
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

            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (milestoneAndGoalTemplateData.get(getLayoutPosition()) instanceof MilestoneSetUp){
                        if (!templateWithSameNameUsed(((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle(),"Milestone")){
                            User.getMilestoneSetUpArrayList().add((MilestoneSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition()));
                            System.out.println(User.getMilestoneSetUpArrayList()+"milestonesDiscover");
                            Toast.makeText(itemView.getContext(),"Template successfully added to dashboard!",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(v.getContext(),"Unable to add milestone, milestone with same title as template has been used!",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        if (milestoneAndGoalTemplateData.get(getLayoutPosition()) instanceof GoalSetUp){
                            if (!templateWithSameNameUsed(((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition())).getTitle(),"Goal")){
                                User.getGoalSetUpArrayList().add((GoalSetUp) milestoneAndGoalTemplateData.get(getLayoutPosition()));
                                System.out.println(User.getGoalSetUpArrayList()+"goalsDiscover");
                                Toast.makeText(itemView.getContext(),"Template successfully added to dashboard!",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(v.getContext(),"Unable to add goal, Goal with same title as template has been used!",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }

    public boolean templateWithSameNameUsed(String templateTitle, String type){
        if (type.equals("Milestone")){
            for (int i = 0;i<User.getMilestoneSetUpArrayList().size();i++){
                if (User.getMilestoneSetUpArrayList().get(i).getTitle().equals(templateTitle)){
                    return true;
                }
            }
        }else{
            if (type.equals("Goal")){
                for (int i = 0;i<User.getGoalSetUpArrayList().size();i++){
                    if (User.getGoalSetUpArrayList().get(i).getTitle().equals(templateTitle)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

