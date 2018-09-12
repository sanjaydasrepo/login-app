package com.androidtutorialshub.loginregister.network;

import com.androidtutorialshub.loginregister.model.User;

import retrofit2.Call;
import retrofit2.http.POST;

public interface UserClient {
    @POST("login")
    Call<User> login();
}
