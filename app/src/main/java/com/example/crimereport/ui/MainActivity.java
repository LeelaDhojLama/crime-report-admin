package com.example.crimereport.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import com.example.crimereport.R;

public class MainActivity extends AppCompatActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar =  findViewById(R.id.my_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Crime Report");

        getSupportFragmentManager().beginTransaction().replace(R.id.root,CrimeReportListFragment.getInstance(),"list").addToBackStack("list").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.toolbar_menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list:
                // do something
                Intent intent = new Intent(MainActivity.this,NotificationActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }
}
