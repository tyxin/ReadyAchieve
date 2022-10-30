package com.example.readyachieve.model;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Goal {

    private ArrayList<GoalSetUp> goalSetUpArrayList;
    private Context context;
    private static final String TAG = "State Change";

    public Goal(Context context){
        this.context = context;
        goalSetUpArrayList = new ArrayList<GoalSetUp>();
        loadGoalSetUpArrList();
    }

    private void loadGoalSetUpArrList() {
        try{
            FileInputStream fIn = context.openFileInput("goals.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp;
            goalSetUpArrayList.clear();
            while((temp = bufferedReader.readLine())!=null){
                String[] tempArr = temp.split("[|]");
                GoalSetUp goalSetUpTemp = new GoalSetUp(tempArr[0],tempArr[1],
                        convertStringToLocalDate(tempArr[2]), convertStringToLocalDate(tempArr[3]),
                        tempArr[4]);
                goalSetUpArrayList.add(goalSetUpTemp);
            }
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called");
        }
    }

    public void addMilestoneToGoal(String goalTitle, MilestoneSetUp milestoneSetUp){
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            if (goalSetUpArrayList.get(i).getTitle().equals(goalTitle)){
                goalSetUpArrayList.get(i).addMilestoneSetUp(milestoneSetUp);
            }
        }
    }

    private LocalDate convertStringToLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(date,formatter);
    }
}
