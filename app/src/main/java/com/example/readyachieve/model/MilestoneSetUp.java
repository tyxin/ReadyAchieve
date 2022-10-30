package com.example.readyachieve.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class MilestoneSetUp {
    private String title;
    private String description;
    private boolean isShortTerm;
    private String repeatType;
    private LocalDate dateCreated;
    //when to goal should end / finish if has end date
    private LocalDate dateTarget;
    private String goalGroup;

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    private boolean isCompleted;

    public MilestoneSetUp(String title, String description, boolean isShortTerm, String repeatType,
                          LocalDate dateCreated, LocalDate dateTarget, String goalGroup){
        this.title = title;
        this.description = description;
        this.isShortTerm = isShortTerm;
        this.repeatType = repeatType;
        this.dateCreated = dateCreated;
        this.dateTarget = dateTarget;
        this.goalGroup = goalGroup;
        this.isCompleted = false;
    }

    public void changeMilestoneSetUp(String title, String description, boolean isShortTerm, String repeatType,
                                     LocalDate dateTarget, String goalGroup, boolean isCompleted){
        this.title = title;
        this.description = description;
        this.isShortTerm = isShortTerm;
        this.repeatType = repeatType;
        this.dateTarget = dateTarget;
        this.goalGroup = goalGroup;
        this.isCompleted = isCompleted;
    }

    public void updateMilestoneDate(){
        if (repeatType.equals("No Repeat")){
        }else{
            if (dateTarget.isBefore(LocalDate.now())){
                switch (repeatType){
                    case "Daily": dateTarget.plusDays(1); break;
                    case "Weekly": dateTarget.plusWeeks(1); break;
                    case "Monthly": dateTarget.plusMonths(1); break;
                    case "Yearly": dateTarget.plusYears(1); break;
//                    case "Custom":
//                        break;
                    default: break;
                }
            }else{
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public boolean isShortTerm() {
        return isShortTerm;
    }

    public String getRepeatType() {
        return repeatType;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public LocalDate getDateTarget() {
        return dateTarget;
    }

    public String getGoalGroup() {
        return goalGroup;
    }

    public boolean isCompleted() { return isCompleted; }


    @Override
    public String toString() {
        return "MilestoneSetUp{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", isShortTerm=" + isShortTerm +
                ", repeatType='" + repeatType + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateTarget=" + dateTarget +
                ", goalGroup='" + goalGroup + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MilestoneSetUp that = (MilestoneSetUp) o;
        return isShortTerm == that.isShortTerm &&
                isCompleted == that.isCompleted &&
                title.equals(that.title) &&
                Objects.equals(description, that.description) &&
                repeatType.equals(that.repeatType) &&
                dateCreated.equals(that.dateCreated) &&
                dateTarget.equals(that.dateTarget) &&
                goalGroup.equals(that.goalGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, isShortTerm, repeatType, dateCreated, dateTarget, goalGroup, isCompleted);
    }
}


