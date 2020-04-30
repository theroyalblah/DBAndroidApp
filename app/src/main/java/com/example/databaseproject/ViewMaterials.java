package com.example.databaseproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewMaterials extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_view_materials);
        super.onCreate(savedInstanceState);

        String query = String.format("SELECT * FROM material WHERE material_id IN (SELECT material_id FROM assign WHERE meet_id=%s) ORDER BY assigned_date DESC", MeetingAdapter.meetingMaterialId);
        JSONArray response = QueryBuilder.performQuery(query);

        List<Material> mats = new ArrayList<Material>();
        JSONObject res;
        for (int i = 0; i < response.length(); i++) {
            res = QueryBuilder.getJSONObject(response, i);
            try {
                mats.add(new Material(res.getString("title"), res.getString("author"), res.getString("type"), res.getString("url"),
                        res.getString("assigned_date"), res.getString("notes")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        final RecyclerView materialsRecyclerView = findViewById(R.id.recyclerViewMaterials);

        materialsRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        materialsRecyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        MaterialAdapter mAdapter = new MaterialAdapter(mats);
        materialsRecyclerView.setAdapter(mAdapter);

    }
}
