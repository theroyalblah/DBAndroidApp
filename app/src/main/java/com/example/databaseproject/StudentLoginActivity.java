package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class StudentLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        //get user type
        final User loggedInUser = UserSession.getUser();
        final String UserType = loggedInUser.getUserType();

        TextView userTextType = findViewById(R.id.userTextType);
        final TextView welcomeType = findViewById(R.id.welcomeType);


        // Intent
        final Intent editAccountIntent = new Intent(this, EditStudentAccountActivity.class);
        final Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        final Intent enrollAsMenteeIntent = new Intent(this, EnrollAsMenteeStudent.class);
        final Intent enrollAsMentorIntent = new Intent(this, EnrollAsMentorStudent.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final Button registerAsMentor = findViewById(R.id.enrollMentor);
        final Button registerAsMentee = findViewById(R.id.enrollMentee);
        final Button editAccount = findViewById(R.id.editAccount);
        final Button logOutButton = findViewById(R.id.logOutButton);

        registerAsMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(editAccountIntent);
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

        registerAsMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(enrollAsMentorIntent);
            }
        });


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSession.clearCurrentUser();
                startActivity(loginActivityIntent);
            }
        });

        //Only students are allowed to register
        if (UserType != "student") {
            registerAsMentee.setVisibility(View.GONE);
            registerAsMentor.setVisibility(View.GONE);
        } else {
            registerAsMentee.setVisibility(View.VISIBLE);
            registerAsMentor.setVisibility(View.VISIBLE);
        }

    }
}
