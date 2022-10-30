package com.example.readyachieve.ui.information.feedback;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.readyachieve.R;
import com.example.readyachieve.ui.main.dashboard.DashboardViewModel;
import com.google.android.material.textfield.TextInputLayout;

public class FeedbackFragment extends Fragment {


    private EditText subjectEmailFeedback;
    private EditText contentEmailFeedback;
    private Button sendEmailButton;
    private String[] to = {"h1810151@nushigh.edu.sg"};
    private FeedbackViewModel feedbackViewModel;
    NotificationManager notificationManager;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        subjectEmailFeedback = ((TextInputLayout) root.findViewById(R.id.email_subject)).getEditText();
        contentEmailFeedback = ((TextInputLayout) root.findViewById(R.id.email_content)).getEditText();
        sendEmailButton = root.findViewById(R.id.send_email);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel("com.example.readyachieve.notifs","ReadyAchieve notifications",
                "ReadyAchieve");

        sendEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
                sendNotificationEmail();
            }
        });

        return root;
    }

    public void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subjectEmailFeedback.getText().toString());
        emailIntent.putExtra(Intent.EXTRA_TEXT, contentEmailFeedback.getText().toString());
        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getActivity(), "No email client installed", Toast.LENGTH_SHORT).show();
        }
    }

    private void createNotificationChannel(String id, String name, String description) {
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(id,name,importance);
        channel.setDescription(description);
        channel.enableLights(true);
        channel.setLightColor(Color.RED);
        channel.enableVibration(true);
        channel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,100});
        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotificationEmail() {
        int notificationID = 101;

        Intent resultIntent = new Intent(getContext(), getActivity().getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        String channelID = "com.example.readyachieve.notifs";
        Notification notification = new Notification.Builder(getContext(),
                channelID).setContentTitle("Feedback to developer")
                .setContentText("Thank you for your feedback and suggestions!")
                .setSmallIcon(R.drawable.logo)
                .setChannelId(channelID)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(notificationID, notification);
    }

}
