package com.example.readyachieve;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.example.readyachieve.ui.onboarding.OnBoardingStepEight;
import com.example.readyachieve.ui.onboarding.OnBoardingStepFive;
import com.example.readyachieve.ui.onboarding.OnBoardingStepFour;
import com.example.readyachieve.ui.onboarding.OnBoardingStepOne;
import com.example.readyachieve.ui.onboarding.OnBoardingStepSeven;
import com.example.readyachieve.ui.onboarding.OnBoardingStepSix;
import com.example.readyachieve.ui.onboarding.OnBoardingStepThree;
import com.example.readyachieve.ui.onboarding.OnBoardingStepTwo;
import com.google.android.material.tabs.TabLayout;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class OnboardingActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private static final String TAG = "State Change";
    // ViewPager Adapter class
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int i) {
            return mList.get(i);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        public void addFragment(Fragment fragment) {
            mList.add(fragment);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.onboarding_main_layout);

        // Initialize ViewPager view
        viewPager = findViewById(R.id.viewPagerOnBoarding);
        // create ViewPager adapter

        clearAllDataInInternalStorage();
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add All Fragments to ViewPager
        viewPagerAdapter.addFragment(new OnBoardingStepOne());
        viewPagerAdapter.addFragment(new OnBoardingStepTwo());
        viewPagerAdapter.addFragment(new OnBoardingStepThree());
        viewPagerAdapter.addFragment(new OnBoardingStepFour());
        viewPagerAdapter.addFragment(new OnBoardingStepFive());
        viewPagerAdapter.addFragment(new OnBoardingStepSix());
        viewPagerAdapter.addFragment(new OnBoardingStepSeven());
        viewPagerAdapter.addFragment(new OnBoardingStepEight());

        // Set Adapter for ViewPager
        viewPager.setAdapter(viewPagerAdapter);

        // Setup dot's indicator
        TabLayout tabLayout = findViewById(R.id.tabLayoutIndicator);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void clearAllDataInInternalStorage() {
        try{
            FileOutputStream fOut = openFileOutput("milestones.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write("");
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }

        try{
            FileOutputStream fOut = openFileOutput("goals.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write("");
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }

        try{
            FileOutputStream fOut = openFileOutput("history.csv",MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            osw.write("");
            osw.close();
            fOut.close();
        }catch (IOException e){
            Log.i(TAG,"IOException called");
        }
    }

    public void skipToProfileOnboarding(){
        viewPager.setCurrentItem(7);
    }
}
