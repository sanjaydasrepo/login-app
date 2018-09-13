package com.androidtutorialshub.loginregister.network;

import com.androidtutorialshub.loginregister.model.Login;
import com.androidtutorialshub.loginregister.model.Register;
import com.androidtutorialshub.loginregister.model.User;
import com.androidtutorialshub.loginregister.model.UserResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("/sign-up")
    Call<String> saveUser(@Body Register register);

    @POST("/login")
    Call<UserResp> login(@Body Login login);
}
