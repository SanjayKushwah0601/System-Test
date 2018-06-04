package com.example.sanjay.sanjaysystemtest.Jobs;

import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.e("MyJob", "Started");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.e("MyJob", "Stopped");
        return false;
    }
}
