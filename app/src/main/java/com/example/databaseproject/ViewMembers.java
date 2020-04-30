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

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ViewMembers extends AppCompatActivity {

    String menteeMemberNames[];
    String mentorMemberNames[];
    String menteeNames_output = "";
    String mentorNames_output = "";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_members);
        super.onCreate(savedInstanceState);

        try {
            searchMembers();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < menteeMemberNames.length; i++){
            menteeNames_output += menteeMemberNames[i] + "\n";
        }

        for(int i = 0; i < mentorMemberNames.length; i++){
            mentorNames_output += mentorMemberNames[i] + "\n";
        }

        TextView menteesMembersText = findViewById(R.id.menteeMembers);
        menteesMembersText.setText(menteeNames_output);
        TextView mentorsMembersText = findViewById(R.id.mentorMembers);
        mentorsMembersText.setText(mentorNames_output);

        final Button returnButton = findViewById(R.id.returnToMat);
        final Intent returnProfile = new Intent(this, ViewMeetingsActivity.class);

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(returnProfile);

            }
        });
    }

    private void searchMembers() throws JSONException{

        String query = String.format("SELECT name FROM users WHERE id IN (SELECT mentee_id FROM enroll WHERE meet_id='%s')", MeetingAdapter.meetingMaterialId);
        JSONArray response = QueryBuilder.performQuery(query);
        JSONObject res;

        for (int i = 0; i < response.length(); i++) {
            res = QueryBuilder.getJSONObject(response, i);
            try {
                menteeMemberNames[i] = res.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        String query2 = String.format("SELECT name FROM users WHERE id IN (SELECT mentor_id FROM enroll2 WHERE meet_id='%s')", MeetingAdapter.meetingMaterialId);
        JSONArray response2 = QueryBuilder.performQuery(query2);
        JSONObject res2;

        for (int i = 0; i < response2.length(); i++) {
            res2 = QueryBuilder.getJSONObject(response2, i);
            try {
                mentorMemberNames[i] = res2.getString("name");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }
}
