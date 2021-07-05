package com.example.iasubstituteteacher.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.R;

/**
 * A class controlling the recycler view which shows all the OpenJobs, given that the open job is
 * still active and has not passed the current date and time.
 */

public class OpenJobsViewHolder extends RecyclerView.ViewHolder
{
    protected TextView subjectText;
    protected TextView dateText;
    protected TextView timeText;
    protected ConstraintLayout openJobLayout;

    public OpenJobsViewHolder(@NonNull View itemView) {
        super(itemView);

        subjectText = itemView.findViewById(R.id.subjectText);
        dateText = itemView.findViewById(R.id.dateText);
        timeText = itemView.findViewById(R.id.timeText);

        this.openJobLayout = itemView.findViewById(R.id.openJob_parent_layout);
    }

    public TextView getSubjectText() {
        return subjectText;
    }

    public TextView getDateText() {
        return dateText;
    }

    public TextView getTimeText() {
        return timeText;
    }

    public ConstraintLayout getOpenJobLayout() {
            return openJobLayout;
    }
}
