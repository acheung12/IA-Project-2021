package com.example.iasubstituteteacher.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.Jobs.RequestedJobs;
import com.example.iasubstituteteacher.JobsThing.OpenJobsInfoActivity;
import com.example.iasubstituteteacher.JobsThing.RequestedJobsActivity;
import com.example.iasubstituteteacher.JobsThing.RequestedJobsInfoActivity;
import com.example.iasubstituteteacher.R;

import java.util.ArrayList;

public class RequestedJobsAdapter extends RecyclerView.Adapter<RequestedJobsViewHolder>
{
    ArrayList<RequestedJobs> mData;
    private Context context;

    public RequestedJobsAdapter(ArrayList<RequestedJobs> data, Context context)
    {
        this.mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestedJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.
                        requested_jobs_row_view, parent, false);

        RequestedJobsViewHolder holder = new RequestedJobsViewHolder(myView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedJobsViewHolder holder, int position) {

        boolean Active = mData.get(position).isActive();
        final String stringActive = Boolean.toString(Active);

        boolean Choice = mData.get(position).isChoice();
        final String stringChoice = Boolean.toString(Choice);

        holder.subjectText.setText(mData.get(position).getSubject());
        holder.dateText.setText("Date: " + mData.get(position).getDate());
        holder.timeText.setText("Time: " +mData.get(position).getTime());

        holder.getRequestedJobLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, RequestedJobsInfoActivity.class);
                intent.putExtra("requestSubject", mData.get(position).getSubject());
                intent.putExtra("requestJobsID", mData.get(position).getJobsId());
                intent.putExtra("requestDate", mData.get(position).getDate());
                intent.putExtra("requestTime", mData.get(position).getTime());
                intent.putExtra("requestLocation", mData.get(position).getLocation());
                intent.putExtra("requestActive", stringActive);
                intent.putExtra("requestLessonPlan", mData.get(position).getLessonPlan());
                intent.putExtra("requestUsersEmail", mData.get(position).getUsersEmail());
                intent.putExtra("requestUsersID", mData.get(position).getUserId());
                intent.putExtra("requestChoice", stringChoice);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
