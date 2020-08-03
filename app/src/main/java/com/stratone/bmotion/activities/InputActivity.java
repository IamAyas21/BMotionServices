package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stratone.bmotion.R;
import com.stratone.bmotion.adapter.FuelAdapter;
import com.stratone.bmotion.model.Fuel;
import com.stratone.bmotion.model.OrderDetails;
import com.stratone.bmotion.model.Orders;
import com.stratone.bmotion.model.User;
import com.stratone.bmotion.response.ResponseFuels;
import com.stratone.bmotion.response.ResponseOrders;
import com.stratone.bmotion.rest.ApiClient;
import com.stratone.bmotion.rest.ApiInterface;
import com.stratone.bmotion.utils.SessionManager;
import com.stratone.bmotion.utils.UploadService;

import java.util.ArrayList;
import java.util.List;

public class InputActivity extends AppCompatActivity {
    @BindView(R.id.lvFuelSubsidy)
    ListView fuelSubsidy;

    @BindView(R.id.lvFuelNonSubsidy)
    ListView fuelNonSubsidy;

    @BindView(R.id.OkOrder)
    Button order;

    @BindView(R.id.imgBtnBack)
    ImageView back;

    @BindView(R.id.Quota)
    TextView quota;

    private User user;
    private FuelAdapter adapter;
    private List<Fuel> fuelListSubsidy, fuelListNonSubsidy;
    private static final String TAG = "InputActivity";
    private ProgressDialog pDialog;

    ApiInterface apiService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        apiService = ApiClient.getClient().create(ApiInterface.class);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();
        user = sessionManager.getUserDetails();
        quota.setText(user.getQuota());

        fuelList("Y");
        fuelList("N");

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fuelListSubsidy.size() > 0 || fuelListNonSubsidy.size() > 0)
                {
                    Order();
                }
                else
                {
                    Toast.makeText(InputActivity.this,getApplicationContext().getResources().getString(R.string.error_cant_empty),Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }

    private void fuelList(final String isSubsidy)
    {
        apiService.fuels(isSubsidy).enqueue(new Callback<ResponseFuels>() {
            @Override
            public void onResponse(Call<ResponseFuels> call, Response<ResponseFuels> response) {
                if(response.isSuccessful())
                {
                    if(response.body().getStatus().equals("success"))
                    {
                        if(isSubsidy.equals("Y"))
                        {
                            fuelListSubsidy = response.body().getData();
                            adapter = new FuelAdapter(InputActivity.this, R.layout.list_item_fuel, fuelListSubsidy);
                            fuelSubsidy.setAdapter(adapter);
                            adapter.setListViewHeightBasedOnChildren(fuelSubsidy);
                        }
                        else{
                            fuelListNonSubsidy = response.body().getData();
                            adapter = new FuelAdapter(InputActivity.this, R.layout.list_item_fuel, fuelListNonSubsidy);
                            fuelNonSubsidy.setAdapter(adapter);
                            adapter.setListViewHeightBasedOnChildren(fuelNonSubsidy);
                        }
                    }
                    else {
                        Toast.makeText(InputActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else if(!response.isSuccessful())
                {
                    Toast.makeText(InputActivity.this,getApplicationContext().getResources().getString(R.string.login_failed),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseFuels> call, Throwable t) {
                Log.e(TAG,"onFailure: "+t.getLocalizedMessage());
                Toast.makeText(InputActivity.this,getApplicationContext().getResources().getString(R.string.connect_server_failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Order()
    {
        pDialog = new ProgressDialog(InputActivity.this);
        pDialog.setMessage(getResources().getString(R.string.prompt_loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();

        Orders orders = new Orders();
        List<OrderDetails> orderDetails = new ArrayList<>();
        OrderDetails orderDetail ;
        for(int i=0; i < fuelListSubsidy.size(); i++){
            if(fuelListSubsidy.get(i).getLiter() > 0)
            {
                orderDetail = new OrderDetails();
                orderDetail.setFuelId(fuelListSubsidy.get(i).getFuelId());
                orderDetail.setLiter(fuelListSubsidy.get(i).getLiter());
                orderDetails.add(orderDetail);
            }
        }

        for(int i=0; i < fuelListNonSubsidy.size(); i++){
            if(fuelListNonSubsidy.get(i).getLiter() > 0)
            {
                orderDetail = new OrderDetails();
                orderDetail.setFuelId(fuelListNonSubsidy.get(i).getFuelId());
                orderDetail.setLiter(fuelListNonSubsidy.get(i).getLiter());
                orderDetails.add(orderDetail);
            }
        }

        orders.setNIP(user.getNIP());
        orders.setOrderDetails(orderDetails);
        apiService.order(orders).enqueue(new Callback<ResponseOrders>() {
            @Override
            public void onResponse(Call<ResponseOrders> call, Response<ResponseOrders> response) {
                if(response.body().getStatus().equals("success"))
                {
                    pDialog.dismiss();
                    Intent intent = new Intent(InputActivity.this, OrderActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent.putExtra("orderNo",response.body().getData().getOrderNo());
                    startActivity(intent);
                    finish();
                }
                else {
                    pDialog.dismiss();
                    Toast.makeText(InputActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseOrders> call, Throwable t) {
                Log.e(TAG,"onFailure: "+t.getLocalizedMessage());
                Toast.makeText(InputActivity.this,getApplicationContext().getResources().getString(R.string.connect_server_failed),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Back()
    {
        Intent i = new Intent(InputActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Back();
    }
}
