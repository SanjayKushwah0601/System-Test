package com.example.sanjay.sanjaysystemtest.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class Util {

    /**
     * @param email email address
     * @return true if valid email address else it returns false
     */
    public static boolean isValidEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    /**
     * @param mobile 10 digit indian mobile number
     * @return true if valid mobile number else it returns false
     */
    public static boolean isValidMobile(String mobile) {
        return (!TextUtils.isEmpty(mobile) && Pattern.matches("^[6-9]\\d{9}$", mobile));
    }


    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connectivityManager
                    = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        } catch (Exception e) {
            return false;
        }
    }
}
