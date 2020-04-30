package com.example.databaseproject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class StudentLoginActivity extends AppCompatActivity {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        //get user type
        final User loggedInUser = UserSession.getUser();
        final String UserType = loggedInUser.getUserType();
        String UsersName = "Default";
        try {
            UsersName = loggedInUser.getName();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Print user and type
        TextView userTextType = findViewById(R.id.userTextType);
        userTextType.setText("Welcome " + UsersName + "\n" + "You are a " + UserType);



        // Intent
        final Intent editAccountIntent = new Intent(this, EditStudentAccountActivity.class);
        final Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        final Intent enrollAsMenteeIntent = new Intent(this, EnrollAsMenteeStudent.class);
        final Intent enrollAsMentorIntent = new Intent(this, EnrollAsMentorStudent.class);
        final Intent viewMeetingsIntent = new Intent(this, ViewMeetingsActivity.class);
        final Intent viewChildrenIntent = new Intent(this, ViewChildren.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final Button registerAsMentor = findViewById(R.id.enrollMentor);
        final Button registerAsMentee = findViewById(R.id.enrollMentee);
        final Button editAccount = findViewById(R.id.editAccount);
        final Button logOutButton = findViewById(R.id.logOutButton);
        final Button adminButton = findViewById(R.id.adminRole);
        final Button viewMeetings = findViewById(R.id.viewMeetings);
        final Button viewChildren = findViewById(R.id.childrenButton);

        viewMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(viewMeetingsIntent);
            }

        });

        registerAsMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(enrollAsMentorIntent);
            }

        });

        editAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(editAccountIntent);
            }
        });

        registerAsMentee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(enrollAsMenteeIntent);
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(adminIntent);
            }
        });

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSession.clearCurrentUser();
                startActivity(loginActivityIntent);
            }
        });

        viewChildren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(viewChildrenIntent);
            }

        });

        //Only students are allowed to register
        if (UserType != "student") {
            registerAsMentee.setVisibility(View.GONE);
            registerAsMentor.setVisibility(View.GONE);
            viewMeetings.setVisibility(View.GONE);
        } else {
            registerAsMentee.setVisibility(View.VISIBLE);
            registerAsMentor.setVisibility(View.VISIBLE);
            viewMeetings.setVisibility(View.VISIBLE);
        }

        //Only Admins can use admin
        if (UserType != "admin") {
            adminButton.setVisibility(View.GONE);
        } else {
            adminButton.setVisibility(View.VISIBLE);
        }

        //Only Parents can use Children Button
        if (UserType != "parent") {
            viewChildren.setVisibility(View.GONE);
        } else {
            viewChildren.setVisibility(View.VISIBLE);
        }
    }
}
