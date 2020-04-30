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

//Adapter to fill recyclerView with dynamic number of objects, based on meetings input
public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MyViewHolder> {

    String grades[];
    String meeting_names[];
    String dates[];
    int enrollment[];
    String times[];
    String meeting_ids[];
    Context context;
    String enroll_state; // 1=Mentee 2=Mentor 3=View
    int userID;
    String query;

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

        TextView meetingName;
        TextView timeText;
        TextView dateText;
        TextView capacityText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            capacityText = itemView.findViewById(R.id.capacityText);
            enrollButton = itemView.findViewById(R.id.enrollButton);
            viewMeetingButton = itemView.findViewById(R.id.viewMeetingButton);
            meetingName = itemView.findViewById(R.id.meetingName);
            timeText = itemView.findViewById(R.id.timeViewText);
            dateText = itemView.findViewById(R.id.dateMeeting);
        }
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.meetingName.setText((Integer.parseInt(grades[position]) + 5) + "th Grade " + meeting_names[position]);
        holder.timeText.setText(times[position] + " PM");
        holder.dateText.setText(dates[position]);
        holder.capacityText.setText(enrollment[position] + "/6");

        if(enroll_state == "mentee") {
            holder.enrollButton.setText("Enroll");
            holder.viewMeetingButton.setEnabled(true);
            holder.viewMeetingButton.setAlpha(1);
            holder.viewMeetingButton.setText("Bulk\n Enroll");
        }
        else if (enroll_state == "mentor")
        {
            holder.enrollButton.setText("Enroll");
            holder.viewMeetingButton.setEnabled(false);
            holder.viewMeetingButton.setAlpha(0);
        }
        else {
            holder.enrollButton.setText("Unenroll");
            holder.viewMeetingButton.setEnabled(true);
            holder.viewMeetingButton.setAlpha(1);
            holder.viewMeetingButton.setText("View");
        }

        holder.enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyAndEnroll(enroll_state, position);
            }
        });

        holder.viewMeetingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // vieworbulkMeeting(enroll_state, position);
            }
        });
    }

    private void verifyAndEnroll(String enroll_state, int position)
    {

        if(enroll_state == "mentor") //enroll as mentee
        {
            final Intent enrollMenteeStudentActivity = new Intent(context, EnrollAsMenteeStudent.class);
            query = String.format("INSERT INTO mentees VALUES ('%s')", userID);
            QueryBuilder.performQuery(query);
            query = String.format("INSERT INTO enroll VALUES ('%s', '%s')", meeting_ids[position], userID);
            QueryBuilder.performQuery(query);
            context.startActivity(enrollMenteeStudentActivity);
        }
        else if(enroll_state == "mentor") //enroll as mentor
        {
            final Intent enrollMentorStudentActivity = new Intent(context, EnrollAsMentorStudent.class);

            query = String.format("INSERT INTO enroll2 VALUES ('%s', '%s')", meeting_ids[position], userID);
            QueryBuilder.performQuery(query);
            query = String.format("INSERT INTO mentors VALUES ('%s')", userID);
            QueryBuilder.performQuery(query);

            context.startActivity(enrollMentorStudentActivity);
        }
        /*
        else if(enroll_state == 3) //unenroll
        {
            final Intent viewEnrolledMeetingsActivity = new Intent(ct, ViewEnrolledMeetingsActivity.class);
            AlertDialog.Builder builder = new AlertDialog.Builder(ct);
            builder.setTitle("How would you like to unenroll?");

            builder.setPositiveButton("Unenroll in Bulk",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {
                            query = String.format("DELETE FROM enroll WHERE mentee_id = '%s' AND meet_id IN (SELECT meet_id FROM meetings WHERE meet_name = '%s' AND group_id = '%s')", userID, meeting_names[position], grades[position]);
                            QueryExecution.executeQuery(query);
                            ct.startActivity(viewEnrolledMeetingsActivity);
                            dialog.cancel();
                        }
                    });
            builder.setNegativeButton("Unenroll Individually",
                    new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id)
                        {

                            query = String.format("DELETE FROM enroll WHERE meet_id = '%s' AND mentee_id = '%s'", meeting_ids[position], userID);
                            QueryExecution.executeQuery(query);
                            query = String.format("DELETE FROM enroll2 WHERE meet_id = '%s' AND mentor_id = '%s'", meeting_ids[position], userID);
                            QueryExecution.executeQuery(query);
                            ct.startActivity(viewEnrolledMeetingsActivity);
                            dialog.cancel();
                        }
                    });
            builder.create().show();

        }
         */



    }




}