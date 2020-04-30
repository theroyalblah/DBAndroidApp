package com.example.databaseproject;

import android.os.Build;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.MaterialViewHolder> {

    List<Material> mats;
    public MaterialAdapter(List<Material> mats) {
        this.mats = mats;
    }

    public static class MaterialViewHolder extends RecyclerView.ViewHolder {
        public ConstraintLayout constraintLayout;

        public MaterialViewHolder(ConstraintLayout layout) {
            super(layout);
            constraintLayout = layout;
        }
    }

    @NonNull
    @Override
    public MaterialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.material_row, parent, false);

        MaterialViewHolder viewHolder = new MaterialViewHolder(layout);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MaterialViewHolder holder, int index) {

        ConstraintLayout layout = ((CardView) holder.constraintLayout.getViewById(R.id.materialCardView))
                .findViewById(R.id.materialLayout);

        TextView titleText = layout.findViewById(R.id.materialTitle);
        TextView authorText = layout.findViewById(R.id.materialAuthor);
        TextView dateText = layout.findViewById(R.id.materialDate);
        TextView notesText = layout.findViewById(R.id.materialNotes);
        TextView typeText = layout.findViewById(R.id.materialType);

        Material mat = mats.get(index);
        String text = String.format("Author: %s", mat.author);
        authorText.setText(text);

        text = String.format("Notes: %s", mat.notes);
        notesText.setText(text);

        dateText.setText(mat.assigned_date);
        typeText.setText(mat.type);

        titleText.setClickable(true);
        titleText.setMovementMethod(LinkMovementMethod.getInstance());
        String titleLinkText = String.format("<a href='%s'>%s</a>", mat.url, mat.title);
        titleText.setText(Html.fromHtml(titleLinkText, Html.FROM_HTML_MODE_COMPACT));
    }

    @Override
    public int getItemCount() {
        return mats.size();
    }

}
