package com.example.iasubstituteteacher.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.R;

/**
 * A class controlling the recycler view which shows all the RequestedJobs.
 */

public class RequestedJobsViewHolder extends RecyclerView.ViewHolder
{
    protected TextView subjectText;
    protected TextView dateText;
    protected TextView timeText;

    protected ConstraintLayout requestedJobLayout;

    public RequestedJobsViewHolder(View itemView)
    {
        super(itemView);

        subjectText = itemView.findViewById(R.id.subjectRowView);
        dateText = itemView.findViewById(R.id.dateRowView);
        timeText = itemView.findViewById(R.id.timeRowView);

        this.requestedJobLayout = itemView.findViewById(R.id.requestedJob_parent_layout);
    }

    public TextView getSubjectText()
    {
        return subjectText;
    }

    public TextView getDateText()
    {
        return dateText;
    }

    public TextView getTimeText()
    {
        return timeText;
    }

    public ConstraintLayout getRequestedJobLayout()
    {
        return requestedJobLayout;
    }
}
