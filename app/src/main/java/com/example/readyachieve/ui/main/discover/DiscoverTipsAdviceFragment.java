package com.example.readyachieve.ui.main.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.example.readyachieve.model.User;

import java.util.ArrayList;

public class DiscoverTipsAdviceFragment extends Fragment {

    private Button returnTipsAdviceButton;
    private ArrayList<String> tipsAdviceArrList;

    RecyclerView discoverTipsAdviceRecyclerView;
    RecyclerView.LayoutManager discoverTipsAdviceLayoutManager;
    RecyclerView.Adapter discoverTipsAdviceAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_discover_tips_advice, container, false);

        returnTipsAdviceButton = root.findViewById(R.id.returnTipAdviceButton);

        returnTipsAdviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).navigate(R.id.tips_advice_to_home);
            }
        });

        loadTipsAdviceArrList();

        discoverTipsAdviceRecyclerView = root.findViewById(R.id.discover_tips_advice_recycler_view);
        discoverTipsAdviceLayoutManager = new LinearLayoutManager(getContext());
        discoverTipsAdviceRecyclerView.setLayoutManager(discoverTipsAdviceLayoutManager);
        discoverTipsAdviceAdapter = new DiscoverTipsAdviceRecyclerAdapter(tipsAdviceArrList);
        discoverTipsAdviceRecyclerView.setAdapter(discoverTipsAdviceAdapter);

        return root;
    }

    private void loadTipsAdviceArrList() {
        tipsAdviceArrList = new ArrayList<String>();
        String[] tempTipAdviceArr = {"Choose goals that are worthwhile","Choose goals that are achievable stretches",
                "Make your goals specific", "Commit to your goals","Make your goal public","Prioritize your goals",
                "Make your goals real to you", "Set deadlines to accomplish your goals","Evaluate your goals",
                "Reward yourself for accomplishment"};
        for (int i = 0;i<tempTipAdviceArr.length;i++){
            tipsAdviceArrList.add(tempTipAdviceArr[i]);
        }
    }
}
