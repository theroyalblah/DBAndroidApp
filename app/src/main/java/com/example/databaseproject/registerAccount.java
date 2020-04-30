package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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
                    String grade = editGrade.getText().toString();
                    String parent = parentEmail.getText().toString();

                    String getParentID = String.format("SELECT * FROM parents WHERE email='%s", parent);
                    JSONArray getParentIDResponse = QueryBuilder.performQuery(getParentID);
                    res = QueryBuilder.getJSONObject(getParentIDResponse, 0);
                    try {
                        int parentID = res.getInt("id");

                        String updateStudent = String.format("INSERT INTO students (student_id, grade, parent_id) VALUES ('%s', '%s', '%s');", userId, grade, parentID);
                        QueryBuilder.performQuery(updateStudent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }



                startActivity(loginActivityIntent);
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
