package com.example.readyachieve.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;

public class GoalMilestoneSortDateTarget implements Comparator<Object> {
    @Override
    public int compare(Object o1, Object o2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate o1DateTarget = null;
        LocalDate o2DateTarget = null;
        if (o1 instanceof GoalSetUp){
            o1DateTarget = ((GoalSetUp) o1).getDateGoalTarget();
        }else{
            if (o1 instanceof MilestoneSetUp){
                o1DateTarget = ((MilestoneSetUp) o1).getDateTarget();
            }else{
                System.out.println("Error in sorting");
            }
        }
        if (o2 instanceof GoalSetUp){
            o2DateTarget = ((GoalSetUp) o2).getDateGoalTarget();
        }else{
            if (o2 instanceof MilestoneSetUp){
                o2DateTarget = ((MilestoneSetUp) o2).getDateTarget();
            }else{
                System.out.println("Error in sorting");
            }
        }
        if (o1DateTarget.isBefore(o2DateTarget)){
            return -1;
        }else if(o1DateTarget.isAfter(o2DateTarget)){
            return 1;
        }else{ return 0;}
    }
}
