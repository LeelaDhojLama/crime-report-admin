package com.example.crimereport.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.crimereport.R;
import com.example.crimereport.adapter.NotificationAdapter;
import com.example.crimereport.models.Report;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static com.example.crimereport.models.SharedConstant.PREFERENCE;
import static com.example.crimereport.models.SharedConstant.TOKEN;

public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    Button loginBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.login_btn);
        progressBar = findViewById(R.id.progress_bar);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValid()){
                    login(username.getText().toString(),password.getText().toString());
                }
            }
        });

    }

    public boolean isValid(){
        if(TextUtils.isEmpty(username.getText())){
            username.setError("Username is Required");
            return false;
        }

        if(TextUtils.isEmpty(password.getText())){
            password.setError("Password is Required");
            return false;
        }

        return true;
    }


    private void login(final String username , final String password){
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("police").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    boolean isAuthenticated = false;
//                    List<DocumentSnapshot> myListOfDocuments = task.getResult();

                    for(DocumentSnapshot snapshot:task.getResult()){

//                        Log.d("testid",username);

                        if(snapshot.getString("username").equals(username) && snapshot.getString("password").equals(password)){

                            isAuthenticated = true;

                            break;
//                            Map<String,String> token = new HashMap<>();

                        }
                    }

                    if(isAuthenticated){
                        SharedPreferences.Editor editor = getSharedPreferences(PREFERENCE, MODE_PRIVATE).edit();
                        editor.putBoolean("login", true);
                        editor.apply();
                        Map<String,String> token = new HashMap<>();
                        token.put("token",TOKEN);
                        Toast.makeText(getApplication(),"Please wait updating token",Toast.LENGTH_SHORT).show();
                        FirebaseFirestore.getInstance().collection("police").document(username).set(token, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){


                                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                                }else{

                                }
                            }
                        });


                    }else{
                        Toast.makeText(getApplication(),"Sorry Username/Password not valid",Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.toString());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Sorry No Connection",Toast.LENGTH_SHORT);
            }
        });
    }
}
