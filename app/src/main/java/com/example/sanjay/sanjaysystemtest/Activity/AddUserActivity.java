package com.example.sanjay.sanjaysystemtest.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sanjay.sanjaysystemtest.App;
import com.example.sanjay.sanjaysystemtest.Database.Model.GetUsers.ResponseUser;
import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.example.sanjay.sanjaysystemtest.R;
import com.example.sanjay.sanjaysystemtest.Utils.AlertUtil;
import com.example.sanjay.sanjaysystemtest.Utils.Constants;
import com.example.sanjay.sanjaysystemtest.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Constants service = App.getRetrofitInstance().create(Constants.class);
        Call<ResponseBody> call = service.addUser(firstName, lastName, mobile, email);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("AddUserFailed", response.toString());
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        Log.e("Success", object.toString());
                        if (object.getInt("status") == 200) {
                            AlertUtil.showAlert(AddUserActivity.this, "User added successfuly. User list will be updated on next sync.", true);
                        }
                    } catch (Exception e) {

                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("AddUserFailed", t.getMessage());
                progressBar.setVisibility(View.GONE);
                AlertUtil.showAlert(AddUserActivity.this, "Opps, Something went wrong", false);
            }
        });
    }
}
