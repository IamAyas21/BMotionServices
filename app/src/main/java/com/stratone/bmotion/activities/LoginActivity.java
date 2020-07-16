package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseLogin;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.SessionManager;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.edtUser)
    EditText edtUser;

    @BindView(R.id.edtPassword)
    EditText edtPassword;

    @BindView(R.id.btnLogin)
    Button login;

    ApiInterface apiService;
    SessionManager sessionManager;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);
        sessionManager = new SessionManager(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private void loginUser()
    {
        String userName = edtUser.getText().toString();
        String password = edtPassword.getText().toString();
        apiService.login(userName,password).enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if(response.isSuccessful())
                {
                    User userLoggedIn = response.body().getUser();
                    sessionManager.createLoginSession(userLoggedIn.getName(),userLoggedIn.getEmail());
                    Intent intent = new Intent(LoginActivity.this,DashboardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.login_success),Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
                else if(!response.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.login_failed),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Log.e(TAG,"onFailure: "+t.getLocalizedMessage());
                Toast.makeText(LoginActivity.this,getApplicationContext().getResources().getString(R.string.connect_server_failed),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
