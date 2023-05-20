package com.example.regenerationoil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.example.regenerationoil.JobEntity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class NextActivity extends AppCompatActivity {

    private AppDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context context = getApplicationContext();
        database = Room.databaseBuilder(context, AppDatabase.class, "regeneration_oil")
                .fallbackToDestructiveMigration()
                .build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);

        String jobIdString = getIntent().getStringExtra("jobId");
        int jobId = Integer.parseInt(jobIdString);


        // Выполнение операций с базой данных в фоновом потоке
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("TAG", String.valueOf(jobId));
                JobEntity savedJob = database.jobDao().getJobById(jobId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView jobTextView = findViewById(R.id.jobTextView);
                        jobTextView.setText(savedJob.toString());
                    }
                });
            }
        });

        thread.start();
    }

}
