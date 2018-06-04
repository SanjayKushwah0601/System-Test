package com.example.sanjay.sanjaysystemtest.Jobs;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sanjay.sanjaysystemtest.App;
import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.example.sanjay.sanjaysystemtest.Database.MyDatabase;
import com.example.sanjay.sanjaysystemtest.MainActivity;
import com.example.sanjay.sanjaysystemtest.Utils.AppPreference;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SyncJob extends Job {

    public static final String TAG = "job_demo_tag";
    private ValueEventListener listener;
    private List<User> users;

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {
        // run your job here
        Log.e("MyJob", "Started !!");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                users = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    user.setId(userSnapshot.getKey());
                    users.add(user);
                    Log.d(TAG, "Value is: " + user);
                }

                // Remove the listener when all the user has been fetched from firebase
                myRef.removeEventListener(listener);


//                FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(view.getContext()));
//                scheduleJob(dispatcher);

                new MyTask(users).execute();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        myRef.addValueEventListener(listener);


        Log.e("MyJob", "Started !!!");
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(SyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule();
    }


    private static class MyTask extends AsyncTask<Void, Void, Void> {

        private List<User> users;

        MyTask(List<User> users) {
            this.users = users;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            MyDatabase.getInstance(App.context)
                    .getUserDao()
                    .insert(users);
            AppPreference.setSynTime(App.context, System.currentTimeMillis());

            List<User> users = MyDatabase.getInstance(App.context)
                    .getUserDao().getAllUsers();
            Log.e("Users", users.toString());
            return null;
        }
    }

}
