package com.example.readyachieve.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.readyachieve.ui.main.leaderboard.LeaderboardRecyclerAdapter;
import com.example.readyachieve.ui.main.leaderboard.LeaderboardSortRank;
import com.example.readyachieve.ui.main.leaderboard.LeaderboardViewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static android.content.Context.MODE_PRIVATE;

public class User {
    //statistics
    private static DatabaseReference mDatabaseLeaderboardRanking;
    private static ArrayList<GoalSetUp> goalSetUpArrayList;
    private static ArrayList<MilestoneSetUp> milestoneSetUpArrayList;
    private static ArrayList<HistoryGoalMilestoneSetUp> historyArrayList;
    private Context context;
    private static final String TAG = "State Change";

    public static ArrayList<HistoryGoalMilestoneSetUp> getHistoryArrayList() {
        return historyArrayList;
    }

    public static void setHistoryArrayList(ArrayList<HistoryGoalMilestoneSetUp> historyArrayList) {
        User.historyArrayList = historyArrayList;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    private static String userName;

    public static ArrayList<GoalSetUp> getGoalSetUpArrayList() {
        return goalSetUpArrayList;
    }

    public static ArrayList<MilestoneSetUp> getMilestoneSetUpArrayList() {
        return milestoneSetUpArrayList;
    }

    public User(Context context){
        goalSetUpArrayList = new ArrayList<GoalSetUp>();
        milestoneSetUpArrayList = new ArrayList<MilestoneSetUp>();
        historyArrayList = new ArrayList<HistoryGoalMilestoneSetUp>();
        this.context = context;
        mDatabaseLeaderboardRanking = FirebaseDatabase.getInstance().
                getReference().child("leaderboard");
        //initializing arrayLists
        readFromInternalStorage(context);
        setUser();
    }


    public static void removeMilestoneFromGoal(MilestoneSetUp milestoneSetUp) {
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            if (goalSetUpArrayList.get(i).getTitle().equals(milestoneSetUp.getGoalGroup())){
                goalSetUpArrayList.get(i).getMilestonesInGoalsArrList().remove(milestoneSetUp);
            }
        }
    }

    private void setUser(){
        String name = "";
        try{
            FileInputStream fIn = context.openFileInput("settings.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String temp = bufferedReader.readLine();
            if (temp.equals("")||temp==null){
                System.out.println("error");
            }else{
                //only need read first line
                name = temp; }
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called"); }
        userName = name;
    }

    public static GoalSetUp findGoalSetUp(String goalTitle){
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            System.out.println(goalTitle+" "+goalSetUpArrayList.get(i).getTitle());
            System.out.println(goalTitle.equals(goalSetUpArrayList.get(i).getTitle()));
            if (goalTitle.equals(goalSetUpArrayList.get(i).getTitle())){
                return goalSetUpArrayList.get(i);
            }
        }
        return null;
    }

    public static MilestoneSetUp findMilestoneSetUp(String milestoneTitle){
        for (int i = 0;i<milestoneSetUpArrayList.size();i++){
            if (milestoneTitle.equals(milestoneSetUpArrayList.get(i).getTitle())){
                return milestoneSetUpArrayList.get(i);
            }
        }
        return null;
    }

    public static void changeGoalSetUp(String goalTitle, GoalSetUp toChangeGoalSetUp){
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            if (goalTitle.equals(goalSetUpArrayList.get(i).getTitle())){
                goalSetUpArrayList.get(i).changeGoalSetUp(toChangeGoalSetUp.getTitle(),toChangeGoalSetUp.getDescription(),
                        toChangeGoalSetUp.getDateGoalTarget(),toChangeGoalSetUp.getMilestonesInGoalsArrList(),
                        toChangeGoalSetUp.isCompleted());
            }
        }
    }

