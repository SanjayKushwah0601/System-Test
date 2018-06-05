package com.example.sanjay.sanjaysystemtest;

import android.app.Application;
import android.content.Context;

import com.evernote.android.job.JobManager;
import com.example.sanjay.sanjaysystemtest.Jobs.MyJobCreator;
import com.example.sanjay.sanjaysystemtest.Utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends Application {

    public static App context;
    private static Retrofit retrofit;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JobManager.create(this).addJobCreator(new MyJobCreator());
    }

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
