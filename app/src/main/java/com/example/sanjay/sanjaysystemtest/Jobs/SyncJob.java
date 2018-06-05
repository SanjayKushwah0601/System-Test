package com.example.sanjay.sanjaysystemtest.Jobs;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.example.sanjay.sanjaysystemtest.Activity.MainActivity;
import com.example.sanjay.sanjaysystemtest.App;
import com.example.sanjay.sanjaysystemtest.Database.Model.GetUsers.ResponseUser;
import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.example.sanjay.sanjaysystemtest.Database.MyDatabase;
import com.example.sanjay.sanjaysystemtest.Utils.AppPreference;
import com.example.sanjay.sanjaysystemtest.Utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncJob extends Job {

    public static final String TAG = "job_demo_tag";
    private ValueEventListener listener;
    private List<User> users;

    @Override
    @NonNull
    protected Result onRunJob(@NonNull Params params) {
        Log.e("MyJob", "Started !!");
        getUsers();
        Log.e("MyJob", "Started !!!");
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(SyncJob.TAG)
                .setExecutionWindow(30_000L, 40_000L)
                .build()
                .schedule();
    }

    private void getUsers() {
        /*Create handle for the RetrofitInstance interface*/
        Constants service = App.getRetrofitInstance().create(Constants.class);
        Call<ResponseUser> call = service.getUsersList();
        call.enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                Log.e("GetUserResponse", response.toString());
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        List<User> users = response.body().getUserData();
                        Collections.reverse(users);
                        new MyTask(users).execute();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {

            }
        });
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