    public static void changeMilestoneSetUp(String milestoneTitle, MilestoneSetUp toChangeMilestoneSetUp, String originalGoalGroup){
        for (int i = 0;i<milestoneSetUpArrayList.size();i++){
            if (milestoneTitle.equals(milestoneSetUpArrayList.get(i).getTitle())){
                milestoneSetUpArrayList.get(i).changeMilestoneSetUp(toChangeMilestoneSetUp.getTitle(),toChangeMilestoneSetUp.getDescription(),
                        toChangeMilestoneSetUp.isShortTerm(),toChangeMilestoneSetUp.getRepeatType(),toChangeMilestoneSetUp.getDateTarget(),toChangeMilestoneSetUp.getGoalGroup(),
                        toChangeMilestoneSetUp.isCompleted());
            }
        }
        //case a group to none
        //case a group to another group
        if (!originalGoalGroup.equals(toChangeMilestoneSetUp.getGoalGroup())){
            for (int i = 0;i<goalSetUpArrayList.size();i++){
                if (goalSetUpArrayList.get(i).getTitle().equals(originalGoalGroup)){
                    goalSetUpArrayList.get(i).removeOriginalMilestone(milestoneTitle);
                }
            }
            if (!toChangeMilestoneSetUp.getGoalGroup().equals("None")){
                for (int i = 0;i<goalSetUpArrayList.size();i++){
                    if (goalSetUpArrayList.get(i).getTitle().equals(toChangeMilestoneSetUp.getGoalGroup())){
                        goalSetUpArrayList.get(i).getMilestonesInGoalsArrList().add(toChangeMilestoneSetUp);
                    }
                }
            }
        }else{
        }

    }

    public static void deleteMilestonesInGoals(ArrayList<MilestoneSetUp> milestonesInGoalsArrList){
        for (int i = 0;i<milestonesInGoalsArrList.size();i++){
            milestoneSetUpArrayList.remove(milestonesInGoalsArrList.get(i));
        }
    }

