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
        final Intent studentLoginIntent = new Intent(this, HeaderActivity.class);

        // GUI
        final Button loginButton = findViewById(R.id.loginButton);

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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
