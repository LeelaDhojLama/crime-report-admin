package com.example.crimereport.data.firestore;

import com.example.crimereport.models.FirebaseConstants;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface FirebaseServices {

    @Headers({"Authorization: key=" + FirebaseConstants.SERVER_KEY, "Content-Type:application/json"})
    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body RootModel root);
}
