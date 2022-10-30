package com.example.readyachieve;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.readyachieve.model.Goal;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class GoalActivity extends AppCompatActivity {

    private int layoutPosition;
    private boolean isCreate = true;
    private String goalTitleCurrent;
    private int goalDataInArrListPosition;
    private EditText goalTitle;
    private EditText goalDescription;
    private EditText goalTargetDate;
    private FloatingActionButton goalSaveButton;
    private ImageButton deleteButton;
    private final Calendar calendar = Calendar.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_goal_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutPosition = getIntent().getIntExtra("LayoutPosition",-1);
        System.out.println(layoutPosition+"startLayoutPosition");
        goalTitleCurrent = getIntent().getStringExtra("Title");

        goalTitle = findViewById(R.id.newGoalTitleEditText);
        goalDescription = findViewById(R.id.newGoalDescriptionEditText);
        goalTargetDate = findViewById(R.id.newGoalTargetDateEditText);
        goalSaveButton = findViewById(R.id.newGoalSaveButton);
        deleteButton = findViewById(R.id.deleteGoalButton);

        if (layoutPosition>=0){
            isCreate = false;
            goalDataInArrListPosition = findGoalSetUpPosition(goalTitleCurrent);
            System.out.println(goalDataInArrListPosition+" "+layoutPosition+"indexes");
            GoalSetUp goalToDisplay = User.findGoalSetUp(goalTitleCurrent);
            System.out.println(goalToDisplay+"goalTodisplay");
            displayGoalData(goalToDisplay);
        }else{
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
                new DatePickerDialog(GoalActivity.this,targetDate,calendar.get(Calendar.YEAR),
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
        AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
        builder.setMessage("Are you sure you want to delete this goal? It will delete the milestones in the goal as well!")
        .setTitle("Delete Confirmation");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                User.deleteMilestonesInGoals(User.getGoalSetUpArrayList().
                        get(goalDataInArrListPosition).getMilestonesInGoalsArrList());
                User.getGoalSetUpArrayList().remove(goalDataInArrListPosition);
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
        ((TextView)findViewById(R.id.newGoalTitleTV)).setText("Goal");
        goalTitle.setText(goalSetUp.getTitle());
        goalDescription.setText(goalSetUp.getDescription());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        goalTargetDate.setText(goalSetUp.getDateGoalTarget().format(dateTimeFormatter));
    }

    private void saveGoal() {
        String title = goalTitle.getText().toString().trim();
        String description = goalDescription.getText().toString();
        String targetDate = goalTargetDate.getText().toString();
        if (title.trim().equals("")||targetDate.trim().equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
            builder.setMessage("Empty fields! Please fill up the title and target date.")
                    .setTitle("Empty Fields Error");
            AlertDialog invalidGoalTitleDialog = builder.create();
            invalidGoalTitleDialog.show();
        }else{
            if (isValidGoalTitle(title)){
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDate targetDateLocalDate = LocalDate.parse(targetDate,dateTimeFormatter);
                System.out.println(targetDateLocalDate+"targetDateLocalDate");
                GoalSetUp newGoal = new GoalSetUp(title,description,targetDateLocalDate,LocalDate.now());
                if (isCreate){
                    User.getGoalSetUpArrayList().add(newGoal);
                }else{
                    //get original title
                    newGoal.setMilestonesInGoalsArrList(User.getGoalSetUpArrayList().
                            get(goalDataInArrListPosition).getMilestonesInGoalsArrList());
                    User.changeGoalSetUp(User.getGoalSetUpArrayList().get(goalDataInArrListPosition).getTitle(),newGoal);
                }
                finish();
                Toast.makeText(GoalActivity.this,"Saved successfully.",Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                builder.setMessage("Invalid title! Title has either been used or contains '|'.")
                        .setTitle("Goal Title Error");
                AlertDialog invalidGoalTitleDialog = builder.create();
                invalidGoalTitleDialog.show();
            }
        }
    }

    private boolean isValidGoalTitle(String goalTitle){
        if (isCreate){
            if (!(goalTitle.contains("|"))){
                for (int i = 0;i< User.getGoalSetUpArrayList().size();i++){
                    if (User.getGoalSetUpArrayList().get(i).getTitle().equals(goalTitle)){
                        return false;
                    }
                }
            }else{ return false;}
        }else{
            if (!(goalTitle.contains("|"))){
                for (int i = 0;i< User.getGoalSetUpArrayList().size();i++){
                    if (User.getGoalSetUpArrayList().get(i).getTitle().equals(goalTitle)
                            &&i!=goalDataInArrListPosition){
                        return false;
                    }
                }
            }else{ return false;}
        }
        return true;

    }

    private void updateDate() {
        String format = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        goalTargetDate.setText(simpleDateFormat.format(calendar.getTime()));
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static int findGoalSetUpPosition(String goalTitle){
        for (int i = 0;i<User.getGoalSetUpArrayList().size();i++){
            if (goalTitle.equals(User.getGoalSetUpArrayList().get(i).getTitle())){
                return i;
            }
        }
        return -1;
    }
}