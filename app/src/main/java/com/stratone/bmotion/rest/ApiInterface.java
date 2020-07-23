package com.stratone.bmotion.rest;

import com.stratone.bmotion.response.ResponseUser;

import androidx.annotation.Nullable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("api/user/login")
    Call<ResponseUser> login(
            @Field("email") String username,
            @Field("password") String password
    );
    @Multipart
    @POST("api/user/register")
    Call<ResponseUser> registerMultipart(
            @Part("nip") String nip,
            @Part("name") String name,
            @Nullable @Part("profession") String profession,
            @Part("email") String email,
            @Nullable @Part("password") String password,
            @Part("phone") String phone,
            @Part("ktp") String ktp,
            @Part("expdate") String expdate,
            @Part MultipartBody.Part imagektp,
            @Part MultipartBody.Part filepdf
            );

    @FormUrlEncoded
    @POST("api/user/register")
    Call<ResponseUser> registerBase64(
            @Field("nip") String nip,
            @Field("name") String name,
            @Nullable @Field("profession") String profession,
            @Field("email") String email,
            @Nullable @Field("password") String password,
            @Field("phone") String phone,
            @Field("ktp") String ktp,
            @Part MultipartBody.Part imageKtp
    );
    }
