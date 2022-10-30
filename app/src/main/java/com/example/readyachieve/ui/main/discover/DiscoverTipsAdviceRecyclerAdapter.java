package com.example.readyachieve.ui.main.discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;

import java.util.ArrayList;

public class DiscoverTipsAdviceRecyclerAdapter extends RecyclerView.Adapter<DiscoverTipsAdviceRecyclerAdapter.ViewHolder> {

    private ArrayList<String> adviceArrList;
    //private ArrayList<Integer> colorArrList;
    private ArrayList<String> colorArrList;
    private RecyclerView.Adapter adapter;

    public DiscoverTipsAdviceRecyclerAdapter(ArrayList<String> adviceArrList){
        this.adviceArrList = adviceArrList;
        //this.colorArrList = new ArrayList<Integer>();
        this.colorArrList = new ArrayList<String>();
        loadColorArrList();
    }

    private void loadColorArrList() {
//        int[] color = {R.color.red,R.color.orange,R.color.orange,R.color.yellow,R.color.green,
//        R.color.light_blue,R.color.dark_blue,R.color.purple,R.color.pink,R.color.brown,};
        String[] color = {"#CF6363","#E1A55D","#E8EC72","#A3EA95","#95EBF6","#4F80D1","#7F71D5",
        "#D583D5","#9C7C70","#8A9E9E"};
        for (int i = 0;i<color.length;i++){
            colorArrList.add(color[i]);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_tips_advice_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(adviceArrList.get(position));
        if (holder.startBar.getBackground() instanceof GradientDrawable){
            GradientDrawable gradientDrawable = (GradientDrawable) holder.startBar.getBackground();
            gradientDrawable.mutate();
            gradientDrawable.setColor(Color.parseColor(colorArrList.get(position)));
        } else{
            System.out.println("Error!");
        }
        //holder.itemGoalCount.setText(milestoneSetUpArrayList.get(position).getGoalsInMilestonesArrList().size());
        //holder.itemDescription.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return adviceArrList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //TextView itemGoalCount;
        TextView itemTitle;
        TextView startBar;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.discover_tips_advice_title);
            startBar = itemView.findViewById(R.id.discover_tips_advice_start_bar);
//            System.out.println("start");
//            System.out.println(startBar.getBackground() instanceof ShapeDrawable);
//            System.out.println(startBar.getBackground() instanceof GradientDrawable);
//            System.out.println(startBar.getBackground() instanceof ColorDrawable);
//            System.out.println("end");

            //itemGoalCount = itemView.findViewById(R.id.milestone_card_noOfGoals);
            itemView.setClickable(false);
        }
    }
}
