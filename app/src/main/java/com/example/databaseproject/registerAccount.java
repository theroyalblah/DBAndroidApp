package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_student_account);
        // Intent
        final Intent loginActivityIntent = new Intent(this, LoginActivity.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final EditText editEmail = findViewById(R.id.editEmail);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPassword = findViewById(R.id.editPassword);
        final EditText editGrade = findViewById(R.id.editGrade);
        final EditText editPhone = findViewById(R.id.editPhone);
        final Button submitButton = findViewById(R.id.submitButton);
        final EditText parentEmail = findViewById(R.id.parentEmail);

        final CheckBox isParentRadio = findViewById(R.id.parentCheckBox);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString().toLowerCase();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();
                String phone = editPhone.getText().toString();
                String grade = editGrade.getText().toString();
                String parent = parentEmail.getText().toString();
                boolean isRegistrationValid = true;

                if (email.matches("")) {
                    String text = "Please enter an email";
                    Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                    isRegistrationValid = false;
                }
                else if (name.matches("")) {
                    String text = "Please enter a name";
                    Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                    isRegistrationValid = false;
                }
                else if (password.matches("")) {
                    String text = "Please enter a password";
                    Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                    isRegistrationValid = false;
                }
                else if (phone.matches("")) {
                    String text = "Please enter a phone number";
                    Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                    isRegistrationValid = false;
                }
                // student exclusive fields
                if(!isParentRadio.isChecked()) {
                    if (grade.matches("")) {
                        String text = "Please enter a grade";
                        Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                        isRegistrationValid = false;
                    }
                    else if (parent.matches("")) {
                        String text = "Please enter a parent email";
                        Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                        isRegistrationValid = false;
                    }
                }

                if (isRegistrationValid) {
                    // TODO can create a user that may not actually be valid: student can be created without valid parent email
                    String insertUser = String.format("INSERT INTO users (email, password, name, phone) VALUES ('%s', '%s', '%s', '%s');", email, password, name, phone);
                    QueryBuilder.performQuery(insertUser);
                    
                    String getUserID = String.format("SELECT * FROM users where email='%s';", email);
                    JSONArray getUserIDResponse = QueryBuilder.performQuery(getUserID);
                    JSONObject res = QueryBuilder.getJSONObject(getUserIDResponse, 0);

                    int userId = 0;
                    try {
                        userId = res.getInt("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(isParentRadio.isChecked()) {
                        String parent_insert = String.format("INSERT INTO parents (parent_id) VALUES ('%s');", userId);
                        QueryBuilder.performQuery(parent_insert);
                    }
                    // user is a student
                    else {

                        String getParentID = String.format("SELECT * FROM users WHERE email='%s';", parent);
                        JSONArray getParentIDResponse = QueryBuilder.performQuery(getParentID);
                        res = QueryBuilder.getJSONObject(getParentIDResponse, 0);
                        if(res.length() != 0) {
                            try {
                                int parentID = res.getInt("id");
                                String checkParentID = String.format("SELECT * FROM parents WHERE parent_id='%s';", parentID);
                                JSONArray checkParentIDResponse = QueryBuilder.performQuery(checkParentID);
                                res = QueryBuilder.getJSONObject(checkParentIDResponse, 0);

                                // if id is actually a parent
                                if(res.length() != 0) {
                                    String updateStudent = String.format("INSERT INTO students (student_id, grade, parent_id) VALUES ('%s', '%s', '%s');", userId, grade, parentID);
                                    QueryBuilder.performQuery(updateStudent);
                                } else {
                                    String text = "Your parent email was not found. Try again.";
                                    Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                                    isRegistrationValid = false;
                                }

                            } catch (JSONException e) {
                                String text = "Your parent email was not found. Try again.";
                                Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                                isRegistrationValid = false;
                            }
                        } else {

                            String text = "Your parent email was not found. Try again.";
                            Toast.makeText(RegisterAccount.this, text, Toast.LENGTH_SHORT).show();
                            isRegistrationValid = false;
                        }

                    }
                    if (isRegistrationValid) {
                        startActivity(loginActivityIntent);
                    }
                }
            }
        });

        isParentRadio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isParentRadio.isChecked()) {
                    parentEmail.setVisibility(View.GONE);
                    editGrade.setVisibility(View.GONE);
                } else {
                    parentEmail.setVisibility(View.VISIBLE);
                    editGrade.setVisibility(View.VISIBLE);
                }
            }

        });

    }
}
