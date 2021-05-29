package com.example.iasubstituteteacher.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iasubstituteteacher.Jobs.OpenJobs;
import com.example.iasubstituteteacher.JobsThing.OpenJobsActivity;
import com.example.iasubstituteteacher.JobsThing.OpenJobsInfoActivity;
import com.example.iasubstituteteacher.R;

import java.util.ArrayList;

public class OpenJobsAdapter extends RecyclerView.Adapter<OpenJobsViewHolder>
{

    ArrayList<OpenJobs> mData;
    private Context context;

    public OpenJobsAdapter(ArrayList<OpenJobs> data, Context context)
    {
        this.mData = data;
        this.context = context;
    }

    @NonNull
    @Override
    public OpenJobsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.open_jobs_row_view,
                parent, false);

        OpenJobsViewHolder holder = new OpenJobsViewHolder(myView);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OpenJobsViewHolder holder, int position) {

        boolean Active = mData.get(position).isActive();
        final String stringActive = Boolean.toString(Active);

        holder.subjectText.setText(mData.get(position).getSubject());
        holder.dateText.setText("Date: " + mData.get(position).getDate());
        holder.timeText.setText("Time: "+ mData.get(position).getTime());

        holder.getOpenJobLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, OpenJobsInfoActivity.class);
                intent.putExtra("openSubject", mData.get(position).getSubject());
                intent.putExtra("openJobsID", mData.get(position).getJobsId());
                intent.putExtra("openDate", mData.get(position).getDate());
                intent.putExtra("openTime", mData.get(position).getTime());
                intent.putExtra("openLocation", mData.get(position).getLocation());
                intent.putExtra("openActive", stringActive);
                intent.putExtra("openLessonPlan", mData.get(position).getLessonPlan());
                intent.putExtra("openUsersEmail", mData.get(position).getUsersEmail());
                intent.putExtra("openUsersID", mData.get(position).getUserId());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
