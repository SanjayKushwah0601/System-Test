package com.example.sanjay.sanjaysystemtest.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.example.sanjay.sanjaysystemtest.R;
import com.example.sanjay.sanjaysystemtest.Utils.AlertUtil;
import com.example.sanjay.sanjaysystemtest.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = AddUserActivity.class.getName();
    EditText etFirstName, etLastName, etEmail, etMobileNumber;
    Button buttonSubmit;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        initUI();
    }

    private void initUI() {
        etFirstName = findViewById(R.id.edit_text_first_name);
        etLastName = findViewById(R.id.edit_text_last_name);
        etEmail = findViewById(R.id.edit_text_email);
        etMobileNumber = findViewById(R.id.edit_text_mobile);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        buttonSubmit = findViewById(R.id.button_submit);
        buttonSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_submit) {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String mobileNumber = etMobileNumber.getText().toString().trim();

            if (TextUtils.isEmpty(firstName)) {
                etFirstName.setError("Please enter first name");
                return;
            } else if (TextUtils.isEmpty(lastName)) {
                etLastName.setError("Please enter last name");
                return;
            } else if (TextUtils.isEmpty(email)) {
                etEmail.setError("Please enter your email");
                return;
            } else if (!Util.isValidEmail(email)) {
                etEmail.setError("Please enter valid email");
                return;
            } else if (TextUtils.isEmpty(mobileNumber)) {
                etMobileNumber.setError("Please enter your mobile number");
                return;
            } else if (!Util.isValidMobile(mobileNumber)) {
                etMobileNumber.setError("Please enter valid mobile number");
                return;
            }

            if (Util.isNetworkAvailable(this)) {
                addNewUser(firstName, lastName, email, mobileNumber);
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * Add new user to the firebase
     *
     * @param firstName mandatory
     * @param lastName  mandatory
     * @param email     mandatory
     * @param mobile    mandatory
     */
    private void addNewUser(String firstName, String lastName, String email, String mobile) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");
        String key = myRef.push().getKey();
        User post = new User(key, firstName, lastName, mobile, email);
        Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, postValues);
        myRef.updateChildren(childUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showAlert(AddUserActivity.this, "User added successfuly. User list will updated on next sync.", true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                AlertUtil.showAlert(AddUserActivity.this, "Opps, Something went wrong", false);
            }
        });
    }
}
