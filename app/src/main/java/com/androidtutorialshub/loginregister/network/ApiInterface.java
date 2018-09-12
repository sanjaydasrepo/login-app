package com.androidtutorialshub.loginregister.network;

import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.model.UserResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST
    @FormUrlEncoded
    Call<UserResp> saveUser(@Body UserResp userResp);

    @POST
    Call<UserResp> login();
}
