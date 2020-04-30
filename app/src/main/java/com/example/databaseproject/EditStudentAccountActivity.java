package com.example.databaseproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditStudentAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student_account);
        // Intent
        final Intent studentLoginIntent = new Intent(this, StudentLoginActivity.class);

        // GUI
        final TextView welcomeText = findViewById(R.id.welcomeText);

        final EditText editEmail = findViewById(R.id.editEmail);
        final EditText editName = findViewById(R.id.editName);
        final EditText editPassword = findViewById(R.id.editPassword);
        final EditText editGrade = findViewById(R.id.editGrade);
        final EditText editPhone = findViewById(R.id.editPhone);
        final Button submitButton = findViewById(R.id.submitButton);

        User loggedInUser = UserSession.getUser();
        if (UserSession.getUser().getUserType().equals("parent")) {
            editGrade.setVisibility(View.GONE);
        }


        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User loggedInUser = UserSession.getUser();
                int userId = loggedInUser.getId();

                String email = editEmail.getText().toString().toLowerCase();
                String name = editName.getText().toString();
                String password = editPassword.getText().toString();
                String phone = editPhone.getText().toString();

                String format = String.format("UPDATE users SET email = '%s', password = '%s', name = '%s', phone = '%s' WHERE id = '%s'",
                        email, name, password, phone, userId);
                QueryBuilder.performQuery(format);

                // student account
                if (UserSession.getUser().getUserType().equals("student")){
                    String grade = editGrade.getText().toString();
                    String updateGrade = String.format("UPDATE students SET grade = '%s' WHERE student_id = '%s'", grade, userId);

                    QueryBuilder.performQuery(updateGrade);

                }

                // Grab new user data
                String getNewData = String.format("SELECT * from users WHERE id = '%s'", userId);

                JSONArray response = QueryBuilder.performQuery(getNewData);
                JSONObject res = QueryBuilder.getJSONObject(response, 0);
                try {
                    User updatedUser = new User(res.getInt("id"));
                    UserSession.setCurrentUser(updatedUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                startActivity(studentLoginIntent);
            }
        });
    }
}
