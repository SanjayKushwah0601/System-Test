package com.example.sanjay.sanjaysystemtest.Database.Model.GetUsers;

import java.util.List;

import com.example.sanjay.sanjaysystemtest.Database.Model.User;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUser {

    @SerializedName("user_data")
    @Expose
    private List<User> userData = null;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private Integer status;

    public List<User> getUserData() {
        return userData;
    }

    public void setUserData(List<User> userData) {
        this.userData = userData;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


    @Override
    public String toString() {
        return "ResponseUser{" +
                "userData=" + userData +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
