package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.EditText;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Intent
        final Intent studentLoginIntent = new Intent(this, StudentLoginActivity.class);
        final Intent registerAccountIntent = new Intent(this, RegisterAccount.class);

        // GUI
        final Button loginButton = findViewById(R.id.loginButton);
        final Button registerButton = findViewById(R.id.registerButton);

        final EditText emailField = findViewById(R.id.emailField);
        final EditText passField = findViewById(R.id.passField);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailField.getText().toString().toLowerCase();
                String password = passField.getText().toString();

                // unfortunately we leave the good graces of string concatenation to avoid SQL injection.
                String sql = String.format("SELECT * FROM users WHERE LOWER(email)='%s' AND password='%s';", email, password);
                JSONArray response = QueryBuilder.performQuery(sql);
                JSONObject res = QueryBuilder.getJSONObject(response, 0);
                if (res != null) {
                    try {
                        if (password.equals(res.getString("password"))) {

                            int userID = res.getInt("id");
                            User loggedInUser = new User(userID);
                            String userType = loggedInUser.getUserType();
                            UserSession.setCurrentUser(loggedInUser);
                            startActivity(studentLoginIntent);

                        } else {
                            // incorrect password
                            int duration = Toast.LENGTH_SHORT;

                            String text = "Your password was incorrect, please try again.";
                            Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                            toast.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                // no user found
                else {
                    int duration = Toast.LENGTH_SHORT;

                    String text = "Your email or password was incorrect. Please try again.";
                    Toast toast = Toast.makeText(LoginActivity.this, text, duration);
                    toast.show();
                }

            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(registerAccountIntent);
            }
        });
    }

}
