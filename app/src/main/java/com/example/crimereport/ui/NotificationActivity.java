package com.example.crimereport.ui;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.crimereport.R;
import com.example.crimereport.adapter.NotificationAdapter;
import com.example.crimereport.data.repository.ReportRepository;
import com.example.crimereport.models.Notification;
import com.example.crimereport.models.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NotificationActivity extends BaseActivity {

    ReportRepository reportRepository;
    NotificationAdapter notificationAdapter;
    RecyclerView mRecyclerView;
    ProgressBar progressBar;

    Geocoder geocoder;
    List<Address> addresses;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        mRecyclerView = findViewById(R.id.notification_list);
        progressBar = findViewById(R.id.progress_bar);
        reportRepository = new ReportRepository();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        geocoder = new Geocoder(this, Locale.getDefault());



        final List<Report> reports = new ArrayList<>();
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("report").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> myListOfDocuments = task.getResult();

                    for(DocumentSnapshot snapshot:task.getResult()){
                        Report report = new Report();
                        report.setDescription(snapshot.getString("description"));
                        report.setImage(snapshot.getString("image"));

                        Log.d("latalng",snapshot.getString("location"));

                        String latlng[] = snapshot.getString("location").split(",");

                        try {
                            addresses = geocoder.getFromLocation(Double.valueOf(latlng[0]), Double.valueOf(latlng[1]), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            Log.d("address",addresses.toString());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        String city = addresses.get(0).getLocality();
                        String state = addresses.get(0).getAdminArea();
                        String country = addresses.get(0).getCountryName();
                        String postalCode = addresses.get(0).getPostalCode();
                        String knownName = addresses.get(0).getFeatureName();

                        report.setLocation(knownName+","+city+","+country);
//                        report.setDate(snapsho);

                        Log.d("reportde",report.getDescription());
                        reports.add(report);
                    }
                    Log.d("NotificationActivity",String.valueOf(reports.size()));
//                    notificationAdapter = new NotificationAdapter(reports, NotificationActivity.this);
                    mRecyclerView.setAdapter(notificationAdapter);
                    progressBar.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.toString());
            }
        });

    }

    @Override
    int getContentView() {
        return R.layout.activity_notification;
    }
}
