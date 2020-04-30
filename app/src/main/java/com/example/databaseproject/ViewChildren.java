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

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewChildren extends AppCompatActivity {

    String children_names[];
    String children_id[];
    String names_output = "";

    public static String childId;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_children);

        try {
            searchChildren();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < children_names.length; i++){
                names_output += children_id[i] + " " + children_names[i] + "\n";
        }

        TextView childrenText = findViewById(R.id.childrenText);
        childrenText.setText(names_output);

        final EditText editChild = findViewById(R.id.editChild);

        final Intent editChildIntent = new Intent(this, EditChild.class);

        final Button editChildButton = findViewById(R.id.editChildButton);

        editChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                childId = editChild.getText().toString();
                startActivity(editChildIntent);
            }

        });

        final Button returnButton = findViewById(R.id.returnButton2);
        final Intent returnProfile = new Intent(this, StudentLoginActivity.class);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(returnProfile);

            }
        });
    }

    //get user info
    final User loggedInUser = UserSession.getUser();
    final int parentsID = loggedInUser.getId();

    private void searchChildren() throws JSONException{


        //Search children names
        String query = String.format("SELECT name FROM users WHERE id IN" +
                "                      (SELECT student_id FROM students WHERE parent_id = '%d')",
                parentsID);

        JSONArray response = QueryBuilder.performQuery(query);
        JSONObject res;

        children_names = new String[response.length()];

        for (int i = 0; i < response.length(); i++){

            res = QueryBuilder.getJSONObject(response, i);
            children_names[i] = res.getString("name");
        }

        //Search their id
        String query2 = String.format("SELECT id FROM users WHERE id IN" +
                        "                      (SELECT student_id FROM students WHERE parent_id = '%d')",
                parentsID);

        JSONArray response2 = QueryBuilder.performQuery(query2);
        JSONObject res2;

        children_id = new String[response2.length()];

        for (int i = 0; i < response2.length(); i++){

            res2 = QueryBuilder.getJSONObject(response2, i);
            children_id[i] = res2.getString("id");
        }
    }

}
