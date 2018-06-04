package com.example.sanjay.sanjaysystemtest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.sanjay.sanjaysystemtest.Database.Model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    private List<User> dataList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFullName, tvEmail, tvMobile;

        MyViewHolder(View view) {
            super(view);
            tvFullName = view.findViewById(R.id.textView_name);
            tvEmail = view.findViewById(R.id.textView_email);
            tvMobile = view.findViewById(R.id.textView_mobile);
        }
    }


    public UserListAdapter(List<User> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_user, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        final User user = dataList.get(position);
        holder.tvFullName.setText(user.getFirstName() + " " + user.getLastName());
        holder.tvEmail.setText("Email: " + user.getEmailId());
        holder.tvMobile.setText("Mobile: " + user.getMobileNumber());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public String formatDate(String date) throws ParseException {

        SimpleDateFormat spf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        Date newDate = spf.parse(date);
        spf = new SimpleDateFormat("dd MMM yyyy hh:mm:ss");
        date = spf.format(newDate);
        System.out.println(date);
        return date;
    }
}
