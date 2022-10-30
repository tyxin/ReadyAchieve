package com.example.readyachieve.ui.main.home;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.readyachieve.R;

import java.util.ArrayList;

public class HomeRecyclerAdapter extends RecyclerView.Adapter<HomeRecyclerAdapter.ViewHolder> {

    private ArrayList<String> titleData;
    private ArrayList<String> descriptionData;
    private ArrayList<Integer> imageData;
    private RecyclerView.Adapter adapter;

    public HomeRecyclerAdapter(ArrayList<String> titleData, ArrayList<String> descriptionData,
                               ArrayList<Integer> imageData){
        this.titleData = titleData;
        this.descriptionData = descriptionData;
        this.imageData = imageData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_layout,parent,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
        //return null;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //green #EA5045 red #81C784
        holder.itemTitle.setText(titleData.get(position));
        holder.itemImage.setImageResource(imageData.get(position));
        //holder.itemDescription.setText(descriptionData.get(position));
    }

    @Override
    public int getItemCount() {
        return titleData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView itemImage;
        TextView itemTitle;
        TextView itemDescription;
        boolean isClicked = false;
        Context context;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context =itemView.getContext();
            itemTitle = itemView.findViewById(R.id.home_card_text);
            itemImage = itemView.findViewById(R.id.home_card_image);
            itemDescription = itemView.findViewById(R.id.home_card_description);

            itemView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    System.out.println(descriptionData.get(getAdapterPosition()));
                    if (!isClicked){
                        itemDescription.setText(descriptionData.get(getAdapterPosition()));
                        isClicked = true;
                    }else{
                        itemDescription.setText("");
                        isClicked = false;
                    }
                }
            });
        }
    }
}
