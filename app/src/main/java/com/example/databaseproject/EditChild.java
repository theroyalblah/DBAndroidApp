package com.example.databaseproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditChild extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_child);

        // Intent
        final Intent returnViewChildren = new Intent(this, ViewChildren.class);
        final Intent returnProfile = new Intent(this, StudentLoginActivity.class);


        //Get inputted id
        final String childId = ViewChildren.childId;

        //Search their id
        String query = String.format("SELECT name FROM users WHERE id = '%s'",
                childId);

        JSONArray response = QueryBuilder.performQuery(query);
        JSONObject res;
        res = QueryBuilder.getJSONObject(response, 0);
        String show_Name = "";

        try {
            show_Name = res.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final EditText editEmail = findViewById(R.id.editEmail);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPassword = findViewById(R.id.editPassword);
        final EditText editGrade = findViewById(R.id.editGrade);
        final EditText editPhone = findViewById(R.id.editPhone);
        final Button submitButton = findViewById(R.id.submitButton);
        final Button returnButton = findViewById(R.id.returnButton);

        final TextView childName = findViewById(R.id.childName);
        childName.setText(show_Name);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = editEmail.getText().toString().toLowerCase();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();
                String phone = editPhone.getText().toString();

                String format = String.format("UPDATE users SET email = '%s', password = '%s', name = '%s', phone = '%s' WHERE id = '%s'",
                        email, password, name, phone, childId);
                QueryBuilder.performQuery(format);

                String grade = editGrade.getText().toString();
                String updateGrade = String.format("UPDATE students SET grade = '%s' WHERE student_id = '%s'", grade, childId);

                QueryBuilder.performQuery(updateGrade);

                startActivity(returnViewChildren);
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(returnViewChildren);

            }
        });
    }
}
