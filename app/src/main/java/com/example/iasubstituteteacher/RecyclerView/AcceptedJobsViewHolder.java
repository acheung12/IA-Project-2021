package com.example.iasubstituteteacher.RecyclerView;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.R;

public class AcceptedJobsViewHolder extends RecyclerView.ViewHolder
{
    protected TextView subjectText;
    protected TextView dateText;
    protected TextView timeText;
    protected ConstraintLayout acceptedJobLayout;

    public AcceptedJobsViewHolder(@NonNull View itemView) {
        super(itemView);

        subjectText = itemView.findViewById(R.id.subjectRowText);
        dateText = itemView.findViewById(R.id.dateRowText);
        timeText = itemView.findViewById(R.id.timeRowText);

        this.acceptedJobLayout = itemView.findViewById(R.id.acceptedJob_parent_layout);
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

    public ConstraintLayout getAcceptedJobLayout() {
        return acceptedJobLayout;
    }
}
