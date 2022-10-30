package com.example.readyachieve.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;


public class GoalSetUp {

    private ArrayList<MilestoneSetUp> milestonesInGoalsArrList;
    private String title;
    private String description;
    private LocalDate dateGoalTarget;
    private LocalDate dateGoalCreated;
    private boolean isCompleted;

    public GoalSetUp(String title, String description, LocalDate dateGoalTarget,
                     LocalDate dateGoalCreated){
        this.title = title;
        this.description = description;
        this.dateGoalTarget = dateGoalTarget;
        this.dateGoalCreated = dateGoalCreated;
        milestonesInGoalsArrList = new ArrayList<MilestoneSetUp>();
        this.isCompleted = false;
    }

    public void changeGoalSetUp(String title, String description, LocalDate dateGoalTarget,
                                ArrayList<MilestoneSetUp> milestonesInGoalsArrList, boolean isCompleted){
        this.title = title;
        this.description = description;
        this.dateGoalTarget = dateGoalTarget;
        this.milestonesInGoalsArrList = milestonesInGoalsArrList;
        this.isCompleted = isCompleted;
    }

    public GoalSetUp(String title, String description, LocalDate dateGoalTarget,
                     LocalDate dateGoalCreated, String milestonesInGoalUnproccessedString){
        this.title = title;
        this.description = description;
        this.dateGoalTarget = dateGoalTarget;
        this.dateGoalCreated = dateGoalCreated;
        milestonesInGoalsArrList = new ArrayList<MilestoneSetUp>();
        System.out.println(milestonesInGoalUnproccessedString+"milestoneInGoalUnprocessedString");
        milestonesInGoalUnproccessedString = milestonesInGoalUnproccessedString.substring(1,
                milestonesInGoalUnproccessedString.length()-1);
        System.out.println(milestonesInGoalUnproccessedString+"milestoneInGoaltwo");
        String[] goalsInMilestoneTempArr = milestonesInGoalUnproccessedString.split("[,]");
        System.out.println(Arrays.toString(goalsInMilestoneTempArr)+"arr");
        for (int i = 0;i<goalsInMilestoneTempArr.length;i++){
            MilestoneSetUp foundMilestone = User.findMilestoneSetUp(goalsInMilestoneTempArr[i].trim());
            if (foundMilestone!=null){
                milestonesInGoalsArrList.add(foundMilestone);
            }
        }
    }

    public GoalSetUp(String title, String description, LocalDate dateGoalTarget,
                     LocalDate dateGoalCreated, ArrayList<String> milestonesInGoal){
        this.title = title;
        this.description = description;
        this.dateGoalTarget = dateGoalTarget;
        this.dateGoalCreated = dateGoalCreated;
        milestonesInGoalsArrList = new ArrayList<MilestoneSetUp>();
        for (int i = 0;i<milestonesInGoal.size();i++){
            MilestoneSetUp foundMilestone = User.findMilestoneSetUp(milestonesInGoal.get(i));
            milestonesInGoalsArrList.add(foundMilestone);
        }
    }

    public void addMilestoneSetUp(MilestoneSetUp newMilestoneToGoal){
        System.out.println(newMilestoneToGoal+"newMilestoneToGoal");
        milestonesInGoalsArrList.add(newMilestoneToGoal);
    }

    public void removeOriginalMilestone(String milestoneTitle) {
        for (int i = 0;i<milestonesInGoalsArrList.size();i++){
            if (milestonesInGoalsArrList.get(i).getTitle().equals(milestoneTitle)){
                milestonesInGoalsArrList.remove(i);
            }
        }
    }

    public ArrayList<MilestoneSetUp> getMilestonesInGoalsArrList() {
        return milestonesInGoalsArrList;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDateGoalTarget() {
        return dateGoalTarget;
    }

    public LocalDate getDateGoalCreated() {
        return dateGoalCreated;
    }

    public void setMilestonesInGoalsArrList(ArrayList<MilestoneSetUp> milestonesInGoalsArrList) {
        this.milestonesInGoalsArrList = milestonesInGoalsArrList;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        this.isCompleted = completed;
    }

    @Override
    public String toString() {
        return "GoalSetUp{" +
                "milestonesInGoalsArrList=" + milestonesInGoalsArrList +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", dateGoalTarget=" + dateGoalTarget +
                ", dateGoalCreated=" + dateGoalCreated +
                ", isCompleted=" + isCompleted +
                '}';
    }

}
