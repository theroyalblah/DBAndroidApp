package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StudentLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_login);
        // Intent
        final Intent editAccountIntent = new Intent(this, EditStudentAccountActivity.class);
        final Intent loginActivityIntent = new Intent(this, LoginActivity.class);
        final Intent enrollAsMenteeIntent = new Intent(this, EnrollAsMenteeStudent.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final Button registerAsMentor = findViewById(R.id.enrollMentor);
        final Button registerAsMentee = findViewById(R.id.enrollMentee);
        final Button editAccount = findViewById(R.id.editAccount);
        final Button logOutButton = findViewById(R.id.logOutButton);
        final User loggedInUser = UserSession.getUser();

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


        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSession.clearCurrentUser();
                startActivity(loginActivityIntent);
            }
        });
    }
}
