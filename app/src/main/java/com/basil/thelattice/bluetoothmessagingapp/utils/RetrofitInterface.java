package com.basil.thelattice.bluetoothmessagingapp.utils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/api/user/signIn")
    Call<User> executeSignIn(@Body HashMap<String, String> map);

    @POST("/api/user/signUp")
    Call<User> executeSignUp(@Body HashMap<String, String> map);
}