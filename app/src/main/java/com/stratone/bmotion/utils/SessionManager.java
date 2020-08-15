package com.stratone.bmotion.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.activities.DashboardActivity;
import com.stratone.bmotion.activities.LoginActivity;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseUser;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;

import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    Context _context;
    User user;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHivePref";

    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_NIP = "nip";
    private static final String KEY_EXPIRES = "expires";
    private static final String KEY_QUOTA = "quota";
    private static final String KEY_PURCHASED_BBM = "purchaseBBM";
  /*  private static final String KEY_PHONE = "phone";
    private static final String KEY_PASSWORD = "password";*/

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(User user){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, user.getName());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_NIP, user.getNIP());
        editor.putString(KEY_QUOTA, user.getQuota());
        editor.putString(KEY_PURCHASED_BBM, user.getPurchaseBBM());
     /*   editor.putString(KEY_PHONE, user.getPhone());
        editor.putString(KEY_PASSWORD, user.getPassword());*/

        Date date = new Date();
        //Set user session for next 7 days
        long millis = date.getTime() + (7 * 24 * 60 * 60 * 1000);
        editor.putLong(KEY_EXPIRES, millis);
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public User getUserDetails(){
        User user = new User();
        user.setName(pref.getString(KEY_NAME, null));
        user.setEmail(pref.getString(KEY_EMAIL, null));
        user.setNIP(pref.getString(KEY_NIP, null));
        user.setQuota(pref.getString(KEY_QUOTA, null));
        user.setPurchaseBBM(pref.getString(KEY_PURCHASED_BBM, null));
        /*user.setPhone(pref.getString(KEY_PHONE, null));
        user.setPassword(pref.getString(KEY_PASSWORD, null));*/
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        Date currentDate = new Date();
        long millis = pref.getLong(KEY_EXPIRES, 0);
        if (millis == 0) {
            return false;
        }
        Date expiryDate = new Date(millis);
        return currentDate.before(expiryDate);
        /*return pref.getBoolean(IS_LOGIN, false);*/
    }

   /* public User refreshSession(final Context context)
    {
        user = getUserDetails();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.limitquota(user.getNIP()).enqueue(new Callback<ResponseUser>() {
            @Override
            public void onResponse(Call<ResponseUser> call, Response<ResponseUser> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getStatus().equals("success"))
                    {
                        user = response.body().getUser();
                        SessionManager sessionManager = new SessionManager(context);
                        sessionManager.createLoginSession(user);
                    }
                    else {
                        Toast.makeText(context,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!response.isSuccessful())
                {
                    Toast.makeText(context,context.getResources().getString(R.string.login_failed),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUser> call, Throwable t) {
                Toast.makeText(context,context.getResources().getString(R.string.connect_server_failed),Toast.LENGTH_SHORT).show();
            }
        });
        return user;
    }*/
}
