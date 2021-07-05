package com.example.iasubstituteteacher.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.AcceptedJobs;
import com.example.iasubstituteteacher.JobsThing.AcceptedJobsInfoActivity;
import com.example.iasubstituteteacher.R;

import java.util.ArrayList;

/**
 * A class regarding the Adapter, which is necessary in the function of the recycler view.
 * Specifically, for the AcceptedJobs recycler view.
 */

public class AcceptedJobsAdapter extends RecyclerView.Adapter<AcceptedJobsViewHolder>{

    ArrayList<AcceptedJobs> mData;
    private Context context;

    public AcceptedJobsAdapter(ArrayList<AcceptedJobs> data, Context context)
    {
        this.mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AcceptedJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.
                accepted_jobs_row_view, parent, false);

        AcceptedJobsViewHolder holder = new AcceptedJobsViewHolder(myView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedJobsViewHolder holder, int position) {
        boolean Active = mData.get(position).isActive();
        final String stringActive = Boolean.toString(Active);

        holder.subjectText.setText(mData.get(position).getSubject());
        holder.dateText.setText("Date: " + mData.get(position).getDate());
        holder.timeText.setText("Time: " + mData.get(position).getTime());

        holder.getAcceptedJobLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AcceptedJobsInfoActivity.class);
                intent.putExtra("acceptSubject", mData.get(position).getSubject());
                intent.putExtra("acceptJobsID", mData.get(position).getJobsId());
                intent.putExtra("acceptDate", mData.get(position).getDate());
                intent.putExtra("acceptTime", mData.get(position).getTime());
                intent.putExtra("acceptLocation", mData.get(position).getLocation());
                intent.putExtra("acceptActive", stringActive);
                intent.putExtra("acceptLessonPlan", mData.get(position).getLessonPlan());
                intent.putExtra("acceptUsersEmail", mData.get(position).getUsersEmail());
                intent.putExtra("acceptUsersID", mData.get(position).getUserId());
                intent.putExtra("acceptAcceptorsEmail", mData.get(position).getAcceptorsEmail());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
