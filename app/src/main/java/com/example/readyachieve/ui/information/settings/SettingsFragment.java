package com.example.readyachieve.ui.information.settings;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.readyachieve.R;
import com.example.readyachieve.model.GoalSetUp;
import com.example.readyachieve.model.MilestoneSetUp;
import com.example.readyachieve.model.User;
import com.example.readyachieve.ui.main.dashboard.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
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

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private RadioButton childOption;
    private RadioButton adultOption;
    private RadioButton elderlyOption;
    private FloatingActionButton saveSettingsButton;
    private static final String TAG = "State Change";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);


        nameEditText = ((TextInputLayout) root.findViewById(R.id.nameTVSettings)).getEditText();
        descriptionEditText = ((TextInputLayout) root.findViewById(R.id.descriptionTVSettings)).getEditText();
        childOption = root.findViewById(R.id.childOption);
        adultOption = root.findViewById(R.id.adultOption);
        elderlyOption = root.findViewById(R.id.elderlyOption);
        saveSettingsButton = root.findViewById(R.id.save_settings_button);

        root.findViewById(R.id.nameTVSettings).setClickable(false);
        root.findViewById(R.id.nameTVSettings).setEnabled(false);


        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                Toast.makeText(getContext(),"Saved successfully.",Toast.LENGTH_SHORT).show();
            }
        });
        readSettings();

        return root;
    }

    public void readSettings(){
        try{
            FileInputStream fIn = getContext().openFileInput("settings.csv");
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String temp = bufferedReader.readLine();
            if (temp.equals("")||temp==null){
                System.out.println("error");
            }else{
                String name = temp;
                String option = bufferedReader.readLine();
                String description = "";
                String tempDescription;
                int count = 0;
                while((tempDescription = bufferedReader.readLine())!=null){
                    if (count==0){
                        description+=tempDescription;
                        count++;
                    }else{
                        description+="\n"+tempDescription;
                    }
                }
                System.out.println(description+"description");
                System.out.println(option+"option");
                nameEditText.setText(name);
                descriptionEditText.setText(description);
                switch (option){
                    case "child": childOption.setChecked(true); break;
                    case "adult": adultOption.setChecked(true); break;
                    case "elderly": elderlyOption.setChecked(true); break;
                    default: break;
                }
            }
            bufferedReader.close();
            isr.close();
            fIn.close();
        }catch (IOException e){
            Log.i(TAG, "IOException called");
        }


    }


    public void saveSettings(){
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
    }

}
