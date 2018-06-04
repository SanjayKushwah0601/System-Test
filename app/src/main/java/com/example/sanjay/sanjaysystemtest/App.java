package com.example.sanjay.sanjaysystemtest;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.example.sanjay.sanjaysystemtest.Jobs.MyJobCreator;

public class App extends Application {

    public static App context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JobManager.create(this).addJobCreator(new MyJobCreator());
    }
}
