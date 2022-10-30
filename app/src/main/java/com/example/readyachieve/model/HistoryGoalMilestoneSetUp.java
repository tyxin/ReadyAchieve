package com.example.readyachieve.model;

import java.time.LocalDate;

public class HistoryGoalMilestoneSetUp {

    public String getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    private String type;
    private LocalDate date;

    public HistoryGoalMilestoneSetUp(String type, LocalDate date){
        this.type = type;
        this.date = date;
    }

    @Override
    public String toString() {
        return "HistoryGoalMilestone{" +
                "type='" + type + '\'' +
                ", date=" + date +
                '}';
    }
}
