package com.stratone.bmotion.activities;

import androidx.appcompat.app.AppCompatActivity;
/*import butterknife.BindView;
import butterknife.ButterKnife;*/

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.stratone.bmotion.R;

public class SupportActivity extends AppCompatActivity {
   /* @BindView(R.id.imgBtnBack)
    ImageView back;*/

    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        /*ButterKnife.bind(this);*/

        back = findViewById(R.id.imgBtnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });
    }

    private void Back()
    {
        Intent i = new Intent(SupportActivity.this, DashboardActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        Back();
    }
}
