package com.example.readyachieve.ui.main.progress;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.readyachieve.R;
import com.example.readyachieve.model.User;
import com.faskn.lib.ClickablePieChart;
import com.faskn.lib.Slice;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.slider.LabelFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProgressFragment extends Fragment {

    private ProgressViewModel progressViewModel;
    private com.github.mikephil.charting.charts.PieChart pieChart;
    private BarChart barChart;
    private String[] daysOfWeek = {"Mon","Tue","Wed","Thu","Fri","Sat","Sun"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_progress, container, false);

        pieChart = root.findViewById(R.id.progressPieChart);
        barChart = root.findViewById(R.id.progressBarChart);

        String[] dataTitlePieChart = {"Completed Milestones","Incomplete milestones",
                "Incomplete milestones in goals","Completed Goals","Incomplete Goals"};
        int[] goalstats = User.getGoalStatistics();
        int[] milestonestats = User.getMilestoneStatistics();
        int[] dataValuePieChart = {milestonestats[0],milestonestats[1],
        milestonestats[2],goalstats[0],goalstats[1]};

        int[][] historystats = User.loadMilestonesGoalsCompletedWeek();

        //int[][] historystats = {{1,2},{1,2},{1,2},{1,2},{1,2},{1,2},{1,2}};
        initializePieChartData(dataValuePieChart,dataTitlePieChart);
        initializeBarChartData(historystats);

        return root;
    }

    private void initializeBarChartData(int[][] historystats) {
        String[] xAxisValues = new String[7];
        int increment = -1;
        switch (LocalDate.now().minusDays(6).getDayOfWeek().toString()) {
            case "MONDAY": increment = 0; break;
            case "TUESDAY": increment = 1; break;
            case "WEDNESDAY": increment = 2; break;
            case "THURSDAY": increment = 3; break;
            case "FRIDAY": increment = 4; break;
            case "SATURDAY": increment = 5; break;
            case "SUNDAY": increment = 6; break;
            default:
                System.out.println("Error in initializing bar chat");
                break;
        }
        for (int i = 0;i<7;i++){
            int index;
            if ((i+increment)<7){
                index = (i+increment);
            }else{
                index = i+increment-7;
            }
            xAxisValues[i] = daysOfWeek[index];
        }

        ArrayList<BarEntry> goalsBarEntry = new ArrayList<>();
        for (int i = 0;i<7;i++){
            goalsBarEntry.add(new BarEntry(i+1,historystats[i][0]));
        }

        ArrayList<BarEntry> milestonesBarEntry = new ArrayList<>();
        for (int j = 0;j<7;j++){
            milestonesBarEntry.add(new BarEntry(j+1,historystats[j][1]));
        }
        XAxis xAxis = barChart.getXAxis();
        xAxis.disableGridDashedLine();
        xAxis.setDrawGridLines(false);
        xAxis.setDrawGridLines(false);
        System.out.println(Arrays.toString(xAxisValues)+"xAxisValues");
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        YAxis yAxisLeft = barChart.getAxisLeft();
        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setEnabled(false);
//        yAxisLeft.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                return String.valueOf((int) value);
//            }
//        });
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1.08f);
        yAxisLeft.setAxisMinimum(0);
        yAxisLeft.setDrawAxisLine(false);
        BarDataSet setGoals = new BarDataSet(goalsBarEntry,"Goals Completed");
        setGoals.setColor(Color.parseColor("#E1A55D"));
        BarDataSet setMilestones = new BarDataSet(milestonesBarEntry,"Milestones Completed");
        setMilestones.setColor(Color.parseColor("#4F80D1"));

        BarData barData = new BarData(setGoals,setMilestones);
        barData.setBarWidth(0.5f);
//        IndexAxisValueFormatter indexAxisValueFormatter = new LabelFormatter(barChart,xAxisValues);
//        xAxis.setValueFormatter(indexAxisValueFormatter);
        xAxis.setAxisMinimum(barData.getXMin()-1f);
        xAxis.setAxisMaximum(barData.getXMax()+1f);
        xAxis.setLabelCount(7);
        xAxis.setAvoidFirstLastClipping(false);

        barChart.setData(barData);
        barChart.animateY(1400);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barData.groupBars(0,0.08f,0.01f);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);



    }

//    class LabelFormatter extends IndexAxisValueFormatter {
//
//        String[] labels;
//        BarLineChartBase<?> chart;
//
//        LabelFormatter(BarLineChartBase<?> chart, String[] labels) {
//            this.chart = chart;
//            this.labels = labels;
//        }
//
//        @NonNull
//        @Override
//        public String getFormattedValue(float value) {
//            return labels[(int) value];
//        }
//    }

    public void initializePieChartData(int[] dataValue, String[] titleValue){
        ArrayList<PieEntry> entryArrayList = new ArrayList<PieEntry>();
        //initializing data
        Map<String, Integer> typeAmountMap = new HashMap<>();
        for (int i = 0;i<5;i++){
            typeAmountMap.put(titleValue[i],dataValue[i]);
        }

        //initializing colors for the entries
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#037ef3"));
        colors.add(Color.parseColor("#00c16e"));
        colors.add(Color.parseColor("#ffc845"));
        colors.add(Color.parseColor("#f48924"));
        colors.add(Color.parseColor("#f85a40"));

        for(String type: typeAmountMap.keySet()){
            entryArrayList.add(new PieEntry(typeAmountMap.get(type).floatValue(), type));
        }

        //collecting the entries with label name
        PieDataSet pieDataSet = new PieDataSet(entryArrayList,"");
        //setting text size of the value
        //providing color list for coloring different entries
        pieDataSet.setColors(colors);
        pieDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                String valueFormat = String.valueOf((int) value);
                if ((int)value==0){
                    return "";
                }
                return valueFormat;
            }
        });

        //grouping the data set from entry to chart
        PieData pieData = new PieData(pieDataSet);
        //showing the value of the entries, default true if not set
        pieData.setDrawValues(true);

        pieChart.setData(pieData);
        pieChart.invalidate();

        //using percentage as values instead of amount
        pieChart.setDrawEntryLabels(false);

        //remove the description label on the lower left corner, default true if not set
        pieChart.getDescription().setEnabled(false);

        //enabling the user to rotate the chart, default true
        pieChart.setRotationEnabled(true);
        //adding friction when rotating the pie chart
        pieChart.setDragDecelerationFrictionCoef(0.9f);
        //setting the first entry start from right hand side, default starting from top
        pieChart.setRotationAngle(0);

        pieChart.getLegend().setWordWrapEnabled(true);
        pieChart.getLegend().setTextSize(16);
        pieChart.getLegend().setXOffset(10);

        pieDataSet.setValueTextSize(16);

        //highlight the entry when it is tapped, default true if not set
        pieChart.setHighlightPerTapEnabled(true);
        //adding animation so the entries pop up from 0 degree
        pieChart.animateY(1400, Easing.EaseInOutQuad);
        //setting the color of the hole in the middle, default white
        pieChart.setHoleColor(Color.parseColor("#000000"));

    }

}


