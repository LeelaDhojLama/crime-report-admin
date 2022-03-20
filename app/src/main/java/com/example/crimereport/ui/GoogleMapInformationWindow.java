package com.example.crimereport.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.crimereport.R;
import com.example.crimereport.models.Report;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class GoogleMapInformationWindow implements GoogleMap.InfoWindowAdapter {

    private Context context;
    private Report report;

    public GoogleMapInformationWindow(Context context, Report report) {
        this.context = context;
        this.report = report;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity)context).getLayoutInflater()
                .inflate(R.layout.view_crime_information, null);

        TextView title = view.findViewById(R.id.title);
        TextView description = view.findViewById(R.id.description);
        TextView address = view.findViewById(R.id.location);
        TextView date = view.findViewById(R.id.date);
        ImageView img = view.findViewById(R.id.report_image);

       title.setText("Title: "+report.getTitle());
       description.setText("Description: "+report.getDescription());
       address.setText("Address: "+report.getAddress());
       date.setText("Date :"+report.getDate());
        Glide.with(view).load(report.getImage()).into(img);
        return view;
    }
}
