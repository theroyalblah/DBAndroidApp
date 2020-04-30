package com.example.databaseproject;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;


public class ViewMeetingsActivity extends AppCompatActivity {

    String group_ids[];
    String meeting_names[];
    String dates[];
    int enrollment_size[];
    String times[];
    String meet_ids[];

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_enroll_as_mentee);
        final RecyclerView recyclerView = findViewById(R.id.recyclerView);

        try {
            buildMeetingList();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MeetingAdapter meetingEnrollAdapter = new MeetingAdapter(this, group_ids, meeting_names, dates, enrollment_size, times, meet_ids, "viewer", UserSession.getUser().getId());
        recyclerView.setAdapter(meetingEnrollAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void buildMeetingList() throws JSONException {
        int userID = UserSession.getUser().getId();

        String date = "2000-01-01";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("EST"));
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        date = dateTimeFormatter.format(now);

        String query = String.format("SELECT * FROM meetings WHERE meet_id IN (SELECT meet_id FROM enroll WHERE mentee_id = '%s') OR " +
                                        "meet_id IN (SELECT meet_id FROM enroll2 WHERE mentor_id = '%s')", userID, userID);

        JSONArray response = QueryBuilder.performQuery(query);

        group_ids = new String[response.length()];
        meeting_names = new String[response.length()];
        dates = new String[response.length()];
        enrollment_size = new int[response.length()];
        times = new String[response.length()];
        meet_ids = new String[response.length()];

        JSONObject res;
        JSONArray getTimeSlot;
        JSONObject getTimeSlotResponse;


        JSONArray capacity;
        JSONObject capacityResponse;
        for (int i = 0; i < response.length(); i++) {
            res = QueryBuilder.getJSONObject(response, i);
            meeting_names[i] = res.getString("meet_name");
            dates[i] = res.getString("date");
            meet_ids[i] = String.valueOf(res.getString("meet_id"));
            group_ids[i] = String.valueOf(res.getInt("group_id"));

            getTimeSlot = QueryBuilder.performQuery(String.format("SELECT * FROM time_slot WHERE time_slot_id = (SELECT time_slot_id FROM meetings WHERE meet_id = '%s')", meet_ids[i]));
            getTimeSlotResponse = QueryBuilder.getJSONObject(getTimeSlot, 0);

            // only time in the form "05:00", so cut off the rest
            times[i] = String.valueOf(getTimeSlotResponse.getString("start_time")).substring(0,5);

            capacity = QueryBuilder.performQuery(String.format("SELECT * FROM enroll WHERE meet_id = '%s'", meet_ids[i]));
            capacityResponse = QueryBuilder.getJSONObject(capacity, 0);
            if (capacityResponse != null) {
                enrollment_size[i] = capacityResponse.length();
            } else {
                enrollment_size[i] = 0;
            }

        }

    }
}
