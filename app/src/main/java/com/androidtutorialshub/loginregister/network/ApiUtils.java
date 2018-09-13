package com.androidtutorialshub.loginregister.network;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://demo-app-and.herokuapp.com/";

    public static ApiInterface getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(ApiInterface.class);
    }
}
