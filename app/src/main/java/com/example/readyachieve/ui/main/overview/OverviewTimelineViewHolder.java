package com.example.readyachieve.ui.main.overview;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;
import com.github.vipulasri.timelineview.TimelineView;

public class OverviewTimelineViewHolder extends RecyclerView.ViewHolder {

    private TimelineView mTimelineView;
    TextView title;
    TextView date;
    ImageView image;

    public OverviewTimelineViewHolder(View itemView, int viewType){
        super(itemView);
        mTimelineView = itemView.findViewById(R.id.overview_timeline);
        mTimelineView.initLine(viewType);

        title = itemView.findViewById(R.id.overview_step_title);
        date = itemView.findViewById(R.id.overview_step_date);
        image = itemView.findViewById(R.id.overview_step_image);
    }


}
