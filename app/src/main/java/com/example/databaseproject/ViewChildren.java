package com.example.databaseproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewChildren extends AppCompatActivity {

    String children_names[];
    String names_output = "";

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
                names_output += (i+1) + " " + children_names[i] + "\n";
        }

        TextView childrenText = findViewById(R.id.childrenText);
        childrenText.setText(names_output);


        final Intent editChildIntent = new Intent(this, EditChild.class);

        final Button editChildButton = findViewById(R.id.editChildButton);

        editChildButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(editChildIntent);
            }

        });
    }

    //get user info
    final User loggedInUser = UserSession.getUser();
    final int parentsID = loggedInUser.getId();

    private void searchChildren() throws JSONException{

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
    }

}
