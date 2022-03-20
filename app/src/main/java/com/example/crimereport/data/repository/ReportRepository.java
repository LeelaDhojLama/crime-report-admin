package com.example.crimereport.data.repository;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.example.crimereport.FireStoreListener;
import com.example.crimereport.data.firestore.*;
import com.example.crimereport.models.Report;
import com.example.crimereport.ui.MapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import okhttp3.ResponseBody;
import retrofit2.Callback;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportRepository extends BaseRepository {

    FireStoreListener fireStoreListener;
    public ReportRepository(MapFragment mapFragment){
        fireStoreListener = mapFragment;
    }

    public ReportRepository(){

    }

    @Override
    public void addReport(final Report report, final String token) {

        Uri file = Uri.fromFile(new File((report.getImage())));
        final StorageReference fileReference = storageReference.child("images/"+file.getLastPathSegment());
        final UploadTask uploadTask = fileReference.putFile(file);

        Log.d("list",report.getDescription());
        Log.d("list",report.getLocation());
        Log.d("list",report.getImage());

//        final String token = token;

        fileReference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d("download",uri.toString());
                        Map<String,String> reportData = new HashMap<>();
                        reportData.put("image",uri.toString());
                        reportData.put("description",report.getDescription());
                        reportData.put("location",report.getLocation());
//                        reportData.put("createdate", report.get);

                        db.collection("report").add(reportData).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("ReportRepository","Success");
                                sendNotificationToUser(token,report.getDescription());
                                        report.getDescription();
                                fireStoreListener.success(true);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    @Override
    public void deleteReport() {

    }

    @Override
    public List<Report> getReports() {
        final List<Report>mReport = new ArrayList<>();
        db.collection("report").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    List<DocumentSnapshot> myListOfDocuments = task.getResult();

                    for(DocumentSnapshot snapshot:task.getResult()){
                        Report report = new Report();
                        report.setDescription(snapshot.getString("description"));
                        Log.d("report description",report.getDescription());
                        mReport.add(report);
                    }
                }
            }
        });

        return mReport;
    }

    private void sendNotificationToUser(String token,String body) {
        RootModel rootModel = new RootModel(token, new NotificationModel("Title", body), new DataModel("Name", "30"));

        FirebaseServices apiService =  FirebaseApiClient.getClient().create(FirebaseServices.class);
        retrofit2.Call<ResponseBody> responseBodyCall = apiService.sendNotification(rootModel);

        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.d("TAG","Successfully notification send by using retrofit.");
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {

            }
        });
    }
}
