package com.example.databaseproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> {

    String grades[];
    String meeting_names[];
    String dates[];
    int enrollment[];
    String times[];
    String meeting_ids[];
    Context context;

    //expects mentor, mentee, or viewer
    String enroll_state;

    int userID;

    public static int targetMeetingId;

    //instantiate the basis of the adapter
    public MeetingAdapter(Context ct, String grades[], String meeting_names[], String dates[], int enrollment[], String times[], String meeting_ids[], String enroll_state, int userID)
    {
        this.context = ct;
        this.grades = grades;
        this.meeting_names = meeting_names;
        this.dates = dates;
        this.enrollment = enrollment;
        this.times = times;
        this.meeting_ids = meeting_ids;
        this.enroll_state = enroll_state;
        this.userID = userID;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.meeting_enroll_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return meeting_names.length;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        Button enrollButton;
        Button viewMeetingButton;
        Button leaveAllMeetings;

        TextView gradelevelText;
        TextView meetingName;
        TextView timeText;
        TextView dateText;
        TextView capacityText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            enrollButton = itemView.findViewById(R.id.enrollButton);
            viewMeetingButton = itemView.findViewById(R.id.viewMeetingButton);
            leaveAllMeetings = itemView.findViewById(R.id.leaveAllMeatings);

            gradelevelText = itemView.findViewById(R.id.gradeLevelText);
            capacityText = itemView.findViewById(R.id.capacityText);
            meetingName = itemView.findViewById(R.id.meetingName);
            timeText = itemView.findViewById(R.id.timeViewText);
            dateText = itemView.findViewById(R.id.dateMeeting);
        }
    }

    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, final int index) {

        viewHolder.meetingName.setText(meeting_names[index]);
        String text = String.format("Grade Level: %s", grades[index]);
        viewHolder.gradelevelText.setText(text);

        viewHolder.dateText.setText(dates[index]);

        // military time should serve well enough
        viewHolder.timeText.setText(times[index]);
        int enrollmentAndYou = enrollment[index];
        
        if(enroll_state == "mentee") {
            viewHolder.enrollButton.setText("Enroll");
            viewHolder.viewMeetingButton.setText("Enroll All");
            viewHolder.leaveAllMeetings.setVisibility(View.GONE);
        }
        else if (enroll_state == "mentor") {
            viewHolder.enrollButton.setText("Enroll");
            viewHolder.viewMeetingButton.setEnabled(false);
            viewHolder.viewMeetingButton.setVisibility(View.GONE);
            viewHolder.leaveAllMeetings.setVisibility(View.GONE);
        }
        else {
            enrollmentAndYou += 1;
            viewHolder.enrollButton.setText("Leave");
            viewHolder.viewMeetingButton.setText("View");
        }

        viewHolder.capacityText.setText(enrollmentAndYou + "/6");

        viewHolder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enroll(enroll_state, index);
            }
        });

        viewHolder.leaveAllMeetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveAllMeetings(index);
            }
        });

        viewHolder.viewMeetingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                viewMaterials(enroll_state, index);
            }
        });
    }

    private void enroll(String enroll_state, int index) {
        String query;

        if(enroll_state.equals("mentee")) {
            final Intent StudentLoginIntent = new Intent(context, StudentLoginActivity.class);
            query = String.format("INSERT INTO mentees VALUES ('%s')", userID);
            QueryBuilder.performQuery(query);
            query = String.format("INSERT INTO enroll VALUES ('%s', '%s')", meeting_ids[index], userID);
            QueryBuilder.performQuery(query);
            context.startActivity(StudentLoginIntent);
        }
        else if(enroll_state.equals("mentor")) {
            final Intent StudentLoginIntent = new Intent(context, StudentLoginActivity.class);

            query = String.format("INSERT INTO enroll2 VALUES ('%s', '%s')", meeting_ids[index], userID);
            QueryBuilder.performQuery(query);
            query = String.format("INSERT INTO mentors VALUES ('%s')", userID);
            QueryBuilder.performQuery(query);

            context.startActivity(StudentLoginIntent);
        }
        else {
            final Intent viewMeetingsActivity = new Intent(context, StudentLoginActivity.class);

            query = String.format("DELETE FROM enroll WHERE meet_id = '%s' AND mentee_id = '%s'", meeting_ids[index], userID);
            QueryBuilder.performQuery(query);
            query = String.format("DELETE FROM enroll2 WHERE meet_id = '%s' AND mentor_id = '%s'", meeting_ids[index], userID);
            QueryBuilder.performQuery(query);
            context.startActivity(viewMeetingsActivity);

        }

    }

    private void leaveAllMeetings(int index) {

        final Intent viewMeetingsActivity = new Intent(context, StudentLoginActivity.class);
        String query = String.format("DELETE FROM enroll WHERE mentee_id = '%s' AND meet_id IN (SELECT meet_id FROM meetings WHERE meet_name = '%s' AND group_id = '%s')", userID, meeting_names[index], grades[index]);
        QueryBuilder.performQuery(query);
        context.startActivity(viewMeetingsActivity);

    }

    private void viewMaterials(String enroll_state, int index) {

        if (enroll_state.equals("mentee")) {
            final Intent StudentLoginIntent = new Intent(context, StudentLoginActivity.class);
            String query = String.format("INSERT INTO enroll SELECT meet_id, '%s' FROM meetings  WHERE group_id = '%s' AND meet_name = '%s'", userID, grades[index], meeting_names[index]);
            QueryBuilder.performQuery(query);
            context.startActivity(StudentLoginIntent);
        } else {
            // final Intent viewMaterialsIntent = new Intent(context, ViewMaterialsActivity.class);
            // context.startActivity(viewMaterialsIntent);
        }
    }

}