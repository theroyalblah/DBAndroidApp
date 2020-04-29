package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.widget.TextView;

import org.json.JSONException;


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
        final User loggedInUser = UserSession.getUser();
        try {
            String text = String.format("Welcome, %s", loggedInUser.getName());
            welcomeText.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        logOutButton.setText("Log Out");
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSession.clearCurrentUser();
                startActivity(loginIntent);
            }
        });
    }
}
