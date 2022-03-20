package com.example.crimereport.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.os.Bundle;
import com.example.crimereport.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.example.crimereport.models.SharedConstant.PREFERENCE;
import static com.example.crimereport.models.SharedConstant.TOKEN;

public class SplashActivity extends BaseActivity {

    Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("instance", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        TOKEN = token;
                        Log.d("token", token);
//                        Toast.makeText(SplashActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
        jumpToActivity();
    }

    private void jumpToActivity(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences prefs = getSharedPreferences(PREFERENCE, MODE_PRIVATE);
                boolean isLogin = prefs.getBoolean("login",false);

                if(isLogin){
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }

                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                startActivity(intent);
                SplashActivity.this.finish();
            }
        },1000);
    }

    @Override
    int getContentView() {
        return R.layout.activity_splash;
    }
}