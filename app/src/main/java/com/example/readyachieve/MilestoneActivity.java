package com.example.readyachieve;

import android.app.DatePickerDialog;
import android.app.PictureInPictureParams;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.HistoryGoalMilestoneSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

public class MilestoneActivity extends AppCompatActivity {

    private int layoutPosition;
    private int milestoneDataInArrListPosition;
    private boolean isCreate = true;
    private String milestoneTitle;

    private EditText titleMilestone;
    private EditText descriptionMilestone;
    private RadioButton shortTermMilestone;
    private RadioButton longTermMilestone;
    private Spinner repeatTypeMilestoneSpinner;
    private EditText targetDateMilestone;
    private Spinner goalGroupSpinner;
    private FloatingActionButton milestoneSaveButton;
    private ArrayList<String> goalGroupData;
    private final Calendar calendar = Calendar.getInstance();
    private CheckBox isCompletedCheckbox;
    private TextView isCompletedTV;
    private ImageButton deleteButton;
    private boolean isInGoal;
    private boolean isTemplate;
    private boolean isInitiallyChecked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.new_milestone_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layoutPosition = getIntent().getIntExtra("LayoutPosition",-1);
        milestoneTitle = getIntent().getStringExtra("Title");
        isTemplate = getIntent().getBooleanExtra("IsTemplate",false);

        titleMilestone = findViewById(R.id.milestoneTitleEditText);
        descriptionMilestone = findViewById(R.id.milestoneDescriptionEditText);
        shortTermMilestone = findViewById(R.id.shortTermOption);
        longTermMilestone = findViewById(R.id.longTermOption);
        repeatTypeMilestoneSpinner = findViewById(R.id.repeatTypeSpinner);
        targetDateMilestone = findViewById(R.id.milestoneTargetDateEditText);
        goalGroupSpinner = findViewById(R.id.goalGroupSpinner);
        milestoneSaveButton = findViewById(R.id.saveButton_newMilestone);
        isCompletedCheckbox = findViewById(R.id.isCompletedMilestoneCheckbox);
        isCompletedTV = findViewById(R.id.completedMilestoneTV);
        deleteButton = findViewById(R.id.deleteMilestoneButton);

        goalGroupData = new ArrayList<String>();
        initializeGoalGroupData();

        ArrayAdapter<String> arrayAdapterRepeatType = new ArrayAdapter<String>(MilestoneActivity.this,
                android.R.layout.simple_spinner_dropdown_item,getResources().getStringArray(R.array.repeatType));
        arrayAdapterRepeatType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatTypeMilestoneSpinner.setAdapter(arrayAdapterRepeatType);

        ArrayAdapter<String> arrayAdapterGoalGroup = new ArrayAdapter<String>(MilestoneActivity.this,
                android.R.layout.simple_spinner_dropdown_item,goalGroupData);
        arrayAdapterGoalGroup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        goalGroupSpinner.setAdapter(arrayAdapterGoalGroup);

        if (isTemplate){
            String[] templateData = getIntent().getStringArrayExtra("TemplateData");
            ((TextView)findViewById(R.id.newMilestoneTitle)).setText("Milestone");
            titleMilestone.setText(templateData[0]);
            descriptionMilestone.setText(templateData[1]);
            if (templateData[2].equals("true")){
                shortTermMilestone.setChecked(true);
            }else{
                longTermMilestone.setChecked(true);
            }
            System.out.println(positionOfRepeatType(templateData[3])+"repeatTypePosition");
            repeatTypeMilestoneSpinner.setSelection(positionOfRepeatType(templateData[3]));
            targetDateMilestone.setText(templateData[6]);
            goalGroupSpinner.setSelection(positionOfGoalGroup(templateData[4]));
            milestoneSaveButton.setVisibility(View.INVISIBLE);
        }

        if (layoutPosition>=0){
            isCreate = false;
            milestoneDataInArrListPosition = findMilestoneSetUpPosition(milestoneTitle);
            MilestoneSetUp milestoneToDisplay = User.findMilestoneSetUp(milestoneTitle);
            isInGoal = !milestoneToDisplay.getGoalGroup().equals("None");
            displayMilestoneData(milestoneToDisplay);
        }else{
            isInGoal = false;
            deleteButton.setVisibility(View.INVISIBLE);
        }

        if (isInGoal){
            isCompletedTV.setVisibility(View.VISIBLE);
            isCompletedCheckbox.setVisibility(View.VISIBLE);
        }

        milestoneSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMilestone();
                //finish();
            }
        });

        targetDateMilestone.setFocusable(false);
        targetDateMilestone.setClickable(true);

        DatePickerDialog.OnDateSetListener targetDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateDate();
            }
        };

        targetDateMilestone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("clicked");
                new DatePickerDialog(MilestoneActivity.this,targetDate,calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteMilestone();
            }
        });

    }

    private void deleteMilestone(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneActivity.this);
        builder.setMessage("Are you sure you want to delete this milestone?")
                .setTitle("Delete Confirmation");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!User.getMilestoneSetUpArrayList().get(milestoneDataInArrListPosition).getGoalGroup().equals("None")){
                    User.removeMilestoneFromGoal(User.getMilestoneSetUpArrayList().get(milestoneDataInArrListPosition));
                }
                User.getMilestoneSetUpArrayList().remove(milestoneDataInArrListPosition);
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

    private void displayMilestoneData(MilestoneSetUp milestoneSetUp) {
        ((TextView)findViewById(R.id.newMilestoneTitle)).setText("Milestone");
        titleMilestone.setText(milestoneSetUp.getTitle());
        descriptionMilestone.setText(milestoneSetUp.getDescription());
        if (milestoneSetUp.isShortTerm()){
            shortTermMilestone.setChecked(true);
        }else{
            longTermMilestone.setChecked(true);
        }
        isInitiallyChecked = milestoneSetUp.isCompleted();
        isCompletedCheckbox.setChecked(milestoneSetUp.isCompleted());
        System.out.println(positionOfRepeatType(milestoneSetUp.getRepeatType())+"repeatTypePosition");
        //setSelection not working?
        repeatTypeMilestoneSpinner.setSelection(positionOfRepeatType(milestoneSetUp.getRepeatType()));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        targetDateMilestone.setText(milestoneSetUp.getDateTarget().format(dateTimeFormatter));
        goalGroupSpinner.setSelection(positionOfGoalGroup(milestoneSetUp.getGoalGroup()));
    }

    private int positionOfRepeatType(String repeatType) {
//        String[] repeatTypeArr = {"Please Select","No Repeat","Daily","Weekly","Monthly",
//                "Yearly","Custom"};
        String[] repeatTypeArr = {"Please Select","No Repeat","Daily","Weekly","Monthly",
                "Yearly"};
        for (int i = 0;i<repeatTypeArr.length;i++){
            if (repeatType.equals(repeatTypeArr[i])){
                return i;
            }
        }
        return -1;
    }

    private int positionOfGoalGroup(String goalGroup) {
        if (goalGroup.equals("None")){ return 0; }
        for (int i = 0;i<goalGroupData.size();i++){
            if (goalGroup.equals(goalGroupData.get(i))){ return i; }
        }
        return -1;
    }

    private void initializeGoalGroupData() {
        goalGroupData.add("None");
        for (int i = 0;i<User.getGoalSetUpArrayList().size();i++){
            goalGroupData.add(User.getGoalSetUpArrayList().get(i).getTitle());
        }
    }

    private void updateDate() {
        String format = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        targetDateMilestone.setText(simpleDateFormat.format(calendar.getTime()));
    }

    private void updateCompleted(boolean isChecked, boolean isInitiallyChecked){
        if (isChecked&&!isInitiallyChecked){
            if (isInGoal){
                User.updateLeaderboard(20);
            }else{
                User.updateLeaderboard(25);
            }
        }
        if (!isChecked&&isInitiallyChecked){
            if (isInGoal){
                User.updateLeaderboard(-20);
            }else{
                User.updateLeaderboard(-25);
            }
        }
    }

    private void saveMilestone() {
        boolean isValidGoal = true;
        String title = titleMilestone.getText().toString().trim();
        String description = descriptionMilestone.getText().toString();
        boolean isShortTerm = false;
        if (shortTermMilestone.isChecked()){ isShortTerm = true;
        }else{
            if (longTermMilestone.isChecked()){ isShortTerm = false;
            }else{ isValidGoal = false; }
        }
        String repeatType = repeatTypeMilestoneSpinner.getSelectedItem().toString();
        if (repeatType.equals("Please Select")){
            isValidGoal = false;
        }

        if (title.trim().equals("")){ isValidGoal = false;}
        String targetDateString = targetDateMilestone.getText().toString();
        if (targetDateString.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneActivity.this);
            builder.setMessage("Empty fields! Please key in all fields, description can be left blank")
                    .setTitle("Empty fields!");
            AlertDialog emptyFieldsDialog = builder.create();
            emptyFieldsDialog.show();
        }else{
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate targetDateLocalDate = LocalDate.parse(targetDateString,dateTimeFormatter);
            String goalGroup = goalGroupSpinner.getSelectedItem().toString();

            if (isValidGoal){
                MilestoneSetUp newMilestone = new MilestoneSetUp(title, description, isShortTerm, repeatType, LocalDate.now(),
                        targetDateLocalDate, goalGroup);
                if (isCreate) {
                    if (isValidMilestoneTitle(title)) {
                        User.getMilestoneSetUpArrayList().add(newMilestone);
                        if (!isInitiallyChecked || isCompletedCheckbox.isChecked()){
                            User.getHistoryArrayList().add(new HistoryGoalMilestoneSetUp("milestone",LocalDate.now()));
                        }
                        newMilestone.setCompleted(isCompletedCheckbox.isChecked());
                        addMilestoneToGoal(newMilestone);
                        updateCompleted(isCompletedCheckbox.isChecked(),isInitiallyChecked);
                        Toast.makeText(MilestoneActivity.this,"Saved successfully.",Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneActivity.this);
                        builder.setMessage("Invalid title! Title has either been used or contains '|'.")
                                .setTitle("Milestone Title Error");
                        AlertDialog invalidMilestoneTitleDialog = builder.create();
                        invalidMilestoneTitleDialog.show();
                    }
                }else{
                    if (!(title.contains("|")||title.contains(","))){
                        System.out.println("changed successfully");
                        newMilestone.setCompleted(isCompletedCheckbox.isChecked());
                        System.out.println(newMilestone+"newMilestone");
                        //to get original title
                        String originalGoalGroup = User.getMilestoneSetUpArrayList().
                                get(milestoneDataInArrListPosition).getGoalGroup();
                        User.changeMilestoneSetUp(User.getMilestoneSetUpArrayList()
                                .get(milestoneDataInArrListPosition).getTitle(),newMilestone,originalGoalGroup);
                        updateCompleted(isCompletedCheckbox.isChecked(),isInitiallyChecked);
                        Toast.makeText(MilestoneActivity.this,"Saved successfully.",Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneActivity.this);
                        builder.setMessage("Invalid title! Title has either been used or contains '|'.")
                                .setTitle("Milestone Title Error");
                        AlertDialog invalidMilestoneTitleDialog = builder.create();
                        invalidMilestoneTitleDialog.show();
                    }
                }
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(MilestoneActivity.this);
                builder.setMessage("Empty fields! Please key in all fields, description can be left blank")
                        .setTitle("Empty fields!");
                AlertDialog emptyFieldsDialog = builder.create();
                emptyFieldsDialog.show();
            }
        }
    }

    private boolean isValidMilestoneTitle(String milestoneTitle){
        if (isCreate){
            if (!(milestoneTitle.contains("|")||milestoneTitle.contains(","))){
                for (int i = 0;i<User.getMilestoneSetUpArrayList().size();i++){
                    if (User.getMilestoneSetUpArrayList().get(i).getTitle().equals(milestoneTitle)){
                        return false;
                    }
                }
            }else{ return false;}
        }else{
            if (!(milestoneTitle.contains("|")||milestoneTitle.contains(","))){
                for (int i = 0;i<User.getMilestoneSetUpArrayList().size();i++){
                    if (User.getMilestoneSetUpArrayList().get(i).getTitle().equals(milestoneTitle)
                            &&i!=milestoneDataInArrListPosition){
                        return false;
                    }
                }
            }else{ return false;}
        }
        return true;
    }

    private void addMilestoneToGoal(MilestoneSetUp newMilestone) {
        for (int i = 0;i<User.getGoalSetUpArrayList().size();i++){
            if (User.getGoalSetUpArrayList().get(i).getTitle().equals(newMilestone.getGoalGroup())){
                User.getGoalSetUpArrayList().get(i).addMilestoneSetUp(newMilestone);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static int findMilestoneSetUpPosition(String milestoneTitle){
        for (int i = 0;i<User.getMilestoneSetUpArrayList().size();i++){
            if (milestoneTitle.equals(User.getMilestoneSetUpArrayList().get(i).getTitle())){
                return i;
            }
        }
        return -1;
    }
}