    public static ArrayList<Object> sortMilestoneGoalsInChronologicalOrder(){
        //also only show not completed ones
        ArrayList<Object> combinedMilestoneGoal = new ArrayList<Object>();
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            if (!goalSetUpArrayList.get(i).isCompleted()){
                combinedMilestoneGoal.add(goalSetUpArrayList.get(i));
            }
        }
        for (int j = 0;j<milestoneSetUpArrayList.size();j++){
            if (!milestoneSetUpArrayList.get(j).isCompleted()){
                combinedMilestoneGoal.add(milestoneSetUpArrayList.get(j));
            }
        }
        Collections.sort(combinedMilestoneGoal, new GoalMilestoneSortDateTarget());
        //should be sorted
        return combinedMilestoneGoal;
    }

    public void readFromInternalStorage(Context context){
        //easier also for access, privacy, only leaderboard on database
        try{
            FileInputStream fIn = context.openFileInput("milestones.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String temp;
            milestoneSetUpArrayList.clear();
            while((temp = bufferedReader.readLine())!=null){
                String[] tempArr = temp.split("[|]");
                boolean isShortTerm = false;
                if (tempArr[2].equals("true")){
                    isShortTerm = true;
                }else{
                    if (tempArr[2].equals("false")){
                        isShortTerm = false;
                    }else{
                        System.out.println("error in reading storage");
                    }
                }
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate dateCreated = LocalDate.parse(tempArr[4],dateTimeFormatter);
                LocalDate dateTarget = LocalDate.parse(tempArr[5],dateTimeFormatter);
                MilestoneSetUp tempMilestone = new MilestoneSetUp(decodeString(tempArr[0]),decodeString(tempArr[1]),
                        isShortTerm, tempArr[3],dateCreated,dateTarget,tempArr[6]);
                if (tempArr[7].equals("true")){
                    tempMilestone.setCompleted(true);
                }else{ tempMilestone.setCompleted(false);}
                tempMilestone.updateMilestoneDate();
                milestoneSetUpArrayList.add(tempMilestone);
            }
            //milestoneSetUpArrayList.clear();
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called");
        }

        try{
            FileInputStream fIn = context.openFileInput("goals.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp;
            goalSetUpArrayList.clear();
            while((temp = bufferedReader.readLine())!=null){
                String[] tempArr = temp.split("[|]");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate dateCreated = LocalDate.parse(tempArr[3],dateTimeFormatter);
                LocalDate dateTarget = LocalDate.parse(tempArr[2],dateTimeFormatter);
                GoalSetUp tempGoal = new GoalSetUp(decodeString(tempArr[0]),decodeString(tempArr[1]),dateTarget,dateCreated,tempArr[4]);
                System.out.println(tempGoal+"tempGoal");
                if (tempArr[5].equals("true")){
                    tempGoal.setCompleted(true);
                }else{ tempGoal.setCompleted(false);}
                goalSetUpArrayList.add(tempGoal);
            }
            //goalSetUpArrayList.clear();
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called");
        }

        try{
            FileInputStream fIn = context.openFileInput("history.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp;
            historyArrayList.clear();
            while((temp = bufferedReader.readLine())!=null){
                String[] tempArr = temp.split("[|]");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate date = LocalDate.parse(tempArr[1],dateTimeFormatter);
                HistoryGoalMilestoneSetUp tempHistory = new HistoryGoalMilestoneSetUp(tempArr[0],date);
                historyArrayList.add(tempHistory);
            }
            //historyArrayList.clear();
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called");
        }

    }

    public static void saveInInternalStorage(Context context){
        try{
            FileOutputStream fOut = context.openFileOutput("milestones.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            System.out.println(milestoneSetUpArrayList+"milestoneStorageArrList");
            for (int i = 0;i<milestoneSetUpArrayList.size();i++){
                String isShortTerm = "";
                if (milestoneSetUpArrayList.get(i).isCompleted()){
                    isShortTerm = "true";
                }else{ isShortTerm = "false";}
                String dateCreated = milestoneSetUpArrayList.get(i).getDateCreated().format(dateTimeFormatter);
                String dateTarget = milestoneSetUpArrayList.get(i).getDateTarget().format(dateTimeFormatter);
                String toWrite = processString(milestoneSetUpArrayList.get(i).getTitle())+"|"+
                        processString(milestoneSetUpArrayList.get(i).getDescription())+"|"+ isShortTerm+"|"+
                        milestoneSetUpArrayList.get(i).getRepeatType()+"|"+
                        dateCreated+"|"+ dateTarget+"|"+
                        milestoneSetUpArrayList.get(i).getGoalGroup()+"|"+
                        milestoneSetUpArrayList.get(i).isCompleted()+"\n";
                osw.write(toWrite);
            }
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }

        try{
            FileOutputStream fOut = context.openFileOutput("goals.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            System.out.println(goalSetUpArrayList+"goalArrListStorage");

            for (int i = 0;i<goalSetUpArrayList.size();i++){
                String dateCreated = goalSetUpArrayList.get(i).getDateGoalCreated().format(dateTimeFormatter);
                String dateTarget = goalSetUpArrayList.get(i).getDateGoalTarget().format(dateTimeFormatter);
                ArrayList<String> milestonesInGoalTitleArrList = new ArrayList<String>();
                for (int j = 0;j<goalSetUpArrayList.get(i).getMilestonesInGoalsArrList().size();j++){
                    milestonesInGoalTitleArrList.add(goalSetUpArrayList.get(i).
                            getMilestonesInGoalsArrList().get(j).getTitle());
                }
                String toWrite = processString(goalSetUpArrayList.get(i).getTitle())+"|"+
                        processString(goalSetUpArrayList.get(i).getDescription())+"|"+
                        dateTarget+"|"+ dateCreated+"|"+
                        milestonesInGoalTitleArrList +"|"+
                        goalSetUpArrayList.get(i).isCompleted()+"\n";
                System.out.println(toWrite+"toWrite");
                osw.write(toWrite);
            }
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }

        try{
            FileOutputStream fOut = context.openFileOutput("history.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            for (int i = 0;i<historyArrayList.size();i++){
                String date = historyArrayList.get(i).getDate().format(dateTimeFormatter);
                String toWrite = historyArrayList.get(i).getType()+"|"+date+"\n";
                System.out.println(toWrite+"toWrite");
                osw.write(toWrite);
            }
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }
    }

    /**
     * index 0 is no. of completed milestones
     * index 1 is no. of milestones that do not belong to any goals
     * index 2 is no.of milestones that belong to goals
     * @return
     */
    public static int[] getMilestoneStatistics(){
        int[] uncompletedMilestone = {0,0,0};

        for (int i = 0;i<milestoneSetUpArrayList.size();i++){
            if (!milestoneSetUpArrayList.get(i).isCompleted()){
                if (milestoneSetUpArrayList.get(i).getGoalGroup().equals("None")){
                    uncompletedMilestone[1]++;
                }else{
                    uncompletedMilestone[2]++;
                }
            }else{
                uncompletedMilestone[0]++;
            }
        }
        return uncompletedMilestone;
    }

    /**
     * index 0 is completed, index 1 is not completed
     * @return
     */
    public static int[] getGoalStatistics(){
        int[] goalStatistics = {0,0};
        for (int i = 0;i<goalSetUpArrayList.size();i++){
            if (goalSetUpArrayList.get(i).isCompleted()){
                goalStatistics[0]++;
            }else{
                goalStatistics[1]++;
            }
        }
        return goalStatistics;
    }

    //leaderboard
    /**
     * standardizing
     * completing standalone milestone is 25 points
     * completing milestones in goals is 20 points
     * completing goals is 100 points
     */

    public static void updateLeaderboard(int pointsToAdd){
        ValueEventListener updateLeaderboardPoints = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int points = snapshot.child(userName).child("points").getValue(Integer.class);
                int updatedPoints = pointsToAdd+points;
                //cannot have negative points
                if (updatedPoints<0){ updatedPoints = 0;}
                mDatabaseLeaderboardRanking.child(userName).child("points").setValue(updatedPoints);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("on cancelled firebase leaderboard update points");
            }
        };

        mDatabaseLeaderboardRanking.addListenerForSingleValueEvent(updateLeaderboardPoints);

    }


    /**
     * for past 7 days, inclusive of today
     * each day, number of milestones and goals completed
     * [goals,milestones]
     * @return
     */
    public static int[][] loadMilestonesGoalsCompletedWeek(){
        int[][] data = new int[7][2];
        //start 6 days before current date
        LocalDate day = LocalDate.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        //sort list by chronological order first
        Collections.sort(historyArrayList, new Comparator<HistoryGoalMilestoneSetUp>() {
            @Override
            public int compare(HistoryGoalMilestoneSetUp o1, HistoryGoalMilestoneSetUp o2) {
                if (o1.getDate().isBefore(o2.getDate())){
                    return -1;
                }else if(o1.getDate().isAfter(o2.getDate())){
                    return 1;
                }else{ return 0;}
            }
        });

        for (int j = 0;j<7;j++){
            int noMilestonesCompleted = 0;
            int noGoalsCompleted = 0;
            for (int i = 0;i<User.getHistoryArrayList().size();i++){
                if (historyArrayList.get(i).getDate().format(dateTimeFormatter).equals(day.format(dateTimeFormatter))){
                    if (historyArrayList.get(i).getType().equals("milestone")){
                        noMilestonesCompleted++;
                    }else{
                        if (historyArrayList.get(i).getType().equals("goal")){
                            noGoalsCompleted++;
                        }else{}
                    }
                }
            }
            data[6-j][0] = noGoalsCompleted;
            data[6-j][1] = noMilestonesCompleted;
            day = day.minusDays(1);
        }

        return data;
    }

    private static String processString(String inputText){
        System.out.println(inputText+"inputText");
        String processed = inputText.replaceAll("\\n","\\\\n");
        System.out.println(processed+"processed");
        return processed;
    }

    private static String decodeString(String inputText){
        System.out.println(inputText+"inputText");
        String processed = inputText.replaceAll("\\\\n","\n");
        System.out.println(processed+"processed");
        return processed;
    }

    //TODO: add how many milestones out of goal completed in circle instead
    //TODO: checkmark for goals milestones, implement completed for goals ig
    
}
