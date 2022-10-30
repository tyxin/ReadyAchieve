package com.example.readyachieve.ui.onboarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.readyachieve.MainActivity;
import com.example.readyachieve.R;
import com.example.readyachieve.ui.information.settings.SettingsViewModel;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import static android.content.Context.MODE_PRIVATE;

public class OnBoardingStepEight extends Fragment {

    private SettingsViewModel settingsViewModel;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private RadioButton childOption;
    private RadioButton adultOption;
    private RadioButton elderlyOption;
    private Button startButton;
    private static final String TAG = "State Change";
    private DatabaseReference mDatabaseLeaderboard;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.onboarding_step_eight, container, false);

        mDatabaseLeaderboard = FirebaseDatabase.getInstance().getReference().child("leaderboard");

        nameEditText = ((TextInputLayout) root.findViewById(R.id.nameOnboardingTV)).getEditText();
        descriptionEditText = ((TextInputLayout) root.findViewById(R.id.descriptionOnboardingTV)).getEditText();
        childOption = root.findViewById(R.id.childOptionOnboarding);
        adultOption = root.findViewById(R.id.adultOptionOnboarding);
        elderlyOption = root.findViewById(R.id.elderlyOptionOnboarding);
        startButton = root.findViewById(R.id.start_button);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameEditText.getText().toString().trim().equals("")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Invalid username! Name cannot be only whitespace characters.")
                            .setTitle("Username Error");
                    AlertDialog invalidUsername = builder.create();
                    invalidUsername.show();
                }else{
                    initializeSettings();
                    MainActivity.setAccountCreated();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        return root;
    }


    public void initializeSettings(){
        try{
            FileOutputStream fOut = getContext().openFileOutput("settings.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String option = "none";
            if (childOption.isChecked()){
                option = "child";
            }else{
                if (adultOption.isChecked()){
                    option = "adult";
                }else{
                    if (elderlyOption.isChecked()){
                        option = "elderly";
                    }else{
                        //nothing checked
                    }
                }
            }
            String toWrite = name+"\n"+option+"\n"+description;
            System.out.println(toWrite+"toWrite");
            osw.write(toWrite);
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }

        //initialize on firebase
        mDatabaseLeaderboard.child(nameEditText.getText().toString()).child("name").
                setValue(nameEditText.getText().toString());
        mDatabaseLeaderboard.child(nameEditText.getText().toString()).child("points").setValue(0);

    }

}
