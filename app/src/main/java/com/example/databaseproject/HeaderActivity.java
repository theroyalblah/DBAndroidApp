package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.widget.TextView;


public class HeaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_header);
        // Intent
        final Intent loginIntent = new Intent(this, LoginActivity.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final Button logOutButton = findViewById(R.id.logoutButton);

        welcomeText.setText("Welcome, PeePee");
        logOutButton.setText("Log Out");
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);
            }
        });
    }
}
