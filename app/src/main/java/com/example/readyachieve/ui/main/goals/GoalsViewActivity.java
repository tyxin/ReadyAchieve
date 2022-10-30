package com.example.readyachieve.ui.main.goals;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.GoalActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.HistoryGoalMilestoneSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.dashboard.DashboardRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class GoalsViewActivity extends AppCompatActivity {

    private int layoutPosition;
    //private boolean isCreate = true;
    private String goalTitleCurrent;

    private EditText goalTitle;
    private EditText goalDescription;
    private EditText goalTargetDate;
    private FloatingActionButton goalSaveButton;
    private ImageButton deleteButton;
    private final Calendar calendar = Calendar.getInstance();
    private boolean isTemplate;
    private boolean isInitiallyChecked;

    RecyclerView milestonesInGoalRecyclerView;
    RecyclerView.LayoutManager milestonesInGoalLayoutManager;
    RecyclerView.Adapter milestonesInGoalAdapter;

    private TextView noDataTextViewGoal;
    private ImageView noDataImageViewGoal;
    private CheckBox isCompletedGoalViewCheckbox;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.goal_view_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutPosition = getIntent().getIntExtra("LayoutPosition",-1);
        goalTitleCurrent = getIntent().getStringExtra("Title");
        isTemplate = getIntent().getBooleanExtra("IsTemplate",false);

        goalTitle = findViewById(R.id.viewGoalTitleEditText);
        goalDescription = findViewById(R.id.viewGoalDescriptionEditText);
        goalTargetDate = findViewById(R.id.viewGoalTargetDateEditText);
        goalSaveButton = findViewById(R.id.viewGoalSaveButton);
        noDataImageViewGoal = findViewById(R.id.noDataGoalsView);
        noDataTextViewGoal = findViewById(R.id.noDataTextGoalsView);
        isCompletedGoalViewCheckbox = findViewById(R.id.isCompletedGoalCheckbox);
        deleteButton = findViewById(R.id.deleteGoalViewButton);

        if (isTemplate){
            String[] templateData = getIntent().getStringArrayExtra("TemplateData");
            goalSaveButton.setVisibility(View.INVISIBLE);
            goalTitle.setText(templateData[0]);
            goalDescription.setText(templateData[1]);
            goalTargetDate.setText(templateData[3]);

//            milestonesInGoalRecyclerView = findViewById(R.id.recycler_view_milestones_in_goals);
//            milestonesInGoalLayoutManager = new LinearLayoutManager(GoalsViewActivity.this);
//            milestonesInGoalRecyclerView.setLayoutManager(milestonesInGoalLayoutManager);
//            milestonesInGoalAdapter = new GoalsViewRecyclerAdapter(User.getGoalSetUpArrayList().
//                    get(layoutPosition).getMilestonesInGoalsArrList());
//            milestonesInGoalRecyclerView.setAdapter(milestonesInGoalAdapter);
//
//            if (User.getGoalSetUpArrayList().get(layoutPosition).getMilestonesInGoalsArrList().size()==0){
                noDataTextViewGoal.setVisibility(View.VISIBLE);
                noDataImageViewGoal.setVisibility(View.VISIBLE);
//            }else{
//                noDataImageViewGoal.setVisibility(View.INVISIBLE);
//                noDataImageViewGoal.setVisibility(View.INVISIBLE);
//            }
        }

        if (layoutPosition>=0){
            //isCreate = false;
            GoalSetUp goalToDisplay = User.findGoalSetUp(goalTitleCurrent);
            displayGoalData(goalToDisplay);
            milestonesInGoalRecyclerView = findViewById(R.id.recycler_view_milestones_in_goals);
            milestonesInGoalLayoutManager = new LinearLayoutManager(GoalsViewActivity.this);
            milestonesInGoalRecyclerView.setLayoutManager(milestonesInGoalLayoutManager);
            milestonesInGoalAdapter = new GoalsViewRecyclerAdapter(User.getGoalSetUpArrayList().
                    get(layoutPosition).getMilestonesInGoalsArrList());
            milestonesInGoalRecyclerView.setAdapter(milestonesInGoalAdapter);

            if (User.getGoalSetUpArrayList().get(layoutPosition).getMilestonesInGoalsArrList().size()==0){
                noDataTextViewGoal.setVisibility(View.VISIBLE);
                noDataImageViewGoal.setVisibility(View.VISIBLE);
            }else{
                noDataImageViewGoal.setVisibility(View.INVISIBLE);
                noDataImageViewGoal.setVisibility(View.INVISIBLE);
            }
        }else{
            //should not occur, will only occur for template
            deleteButton.setVisibility(View.INVISIBLE);
        }

        goalTargetDate.setFocusable(false);
        goalTargetDate.setClickable(true);

        goalSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGoal();
                //finish();
            }
        });

        DatePickerDialog.OnDateSetListener targetDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateDate();
            }
        };

        goalTargetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                new DatePickerDialog(GoalsViewActivity.this,targetDate,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteGoal();
            }
        });
    }

    private void deleteGoal(){
        AlertDialog.Builder builder = new AlertDialog.Builder(GoalsViewActivity.this);
        builder.setMessage("Are you sure you want to delete this goal? It will delete the milestones in the goal as well!")
                .setTitle("Delete Confirmation");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User.deleteMilestonesInGoals(User.getGoalSetUpArrayList().
                        get(layoutPosition).getMilestonesInGoalsArrList());
                User.getGoalSetUpArrayList().remove(layoutPosition);
                finish();
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog deleteGoalDialog = builder.create();
        deleteGoalDialog.show();
    }

    private void displayGoalData(GoalSetUp goalSetUp) {
        goalTitle.setText(goalSetUp.getTitle());
        goalDescription.setText(goalSetUp.getDescription());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        goalTargetDate.setText(goalSetUp.getDateGoalTarget().format(dateTimeFormatter));
        isInitiallyChecked = goalSetUp.isCompleted();
        isCompletedGoalViewCheckbox.setChecked(goalSetUp.isCompleted());
    }

    private void saveGoal() {
        String title = goalTitle.getText().toString();
        String description = goalDescription.getText().toString();
        String targetDate = goalTargetDate.getText().toString();
        if (isValidGoalTitle(title)){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate targetDateLocalDate = LocalDate.parse(targetDate,dateTimeFormatter);
            System.out.println(targetDateLocalDate+"targetDateLocalDate");
            ArrayList<String> milestonesInGoalsTitleArrList = new ArrayList<String>();
            for (int i = 0;i<User.getGoalSetUpArrayList().
                    get(layoutPosition).getMilestonesInGoalsArrList().size();i++){
                milestonesInGoalsTitleArrList.add(User.getGoalSetUpArrayList().get(layoutPosition).
                        getMilestonesInGoalsArrList().get(i).getTitle());
            }
            GoalSetUp newGoal = new GoalSetUp(title,description,targetDateLocalDate,LocalDate.now(),milestonesInGoalsTitleArrList);
            if (!isInitiallyChecked && isCompletedGoalViewCheckbox.isChecked()){
                User.getHistoryArrayList().add(new HistoryGoalMilestoneSetUp("goal",LocalDate.now()));
            }
            newGoal.setCompleted(isCompletedGoalViewCheckbox.isChecked());
            //get original title
            updateCompleted(isCompletedGoalViewCheckbox.isChecked(),isInitiallyChecked);
            User.changeGoalSetUp(User.getGoalSetUpArrayList().get(layoutPosition).getTitle(),newGoal);
            Toast.makeText(GoalsViewActivity.this,"Saved successfully.",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(GoalsViewActivity.this);
            builder.setMessage("Invalid title! Title has either been used or contains '|'.")
                    .setTitle("Goal Title Error");
            AlertDialog invalidMilestoneTitleDialog = builder.create();
            invalidMilestoneTitleDialog.show();
        }
    }

    private void updateCompleted(boolean isChecked, boolean isInitiallyChecked){
        if (isChecked&&!isInitiallyChecked){
            User.updateLeaderboard(100);
        }
        if (!isChecked&&isInitiallyChecked){
            User.updateLeaderboard(-100);
        }
    }

    private boolean isValidGoalTitle(String goalTitle){
        if (!goalTitle.contains("|")||goalTitle.trim().equals("")){
            for (int i = 0;i< User.getGoalSetUpArrayList().size();i++){
                if (User.getGoalSetUpArrayList().get(i).getTitle().equals(goalTitle)&&i!=layoutPosition){
                    return false;
                }
            }
        }else{ return false;}
        return true;
    }

    private void updateDate() {
        String format = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        goalTargetDate.setText(simpleDateFormat.format(calendar.getTime()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onresume called");
        if (layoutPosition>=0){
            System.out.println(User.getGoalSetUpArrayList().get(layoutPosition).getMilestonesInGoalsArrList());
            milestonesInGoalAdapter = new GoalsViewRecyclerAdapter(User.getGoalSetUpArrayList().
                    get(layoutPosition).getMilestonesInGoalsArrList());
            milestonesInGoalRecyclerView.setAdapter(milestonesInGoalAdapter);
            milestonesInGoalAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
//            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
