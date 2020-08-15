package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
/*import butterknife.BindView;
import butterknife.ButterKnife;*/
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.adapter.FuelAdapter;
import com.stratone.bmotion.adapter.PurchaseHistoryAdapter;
import com.stratone.bmotion.model.Fuel;
import com.stratone.bmotion.model.OrderDetails;
import com.stratone.bmotion.model.Orders;
import com.stratone.bmotion.model.PurchaseHistory;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseOrders;
import com.stratone.bmotion.response.ResponsePurchaseHistory;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
   /* @BindView(R.id.lvPurchaseHistory)
    ListView listView;

    @BindView(R.id.Quota)
    TextView quota;

    @BindView(R.id.purchasedBBM)
    TextView purchasedBBM;

    @BindView(R.id.imgBtnBack)
    ImageView back;

    @BindView(R.id.phoneNumber)
    TextView phone;

    @BindView(R.id.dateNow)
    TextView dateNow;*/

    ListView listView;
    TextView quota, purchasedBBM,phone, dateNow, fullName;
    LinearLayout lnWallet, lnHistory, lnHome, lnProfile;
    ImageView back;

    ApiInterface apiService;
    SessionManager sessionManager;

    private User user;
    private PurchaseHistoryAdapter adapter;
    private ProgressDialog pDialog;
    private List<PurchaseHistory> purchaseHistoryList;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        /*ButterKnife.bind(this);*/

        listView = findViewById(R.id.lvPurchaseHistory);
        fullName = findViewById(R.id.fullName);
        quota = findViewById(R.id.Quota);
        purchasedBBM = findViewById(R.id.purchasedBBM);
        phone = findViewById(R.id.phoneNumber);
        dateNow = findViewById(R.id.dateNow);
        back = findViewById(R.id.imgBtnBack);
        lnWallet = findViewById(R.id.lnWallet);
        lnHistory = findViewById(R.id.lnHistory);
        lnHome = findViewById(R.id.lnHome);
        lnProfile= findViewById(R.id.lnProfile);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        user = sessionManager.getUserDetails();

        fullName.setText(user.getName());
        quota.setText(user.getQuota());
        purchasedBBM.setText(user.getPurchaseBBM());
        phone.setText(user.getPhone());

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("MMM yyyy", Locale.getDefault());
        dateNow.setText(df.format(c));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
        purchaseHistory(user.getNIP());

        lnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
            }
        });

        lnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        lnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        lnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }

    private void purchaseHistory(String nip)
    {
        pDialog = new ProgressDialog(ProfileActivity.this);
        pDialog.setMessage(getResources().getString(R.string.prompt_loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        PurchaseHistory purchaseHistory = new PurchaseHistory();
        purchaseHistory.setNIP(nip);

        apiService.profiles(purchaseHistory).enqueue(new Callback<ResponsePurchaseHistory>() {
            @Override
            public void onResponse(Call<ResponsePurchaseHistory> call, Response<ResponsePurchaseHistory> response) {
                if(response.body().getStatus().equals("success"))
                {
                    pDialog.dismiss();
                    purchaseHistoryList = response.body().getData();
                    adapter = new PurchaseHistoryAdapter(ProfileActivity.this, R.layout.list_item_history, purchaseHistoryList);
                    listView.setAdapter(adapter);
                    adapter.setListViewHeightBasedOnChildren(listView);
                }
                else {
                    pDialog.dismiss();
                    Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponsePurchaseHistory> call, Throwable t) {
                pDialog.dismiss();
                Log.e(TAG,"onFailure: "+t.getLocalizedMessage());
                Toast.makeText(ProfileActivity.this,getApplicationContext().getResources().getString(R.string.connect_server_failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Back()
    {
        Intent i = new Intent(ProfileActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Back();
    }
}
