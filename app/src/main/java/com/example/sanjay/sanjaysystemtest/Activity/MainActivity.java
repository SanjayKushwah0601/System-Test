package com.example.sanjay.sanjaysystemtest.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.evernote.android.job.JobRequest;
import com.example.sanjay.sanjaysystemtest.App;
import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.example.sanjay.sanjaysystemtest.Database.MyDatabase;
import com.example.sanjay.sanjaysystemtest.Jobs.MyJobService;
import com.example.sanjay.sanjaysystemtest.Jobs.SyncJob;
import com.example.sanjay.sanjaysystemtest.R;
import com.example.sanjay.sanjaysystemtest.Utils.AppPreference;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();
    private RecyclerView recyclerView;
    private static UserListAdapter adapter;
    private static List<User> userList = new ArrayList<>();
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (AppPreference.isFirstRun(this) || (System.currentTimeMillis() - AppPreference.getSynTime(this) > 86430000)) {
            schedulePeriodicJob();

            if (AppPreference.isFirstRun(this))
                prepareDataForFirstRun();
        }
        initUI();
    }

    private void initUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new dispatcher using the Google Play driver.
                Intent intent = new Intent(MainActivity.this, AddUserActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new UserListAdapter(userList);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        new MyTask().execute();
    }


    private void schedulePeriodicJob() {
        int jobId = new JobRequest.Builder(SyncJob.TAG)
                .setPeriodic(TimeUnit.MINUTES.toMillis(60), TimeUnit.MINUTES.toMillis(5))
                .setRequiresDeviceIdle(false)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    private void scheduleJob(FirebaseJobDispatcher dispatcher) {
        Bundle myExtrasBundle = new Bundle();
        myExtrasBundle.putString("some_key", "some_value");

        Job myJob = dispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("my-unique-tag")
                .setRecurring(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(60, 61))
                .setReplaceCurrent(true)
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setExtras(myExtrasBundle)
                .build();

        dispatcher.mustSchedule(myJob);
    }


    /**
     * Show the data when the first time user opens the application
     */
    private void prepareDataForFirstRun() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("user");

        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                List<User> users = new ArrayList<>();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    user.setId(userSnapshot.getKey());
                    users.add(user);
                    Log.d(TAG, "Value is: " + user);
                }

                // Remove the listener when all the user has been fetched from firebase
                myRef.removeEventListener(listener);


                Collections.reverse(users);
                // FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(view.getContext()));
                // scheduleJob(dispatcher);
                new MySetDataTask(users).execute();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        };

        myRef.addValueEventListener(listener);
    }


    private static class MySetDataTask extends AsyncTask<Void, Void, Void> {

        private List<User> users;

        MySetDataTask(List<User> users) {
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

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new MyTask().execute();

        }
    }


    private static class MyTask extends AsyncTask<Void, Void, List<User>> {

        @Override
        protected List<User> doInBackground(Void... voids) {
            return MyDatabase.getInstance(App.context)
                    .getUserDao().getAllUsers();
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            userList.addAll(users);
            adapter.notifyDataSetChanged();
            AppPreference.setFirstRun(App.context, false);
            Toast.makeText(App.context, "Last Sync at: " + new Date(AppPreference.getSynTime(App.context)).toString(), Toast.LENGTH_LONG).show();
        }
    }
}
