package com.example.pavel.ass_homework_2_service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnStartService;
    private Button btnStartActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGui();
        initListeners();


    }

    public void initGui(){
        btnStartActivity = findViewById(R.id.btn_start_activity);
        btnStartService = findViewById(R.id.btn_start_service);
    }

    public void initListeners(){
        btnStartService.setOnClickListener(new ButtonStartServiceListener());
        btnStartActivity.setOnClickListener(new ButtonStartActivityListener());
    }


    private class ButtonStartServiceListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            startService(MyService.newIntent(MainActivity.this));
        }
    }

    private class ButtonStartActivityListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            startActivity(SecondActivity.newIntent(MainActivity.this));
        }
    }
}
