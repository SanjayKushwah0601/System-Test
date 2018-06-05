package com.example.sanjay.sanjaysystemtest.Utils;

import com.example.sanjay.sanjaysystemtest.Database.Model.GetUsers.ResponseUser;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Constants {

    String BASE_URL = "http://www.bacancy.com";

    String GET_USERS = "/btdemo/coinason/api/user/get_post_data";
    String ADD_USER = "/btdemo/coinason/api/user/post_data";


    @GET(GET_USERS)
    Call<ResponseUser> getUsersList();


    @FormUrlEncoded
    @POST(ADD_USER)
    Call<ResponseBody> addUser(@Field("firstname") String firstName,
                               @Field("lastname") String lastName,
                               @Field("phonenumber") String number,
                               @Field("emailid") String email);
}
