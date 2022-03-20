package com.example.crimereport.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.crimereport.R;
import com.example.crimereport.listener.CrimeListClickListener;
import com.example.crimereport.models.Report;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    List<Report> mReports;
    Context mContext;
    CrimeListClickListener crimeListClickListener;

    public NotificationAdapter(List<Report> reports, Context context,CrimeListClickListener crimeListClickListener){
        mReports = reports;
        mContext = context;
        this.crimeListClickListener = crimeListClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Report report = mReports.get(position);
        holder.mDetail.setText(report.getDescription());
        holder.mDate.setText(report.getDate());
        holder.mtitle.setText(report.getTitle());
        holder.mLocation.setText(report.getAddress());
        Glide.with(mContext).load(report.getImage()).into(holder.mImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crimeListClickListener.onClick(report);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mReports.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView mtitle,mDetail,mDate,mLocation;
        ImageView mImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtitle = itemView.findViewById(R.id.title);
            mDate = itemView.findViewById(R.id.date);
            mDetail = itemView.findViewById(R.id.description);
            mImage = itemView.findViewById(R.id.report_image);
            mLocation = itemView.findViewById(R.id.location);

        }
    }
}
