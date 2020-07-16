package com.stratone.bmotion.rest;

import com.stratone.bmotion.response.ResponseLogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("api/user/login")
    Call<ResponseLogin> login(
            @Field("nip") String username,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("api/user/register")
    Call<ResponseBody> register(
            @Field("nip") String nip,
            @Field("name") String name,
            @Field("profession") String profession,
            @Field("email") String email,
            @Field("password") String password,
            @Field("phone") String phone
    );
    }
