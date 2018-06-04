package com.example.sanjay.sanjaysystemtest.Utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class AlertUtil {

    public static void showAlert(final Activity context, String message, final boolean isFinish) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Alert!!")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isFinish)
                            context.finish();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.dismiss();
                        if (isFinish)
                            context.finish();
                    }
                })
                .show();
    }
}
